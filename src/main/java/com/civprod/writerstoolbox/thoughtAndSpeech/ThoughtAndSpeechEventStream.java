/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.SceneDect.SceneContextGenerator;
import com.civprod.writerstoolbox.SceneDect.SceneSample;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import opennlp.tools.ml.model.Event;
import opennlp.tools.util.AbstractEventStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechEventStream extends AbstractEventStream<ThoughtAndSpeechSample> {

    private ThoughtAndSpeechContextGenerator cg;

    public ThoughtAndSpeechEventStream(ObjectStream<ThoughtAndSpeechSample> samples, ThoughtAndSpeechContextGenerator contextGenerator) {
        super(samples);

        this.cg = contextGenerator;
    }

    @Override
    protected Iterator<Event> createEvents(ThoughtAndSpeechSample sample) {
        Collection<Event> events = new ArrayList<Event>();
        String[] document = sample.getDocument();
        String[] priorDecisions = new String[document.length];
        Span[] thoughtsOrSpeech = sample.getThoughtsOrSpeech();
        int curThoughtsOrSpeechIndex = 0;
        Object[] prevLabels = new Object[document.length];
        for (int i = 0; i < document.length; i++) {
            if (curThoughtsOrSpeechIndex < thoughtsOrSpeech.length) {
                Span curSpan = thoughtsOrSpeech[curThoughtsOrSpeechIndex];
                while ((curSpan != null) && (i >= curSpan.getEnd())) {
                    curThoughtsOrSpeechIndex++;
                    if (curThoughtsOrSpeechIndex < thoughtsOrSpeech.length) {
                        curSpan = thoughtsOrSpeech[curThoughtsOrSpeechIndex];
                    } else {
                        curSpan = null;
                    }
                }
            }
            if (curThoughtsOrSpeechIndex >= thoughtsOrSpeech.length) {
                priorDecisions[i] = ThoughtAndSpeechParserME.OTHER;
            } else {
                Span curSpan = thoughtsOrSpeech[curThoughtsOrSpeechIndex];
                if (i < curSpan.getStart()) {
                    priorDecisions[i] = ThoughtAndSpeechParserME.OTHER;
                } else if (i == curSpan.getStart()) {
                    priorDecisions[i] = ThoughtAndSpeechParserME.START + ":" + curSpan.getType();
                } else if (i < curSpan.getEnd()) {
                    priorDecisions[i] = ThoughtAndSpeechParserME.CONTINUE + ":" + curSpan.getType();
                }
            }
            events.add(new Event(priorDecisions[i], cg.getContext(i, document, priorDecisions, prevLabels)));
        }
        return events.iterator();
    }

}
