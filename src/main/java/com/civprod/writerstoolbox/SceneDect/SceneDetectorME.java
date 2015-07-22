/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import com.civprod.writerstoolbox.SceneDect.SceneBreak.SceneBreakDetector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import opennlp.tools.chunker.ChunkerContextGenerator;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.ml.TrainerFactory;
import opennlp.tools.ml.model.AbstractModel;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.ml.model.TrainUtil;
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
public class SceneDetectorME implements SceneDetector {

    /**
     * Constant indicates a Scene split.
     */
    public static final String SPLIT = "s";

    /**
     * Constant indicates no Scene split.
     */
    public static final String NO_SPLIT = "n";
    
    public static final String SCENE_START = "b_S";
    public static final String SCENE_CONTINUE = "c_S";
    public static final String SCENE_BREAK = "sb";
    
    // Note: That should be inlined when doing a re-factoring!
  private static final Double ONE = new Double(1);

    protected MaxentModel model;

    /**
     * The feature context generator.
     */
    private final SceneContextGenerator cgen;

    /**
     * The list of probabilities associated with each decision.
     */
    private List<Double> sceneProbs = new ArrayList<Double>();

    /**
     * Initializes the current instance with the specified model. The default
     * beam size is used.
     *
     * @param model
     */
    public SceneDetectorME(SceneDetectorModel model) {
        SceneDetectorFactory sdFactory = model.getFactory();
        this.model = model.getSceneBreakDetectorModel();
        cgen = sdFactory.getSceneContextGenerator();
    }

    @Override
    public Span[] detectScenes(String[] inParagraphs) {
        List<Integer> positions = new ArrayList<Integer>(inParagraphs.length);
        for (int i = 0, end = inParagraphs.length, index = 0; i < end; i++) {
            double[] probs = model.eval(cgen.getContext(inParagraphs, i));
            String bestOutcome = model.getBestOutcome(probs);
            if (bestOutcome.equals(SPLIT) && isAcceptableBreak(inParagraphs, index, i)) {
                if (index != i) {
                    positions.add(i);
                    sceneProbs.add(probs[model.getIndex(bestOutcome)]);
                }
                index = i + 1;
            }
        }

        int[] starts = new int[positions.size()];
        for (int i = 0; i < starts.length; i++) {
            starts[i] = positions.get(i);
        }
        // string does not contain sentence end positions
        if (starts.length == 0) {
            sceneProbs.add(1d);
            return new Span[]{new Span(0, inParagraphs.length)};
        }
        // Now convert the sent indexes to spans
        boolean leftover = starts[starts.length - 1] != inParagraphs.length;
        Span[] spans = new Span[leftover ? starts.length + 1 : starts.length];
        for (int si = 0; si < starts.length; si++) {
            int start, end;
            if (si == 0) {
                start = 0;
            } else {
                start = starts[si - 1];
            }
            end = starts[si];
            spans[si] = new Span(start, end);
        }

        if (leftover) {
            spans[spans.length - 1] = new Span(starts[starts.length - 1], inParagraphs.length);
            sceneProbs.add(ONE);
        }

        return spans;
    }
    
    /**
   * Returns the probabilities associated with the most recent
   * calls to sentDetect().
   *
   * @return probability for each sentence returned for the most recent
   * call to sentDetect.  If not applicable an empty array is
   * returned.
   */
  public double[] getSentenceProbabilities() {
    double[] sentProbArray = new double[sceneProbs.size()];
    for (int i = 0; i < sentProbArray.length; i++) {
      sentProbArray[i] = sceneProbs.get(i);
    }
    return sentProbArray;
  }

    @Override
    public List<Span> detectScenes(List<String> inParagraphs) {
        return java.util.Arrays.asList(detectScenes(inParagraphs.toArray(new String[inParagraphs.size()])));
    }

    private boolean isAcceptableBreak(String[] inParagraphs, int index, int i) {
        return true;
    }
    
    public static SceneDetectorModel train(String languageCode,
      ObjectStream<SceneSample> samples, SceneDetectorFactory sdFactory,
      TrainingParameters mlParams) throws IOException {
        
        Map<String, String> manifestInfoEntries = new HashMap<String, String>();

    // TODO: Fix the EventStream to throw exceptions when training goes wrong
    SceneDetectorEventStream eventStream = new SceneDetectorEventStream(samples,
        sdFactory.getSceneContextGenerator());

        //AbstractModel sentModel = TrainUtil.train(eventStream,
        //    mlParams.getSettings(), manifestInfoEntries);
        EventTrainer eventTrainer = TrainerFactory.getEventTrainer(mlParams.getSettings(), manifestInfoEntries);
        MaxentModel sentModel = eventTrainer.train(eventStream);

    return new SceneDetectorModel(languageCode, sentModel, manifestInfoEntries,
        sdFactory);
        
    }

}
