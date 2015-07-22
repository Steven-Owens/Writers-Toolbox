/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.tokenize;

import com.civprod.util.TimeComplexity;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.tokenize.TokenContextGenerator;
import opennlp.tools.tokenize.TokenizerFactory;
import opennlp.tools.util.InvalidFormatException;

/**
 *
 * @author Steven Owens
 */
public class WordSplittingTokenizerFactory extends TokenizerFactory {
    
    private Dictionary wordsDictionary;
    private TimeComplexity maxTimeComplexity;
    
    private static final String WORDS_ENTRY_NAME = "words.dictionary";
    private static final String MAX_TIME_COMPLEXITY_NAME = "max.time.complexity";
    /**
   * Creates a {@link TokenizerFactory} that provides the default implementation
   * of the resources.
   */
  public WordSplittingTokenizerFactory() {
      wordsDictionary = null;
      maxTimeComplexity = null;
  }
  
  /**
   * Creates a {@link TokenizerFactory}. Use this constructor to
   * programmatically create a factory.
   * 
   * @param languageCode
   *          the language of the natural text
   * @param abbreviationDictionary
   *          an abbreviations dictionary
   * @param useAlphaNumericOptimization
   *          if true alpha numerics are skipped
   * @param alphaNumericPattern
   *          null or a custom alphanumeric pattern (default is:
   *          "^[A-Za-z0-9]+$", provided by {@link Factory#DEFAULT_ALPHANUMERIC}
   */
  public WordSplittingTokenizerFactory(String languageCode,
      Dictionary abbreviationDictionary, boolean useAlphaNumericOptimization,
      Pattern alphaNumericPattern, Dictionary wordDictionary, TimeComplexity maxTimeComplexity) {
    super(languageCode, abbreviationDictionary,
        useAlphaNumericOptimization, alphaNumericPattern);
    this.wordsDictionary = wordDictionary;
    this.maxTimeComplexity = maxTimeComplexity;
  }
  
  @Override
  public void validateArtifactMap() throws InvalidFormatException {
      super.validateArtifactMap();
      
      if (this.artifactProvider
        .getManifestProperty(MAX_TIME_COMPLEXITY_NAME) == null){
          throw new InvalidFormatException(MAX_TIME_COMPLEXITY_NAME
          + " is a mandatory property!");
      }
      
      Object wordsEntry = this.artifactProvider
        .getArtifact(WORDS_ENTRY_NAME);

    if (wordsEntry != null && !(wordsEntry instanceof Dictionary)) {
      throw new InvalidFormatException("words dictionary '" + wordsEntry +
              "' has wrong type, needs to be of type Dictionary!");
    }
  }
  
  @Override
  public Map<String, Object> createArtifactMap() {
    Map<String, Object> artifactMap = super.createArtifactMap();

    // words are optional
    if (wordsDictionary != null){
      artifactMap.put(WORDS_ENTRY_NAME, wordsDictionary);
    }

    return artifactMap;
  }
  
  @Override
  public Map<String, String> createManifestEntries() {
    Map<String, String> manifestEntries = super.createManifestEntries();

    manifestEntries.put(MAX_TIME_COMPLEXITY_NAME,
        this.getMaxTimeComplexity().toString());

    return manifestEntries;
  }
  
  /**
   * Gets the context generator
   */
    @Override
  public TokenContextGenerator getContextGenerator() {
    TokenContextGenerator superTokenContextGenerator = super.getContextGenerator();
    Dictionary localWordDictionary = this.getWordsDictionary();
    Set<String> wordList;
    if (localWordDictionary != null){
        wordList = localWordDictionary.asStringSet();
    } else {
        wordList = java.util.Collections.EMPTY_SET;
    }
    return new WordSplittingContext(superTokenContextGenerator, wordList, this.getMaxTimeComplexity());
  }

    /**
     * @return the wordsDictionary
     */
    public Dictionary getWordsDictionary() {
        if (this.wordsDictionary == null && artifactProvider != null) {
      this.wordsDictionary = artifactProvider
          .getArtifact(WORDS_ENTRY_NAME);
    }
        return wordsDictionary;
    }

    /**
     * @return the maxTimeComplexity
     */
    public TimeComplexity getMaxTimeComplexity() {
        if (this.maxTimeComplexity == null && artifactProvider != null) {
      this.maxTimeComplexity = TimeComplexity.valueOf(artifactProvider
          .getManifestProperty(MAX_TIME_COMPLEXITY_NAME));
    }
        return maxTimeComplexity;
    }
}
