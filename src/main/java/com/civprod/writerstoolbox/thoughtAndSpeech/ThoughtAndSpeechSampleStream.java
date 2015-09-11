/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.RegexTokenFilter;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TokenStripperToTokenFilter;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.FilterProcessingUnit;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.PipelineStart;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.TextTransformerProcessingUnit;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.TokenizerProcessingUnit;
import com.civprod.writerstoolbox.OpenNLP.StringTokenizerWrapper;
import com.civprod.writerstoolbox.data.html.HTMLTagStripper;
import com.civprod.writerstoolbox.data.html.HTMLTextTransformer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.sentdetect.EmptyLinePreprocessorStream;
import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechSampleStream extends FilterObjectStream<String, ThoughtAndSpeechSample> {

    private PipelineStart mPipeline;

    public static ThoughtAndSpeechSampleStream createDefaultHTMLVersion(ObjectStream<String> sentences) {
        PipelineStart newPipeline = PipelineStart.createPipeline(new TokenizerProcessingUnit(new RegexStringTokenizer()),
                new TextTransformerProcessingUnit(HTMLTextTransformer.instance),
                new FilterProcessingUnit(new RegexTokenFilter(CommonRegexPatterns.XMLTagRegex).negate()),
                new TokenizerProcessingUnit(new StringTokenizerWrapper(opennlp.tools.tokenize.WhitespaceTokenizer.INSTANCE)));
        return new ThoughtAndSpeechSampleStream(sentences, newPipeline);
    }

    public ThoughtAndSpeechSampleStream(ObjectStream<String> sentences, PipelineStart inPipeline) {
        super(new EmptyLinePreprocessorStream(sentences));
        mPipeline = inPipeline;
    }

    public ThoughtAndSpeechSampleStream(ObjectStream<String> sentences) {
        this(sentences, PipelineStart.createPipeline(new TokenizerProcessingUnit(new StringTokenizerWrapper(opennlp.tools.tokenize.WhitespaceTokenizer.INSTANCE))));
    }

    @Override
    public ThoughtAndSpeechSample read() throws IOException {
        String paragraph = samples.read();
        if (paragraph != null) {
            List<String> tokens = new java.util.ArrayList<>(java.util.Arrays.asList(opennlp.tools.tokenize.WhitespaceTokenizer.INSTANCE.tokenize(paragraph)));
            List<Span> spans = new ArrayList<>(1);
            int i = 0;
            int begin = -1;
            String type = "";
            while (i < tokens.size()) {
                String curToken = tokens.get(i);
                if (curToken.contains("<START:")) {
                    begin = i;
                    type = curToken.substring(curToken.indexOf(":")).replaceAll("[:>]", "");
                    tokens.remove(curToken);
                } else if ("<END>".equalsIgnoreCase(curToken)) {
                    int end = i;
                    if (begin < 0) {
                        int debug = 1 + 5;
                    }
                    spans.add(new Span(begin, end-1, type));
                    begin = -1;
                    tokens.remove(curToken);
                } else {
                    i++;
                }
            }
            return new ThoughtAndSpeechSample(tokens, spans);
        } else {
            return null;
        }
    }
}
