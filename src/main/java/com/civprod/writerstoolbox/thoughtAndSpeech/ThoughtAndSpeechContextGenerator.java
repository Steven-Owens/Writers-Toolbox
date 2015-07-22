/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import opennlp.tools.util.BeamSearchContextGenerator;

/**
 *
 * @author Steven Owens
 */
public interface ThoughtAndSpeechContextGenerator extends BeamSearchContextGenerator<String> {
  
  /** Returns the context for the specified position in the specified sequence (list).
     * @param index The index of the sequence.
     * @param sequence  The sequence of items over which the beam search is performed.
     * @param priorDecisions The sequence of decisions made prior to the context for which this decision is being made.
     * @param additionalContext Any addition context specific to a class implementing this interface.
     * @return the context for the specified position in the specified sequence.
     */
  public String[] getContext(int index, String[] sequence, String[] priorDecisions, Object[] additionalContext);
}
