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
public enum CommonWordModes implements WordHandling{
    rawOnSpace(false,false,false,false), NaturalLanguageIncludingPunctuation(true,false,false,true), NaturalLanguageExcludingPunctuation(true,false,true,true), 
    rawOnSpaceStemmed(false,true,false,false), NaturalLanguageIncludingPunctuationStemmed(true,true,false,true), NaturalLanguageExcludingPunctuationStemmed(true,true,true,true);
    
    private final boolean useNaturalLanguageStemmer;
    private final boolean stemWords;
    private final boolean dropPunctuation;
    private final boolean maskNumbers;
    
    CommonWordModes(boolean useNaturalLanguageStemmer,boolean stemWords,boolean dropPunctuation, boolean maskNumbers){
        this.useNaturalLanguageStemmer = useNaturalLanguageStemmer;
        this.stemWords = stemWords;
        this.dropPunctuation = dropPunctuation;
        this.maskNumbers = maskNumbers;
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
        return true;
    }
}
