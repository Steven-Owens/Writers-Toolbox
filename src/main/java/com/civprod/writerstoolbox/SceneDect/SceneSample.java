/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class SceneSample {
    
    private final String document;

  private final List<Span> scenes;
  
  public SceneSample(String document, Span... scenes) {
    this.document = document;
    this.scenes = Collections.unmodifiableList(new ArrayList<Span>(Arrays.asList(scenes)));
  }

    public Span[] getScenes() {
        return scenes.toArray(new Span[scenes.size()]);
    }
    
    /**
   * Retrieves the document.
   *
   * @return the document
   */
  public String getDocument() {
    return document;
  }
  
  @Override
  public String toString() {
    
    StringBuilder documentBuilder = new StringBuilder();
    
    for (Span sentSpan : scenes) {
      documentBuilder.append(sentSpan.getCoveredText(document));
      documentBuilder.append("\n");
    }
    
    return documentBuilder.toString();
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof SceneSample) {
      SceneSample a = (SceneSample) obj;

      return getDocument().equals(a.getDocument())
          && Arrays.equals(getScenes(), a.getScenes());
    } else {
      return false;
    }
  }
    
}
