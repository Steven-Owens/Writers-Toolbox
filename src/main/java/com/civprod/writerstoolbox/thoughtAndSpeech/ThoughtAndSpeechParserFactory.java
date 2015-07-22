/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.thoughtAndSpeech.lang.en.DefaultThoughtAndSpeechContextGenerator;
import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import opennlp.tools.util.BaseToolFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.SequenceValidator;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechParserFactory extends BaseToolFactory {
    
    private String languageCode;
    
    public ThoughtAndSpeechParserFactory(){
        
    }
    
    public ThoughtAndSpeechParserFactory(String langCode){
        languageCode = langCode;
    }
    
    @Override
  public void validateArtifactMap() throws InvalidFormatException {
    // no additional artifacts
  }

  public SequenceValidator<String> getSequenceValidator() {
    return new DefaultThoughtAndSpeechParserSequenceValidator();
  }

  public ThoughtAndSpeechContextGenerator getContextGenerator() {
      if ("en".equalsIgnoreCase(languageCode)){
          return new com.civprod.writerstoolbox.thoughtAndSpeech.lang.en.DefaultThoughtAndSpeechContextGenerator();
      }
    return new DefaultThoughtAndSpeechContextGenerator();
  }
}
