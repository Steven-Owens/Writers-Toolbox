/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

/**
 *
 * @author Steven Owens
 */
public class ImmutableWordHandling implements WordHandling {
    private boolean useNaturalLanguageStemmer;
    private boolean stemWords;
    private boolean dropPunctuation;
    private boolean maskNumbers;
    private boolean normalizeApostrophes;
    
    public ImmutableWordHandling(WordHandling inToCopyFrom){
        useNaturalLanguageStemmer = inToCopyFrom.useNaturalLanguageTokenizer();
        stemWords = inToCopyFrom.stemWords();
        dropPunctuation = inToCopyFrom.dropPunctuation();
        maskNumbers = inToCopyFrom.maskNumbers();
        normalizeApostrophes = inToCopyFrom.normalizeApostrophes();
    }
    
    /**
     * @return the useNaturalLanguageStemmer
     */
    public boolean useNaturalLanguageTokenizer() {
        return useNaturalLanguageStemmer;
    }

    /**
     * @return the stemWords
     */
    public boolean stemWords() {
        return stemWords;
    }

    /**
     * @return the dropPunctuation
     */
    public boolean dropPunctuation() {
        return dropPunctuation;
    }

    /**
     * @return the maskNumbers
     */
    public boolean maskNumbers() {
        return maskNumbers;
    }
    
    public boolean isMutable(){
        return false;
    }

    @Override
    public boolean normalizeApostrophes() {
        return normalizeApostrophes;
    }
    
}
