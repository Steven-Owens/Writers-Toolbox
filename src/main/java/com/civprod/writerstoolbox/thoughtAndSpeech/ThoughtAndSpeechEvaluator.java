/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import java.util.List;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.eval.Evaluator;
import opennlp.tools.util.eval.FMeasure;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechEvaluator extends Evaluator<ThoughtAndSpeechSample> {

    private FMeasure fmeasure = new FMeasure();

    private ThoughtAndSpeechParserME thoughtAndSpeechParser;

    public ThoughtAndSpeechEvaluator(ThoughtAndSpeechParserME testDetector) {
        thoughtAndSpeechParser = testDetector;
    }

    public FMeasure getFMeasure() {
        return fmeasure;
    }

    @Override
    protected ThoughtAndSpeechSample processSample(ThoughtAndSpeechSample t) {
        Span[] evalSpans = thoughtAndSpeechParser.getEvalSpans(t.getDocument()); //guesses
        Span[] thoughtsOrSpeech = t.getThoughtsOrSpeech(); //gold
        fmeasure.updateScores(thoughtsOrSpeech, evalSpans);
        return new ThoughtAndSpeechSample(t.getDocument(), evalSpans);
    }

}
