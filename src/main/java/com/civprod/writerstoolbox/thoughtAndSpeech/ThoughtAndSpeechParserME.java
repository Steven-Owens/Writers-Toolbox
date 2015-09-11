/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.util.stream.FlatListCollector;
import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import com.civprod.writerstoolbox.NaturalLanguage.util.PartMapper;
import com.civprod.writerstoolbox.SceneDect.SceneDetectorEventStream;
import com.civprod.writerstoolbox.SceneDect.SceneDetectorFactory;
import com.civprod.writerstoolbox.SceneDect.SceneDetectorModel;
import com.civprod.writerstoolbox.SceneDect.SceneSample;
import com.civprod.writerstoolbox.data.Paragraph;
import com.civprod.writerstoolbox.data.Sentence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.BeamSearch;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechParserME implements ThoughtAndSpeechParser {

    public static final String PART = "part";
    public static final String START = "start";
    public static final String CONTINUE = "continue";
    public static final String OTHER = "other";

    private static final FlatListCollector<String> myFlatListCollector = new FlatListCollector<>();

    public static final int DEFAULT_BEAM_SIZE = 10;

    /**
     * The beam used to search for sequences of thought and speech tag
     * assignments.
     */
    protected BeamSearch<String> beam;

    private Sequence bestSequence;

    /**
     * The model used to assign thought and speech tags to a sequence of tokens.
     */
    protected MaxentModel model;

    /**
     * Initializes the current instance with the specified model and the
     * specified beam size.
     *
     * @param model The model for this chunker.
     * @param beamSize The size of the beam that should be used when decoding
     * sequences.
     */
    public ThoughtAndSpeechParserME(ThoughtAndSpeechModel model, int beamSize) {
        this.model = model.getThoughtAndSpeechModel();
        ThoughtAndSpeechContextGenerator contextGenerator = model.getFactory().getContextGenerator();
        SequenceValidator<String> sequenceValidator = model.getFactory().getSequenceValidator();
        beam = new BeamSearch<String>(beamSize, contextGenerator, this.model, sequenceValidator, 0);
    }

    /**
     * Initializes the current instance with the specified model. The default
     * beam size is used.
     *
     * @param model
     */
    public ThoughtAndSpeechParserME(ThoughtAndSpeechModel model) {
        this(model, DEFAULT_BEAM_SIZE);
    }

    @Override
    public String[] parse(Paragraph<?> inParagraph) {
        Part parsedParts[] = parseParts(inParagraph);
        String rStrings[] = new String[parsedParts.length];
        for (int i = 0; i < parsedParts.length; i++) {
            rStrings[i] = parsedParts[i].content;
        }
        return rStrings;
    }

    @Override
    public Span[] parsePos(Paragraph<?> inParagraph) {
        Part parsedParts[] = parseParts(inParagraph);
        Span rSpans[] = new Span[parsedParts.length];
        for (int i = 0; i < parsedParts.length; i++) {
            rSpans[i] = parsedParts[i].indexIntoOriginal;
        }
        return rSpans;
    }
    
    //visable to package so Evaluator can see this.
    Sequence getBestSequence(String[] tokenizedParagraph){
        return beam.bestSequence(tokenizedParagraph,new Object[tokenizedParagraph.length]);
    }
    
    Sequence getBestSequence(List<String> tokenizedParagraph){
        return getBestSequence(tokenizedParagraph.toArray(new String[tokenizedParagraph.size()]));
    }
    
    Span[] getEvalSpans(String[] tokenizedParagraph){
        Sequence curSequence = getBestSequence(tokenizedParagraph);
        List<Span> evalSpans = new ArrayList<>();
        List<String> outcomes = curSequence.getOutcomes();
        int start = -1;
        String type = ""; 
        for (int i = 0; i < outcomes.size(); i++) {
            String curOutcome = outcomes.get(i);
            if (curOutcome.startsWith(START) && (start < 0)) {
                start = i;
                type = curOutcome.replaceAll(START, "").replaceAll(":","").trim();
            } else if (curOutcome.equals(OTHER) && (start >= 0)) {
                //i is at least 1 since only one of these blocks can match per loop and the 'curOutcome.startsWith("b-")' condishion must trigger first
                Span newSpan = new Span(start, i-1,type);
                evalSpans.add(newSpan);
                start = -1;
            }
        }
        
        return evalSpans.toArray(new Span[evalSpans.size()]);
    }

    public Part[] parseParts(Paragraph<?> inParagraph) {
        List<String> tokenizedParagraph = inParagraph.getSentences().parallelStream()
                .filter((Object curItem) -> curItem instanceof Sentence)
                .map((Object curItem) -> (Sentence) curItem)
                .map((Sentence curSentence) -> curSentence.getTokens())
                .collect(myFlatListCollector);
        String paragraphContent = inParagraph.getContent();
        List<Part> partedParagraph = PartMapper.map(paragraphContent, tokenizedParagraph);
        bestSequence = getBestSequence(tokenizedParagraph);
        List<String> outcomes = bestSequence.getOutcomes();
        List<Part> outThoughtOrSpeech = new java.util.ArrayList<>(1);
        //using to both store the start of the current thought or speech and as a flag that we found a start of a thought of speech. 
        //-1 means we aren't in a thought or speech  
        int start = -1;
        for (int i = 0; i < partedParagraph.size(); i++) {
            String curOutcome = outcomes.get(i);
            Part curPart = partedParagraph.get(i);
            Span curSpan = curPart.indexIntoOriginal;
            if ((curOutcome.startsWith(START) && (start < 0))) {
                start = curSpan.getStart();
            } else if (curOutcome.equals(OTHER) && (start >= 0)) {
                //i is at least 1 since only one of these blocks can match per loop and the 'curOutcome.startsWith("b-")' condishion must trigger first
                Span prevSpan = partedParagraph.get(i - 1).indexIntoOriginal;
                Span newPartSpan = new Span(start, prevSpan.getEnd());
                outThoughtOrSpeech.add(new Part(paragraphContent.substring(newPartSpan.getStart(), newPartSpan.getEnd()), newPartSpan));
                start = -1;
            }
        }
        return outThoughtOrSpeech.toArray(new Part[outThoughtOrSpeech.size()]);
    }

    public static ThoughtAndSpeechModel train(String languageCode,
            ObjectStream<ThoughtAndSpeechSample> samples, ThoughtAndSpeechParserFactory sdFactory,
            TrainingParameters mlParams) throws IOException {

        Map<String, String> manifestInfoEntries = new HashMap<String, String>();

        // TODO: Fix the EventStream to throw exceptions when training goes wrong
        ThoughtAndSpeechEventStream eventStream = new ThoughtAndSpeechEventStream(samples,
                sdFactory.getContextGenerator());

        EventTrainer eventTrainer = TrainerFactory.getEventTrainer(mlParams.getSettings(), manifestInfoEntries);
        MaxentModel sentModel = eventTrainer.train(eventStream);

        return new ThoughtAndSpeechModel(languageCode, sentModel, manifestInfoEntries,
                sdFactory);

    }
}
