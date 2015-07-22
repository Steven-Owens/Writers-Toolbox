/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.SceneDect.SceneSample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechSample {
    private final List<String> document;
    private final List<Span> thoughtsOrSpeech;
    
    public ThoughtAndSpeechSample(List<String> document, List<Span> thoughtsOrSpeech) {
        this.document =Collections.unmodifiableList(new ArrayList<String>(document));
        this.thoughtsOrSpeech = Collections.unmodifiableList(new ArrayList<Span>(thoughtsOrSpeech));
    }
    
    public ThoughtAndSpeechSample(String document[], Span... thoughtsOrSpeech) {
        this(Arrays.asList(document),Arrays.asList(thoughtsOrSpeech));
    }
    
    public Span[] getThoughtsOrSpeech() {
        return thoughtsOrSpeech.toArray(new Span[thoughtsOrSpeech.size()]);
    }
    
    /**
   * Retrieves the document.
   *
   * @return the document
   */
  public String[] getDocument() {
    return document.toArray(new String[document.size()]);
  }
  
  @Override
  public String toString() {
      List<String> taggedDocument = new ArrayList<>(document);
      List<Span> revSpans =  new ArrayList<>(thoughtsOrSpeech);
      java.util.Collections.reverse(revSpans);
      for (Span curSpan : revSpans){
          taggedDocument.add(curSpan.getEnd(), "<END>");
          taggedDocument.add(curSpan.getStart(), "<START>");
      }
    return taggedDocument.parallelStream().collect(java.util.stream.Collectors.joining(" "));
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } else if (obj instanceof ThoughtAndSpeechSample) {
      ThoughtAndSpeechSample a = (ThoughtAndSpeechSample) obj;

      return Arrays.equals(getDocument(),a.getDocument())
          && Arrays.equals(getThoughtsOrSpeech(), a.getThoughtsOrSpeech());
    } else {
      return false;
    }
  }
}
