/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect.SceneBreak;

import com.civprod.writerstoolbox.SceneDect.SceneDetector;
import java.util.List;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class SceneBreakSceneDetector implements SceneDetector {
    private SceneBreakDetector mSceneBreakDetector;
    
    public SceneBreakSceneDetector(SceneBreakDetector  inSceneBreakDetector){
        mSceneBreakDetector = inSceneBreakDetector;
    }

    @Override
    public Span[] detectScenes(String[] inParagraphs) {
        List<Span> spans = detectScenes(java.util.Arrays.asList(inParagraphs));
        return spans.toArray(new Span[spans.size()]);
    }

    @Override
    public List<Span> detectScenes(List<String> inParagraphs) {
        int startOfScene = 0;
        List<Span> rSpanList = new java.util.ArrayList<>(1);
        for (int i = 0; i <inParagraphs.size(); i++){
            String curParagraph = inParagraphs.get(i);
            if (mSceneBreakDetector.isSceneBreak(curParagraph)){
                rSpanList.add(new Span(startOfScene,i));
                startOfScene = i+1;
            }
        }
        rSpanList.add(new Span(startOfScene,inParagraphs.size()));
        return rSpanList;
    }
}
