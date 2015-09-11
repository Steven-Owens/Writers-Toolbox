/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.thoughtAndSpeech.lang.en.DefaultThoughtAndSpeechContextGenerator;
import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.util.BaseToolFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.SequenceValidator;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechParserFactory extends BaseToolFactory {

    private String languageCode;
    private Dictionary saidWordsDictionary;
    private Dictionary thoughtWordsDictionary;
    private static final String SAID_WORDS_ENTRY_NAME = "said.words.dictionary";
    private static final String THOUGHT_WORDS_ENTRY_NAME = "thought.words.dictionary";

    public ThoughtAndSpeechParserFactory() {

    }
    
    public ThoughtAndSpeechParserFactory(String langCode) {
        this(langCode,null,null);
    }

    public ThoughtAndSpeechParserFactory(String langCode,Dictionary saidWordsDictionary,Dictionary thoughtWordsDictionary) {
        languageCode = langCode;
        this.saidWordsDictionary = saidWordsDictionary;
        this.thoughtWordsDictionary = thoughtWordsDictionary;
    }

    @Override
    public void validateArtifactMap() throws InvalidFormatException {
        Object saidWordsEntry = this.artifactProvider
        .getArtifact(SAID_WORDS_ENTRY_NAME);

    if (saidWordsEntry != null && !(saidWordsEntry instanceof Dictionary)) {
      throw new InvalidFormatException("said words dictionary '" + saidWordsEntry +
              "' has wrong type, needs to be of type Dictionary!");
    }
    
    Object thoughtWordsEntry = this.artifactProvider
        .getArtifact(THOUGHT_WORDS_ENTRY_NAME);

    if (thoughtWordsEntry != null && !(thoughtWordsEntry instanceof Dictionary)) {
      throw new InvalidFormatException("thought words dictionary '" + thoughtWordsEntry +
              "' has wrong type, needs to be of type Dictionary!");
    }
    }
    
    @Override
  public Map<String, Object> createArtifactMap() {
    Map<String, Object> artifactMap = super.createArtifactMap();

    // Abbreviations are optional
    if (saidWordsDictionary != null)
      artifactMap.put(SAID_WORDS_ENTRY_NAME, saidWordsDictionary);
    
    // Abbreviations are optional
    if (thoughtWordsDictionary != null)
      artifactMap.put(THOUGHT_WORDS_ENTRY_NAME, thoughtWordsDictionary);

    return artifactMap;
  }

    public SequenceValidator<String> getSequenceValidator() {
        return new DefaultThoughtAndSpeechParserSequenceValidator();
    }

    public ThoughtAndSpeechContextGenerator getContextGenerator() {
        Set<String> saidWords = null;
        Dictionary saidWordsDictionary1 = getSaidWordsDictionary();
        if (saidWordsDictionary1 != null){
            saidWords = saidWordsDictionary1.asStringSet();
        } else {
            saidWords = Collections.emptySet();
        }
        Set<String> thoughtWords = null;
        Dictionary thoughtWordsDictionary1 = getThoughtWordsDictionary();
        if (thoughtWordsDictionary1 != null){
            thoughtWords = thoughtWordsDictionary1.asStringSet();
        } else {
            thoughtWords = Collections.emptySet();
        }
        if ("en".equalsIgnoreCase(languageCode)) {
            return new com.civprod.writerstoolbox.thoughtAndSpeech.lang.en.DefaultThoughtAndSpeechContextGenerator(saidWords, thoughtWords);
        }
        return new DefaultThoughtAndSpeechContextGenerator(saidWords, thoughtWords);
    }

    /**
     * @return the saidWordsDictionary
     */
    public Dictionary getSaidWordsDictionary() {
        if (this.saidWordsDictionary == null && artifactProvider != null) {
      this.saidWordsDictionary = artifactProvider
          .getArtifact(SAID_WORDS_ENTRY_NAME);
    }
        return saidWordsDictionary;
    }

    /**
     * @return the thoughtWordsDictionary
     */
    public Dictionary getThoughtWordsDictionary() {
        if (this.thoughtWordsDictionary == null && artifactProvider != null) {
      this.thoughtWordsDictionary = artifactProvider
          .getArtifact(THOUGHT_WORDS_ENTRY_NAME);
    }
        return thoughtWordsDictionary;
    }
}
