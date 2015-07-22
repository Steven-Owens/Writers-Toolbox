/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import java.io.IOException;
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
public class SceneDetectorEventStream extends AbstractEventStream<SceneSample> {
    
    private SceneContextGenerator cg;

    public SceneDetectorEventStream(ObjectStream<SceneSample> samples, SceneContextGenerator sceneContextGenerator) {
        super(samples);
        
        this.cg = sceneContextGenerator;
    }

    @Override
    protected Iterator<Event> createEvents(SceneSample sample) {
        Collection<Event> events = new ArrayList<Event>();
        String document = sample.getDocument();
        String paragraphs[] = document.split("\n");
        
        for (Span sentenceSpan : sample.getScenes()) {
            for (int index = sentenceSpan.getStart(); index < sentenceSpan.getEnd(); index++){
                events.add(new Event(SceneDetectorME.NO_SPLIT, cg.getContext(paragraphs,
            sentenceSpan.getStart() + index)));
            }
            events.add(new Event(SceneDetectorME.SPLIT, cg.getContext(paragraphs,
            sentenceSpan.getEnd())));
        }
        
        return events.iterator();
    }
    
}
