/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import java.util.List;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public interface SceneDetector {
    public Span[] detectScenes(String[] inParagraphs);
    public List<Span> detectScenes(List<String> inParagraphs);
}
