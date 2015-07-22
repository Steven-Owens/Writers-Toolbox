/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import opennlp.tools.util.SequenceValidator;

/**
 *
 * @author Steven Owens
 */
public class DefaultThoughtAndSpeechParserSequenceValidator implements SequenceValidator<String>  {
    
    private boolean validOutcome(String outcome, String prevOutcome) {
    if (outcome.startsWith("I-")) {
      if (prevOutcome == null) {
        return (false);
      }
      else {
        if (prevOutcome.equals("O")) {
          return (false);
        }
        if (!prevOutcome.substring(2).equals(outcome.substring(2))) {
          return (false);
        }
      }
    }
    return true;
  }
    
    protected boolean validOutcome(String outcome, String[] sequence) {
    String prevOutcome = null;
    if (sequence.length > 0) {
      prevOutcome = sequence[sequence.length-1];
    }
    return validOutcome(outcome,prevOutcome);
  }

    @Override
    public boolean validSequence(int i, String[] inputSequence, String[] outcomesSequence,
      String outcome) {
        return validOutcome(outcome, outcomesSequence);
    }
    
}
