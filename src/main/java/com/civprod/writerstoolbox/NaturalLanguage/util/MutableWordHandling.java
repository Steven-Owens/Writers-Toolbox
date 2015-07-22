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
public class MutableWordHandling implements WordHandling {
    private boolean useNaturalLanguageTokenizer;
    private boolean stemWords;
    private boolean dropPunctuation;
    private boolean maskNumbers;
    private boolean normalizeApostrophes;
    
    public MutableWordHandling(){
        this(CommonWordModes.NaturalLanguageIncludingPunctuation);
    }
    
    public MutableWordHandling(WordHandling inToCopyFrom){
        useNaturalLanguageTokenizer = inToCopyFrom.useNaturalLanguageTokenizer();
        stemWords = inToCopyFrom.stemWords();
        dropPunctuation = inToCopyFrom.dropPunctuation();
        maskNumbers = inToCopyFrom.maskNumbers();
        normalizeApostrophes = inToCopyFrom.normalizeApostrophes();
    }
    
    /**
     * @return the useNaturalLanguageStemmer
     */
    public boolean useNaturalLanguageTokenizer() {
        return useNaturalLanguageTokenizer;
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

    /**
     * @param useNaturalLanguageTokenizer the useNaturalLanguageStemmer to set
     */
    public void setUseNaturalLanguageTokenizer(boolean useNaturalLanguageTokenizer) {
        this.useNaturalLanguageTokenizer = useNaturalLanguageTokenizer;
    }

    /**
     * @param stemWords the stemWords to set
     */
    public void setStemWords(boolean stemWords) {
        this.stemWords = stemWords;
    }

    /**
     * @param dropPunctuation the dropPunctuation to set
     */
    public void setDropPunctuation(boolean dropPunctuation) {
        this.dropPunctuation = dropPunctuation;
    }

    /**
     * @param maskNumbers the maskNumbers to set
     */
    public void setMaskNumbers(boolean maskNumbers) {
        this.maskNumbers = maskNumbers;
    }
    
    public boolean isMutable(){
        return true;
    }

    @Override
    public boolean normalizeApostrophes() {
        return normalizeApostrophes;
    }

    /**
     * @param normalizeApostrophes the normalizeApostrophes to set
     */
    public void setNormalizeApostrophes(boolean normalizeApostrophes) {
        this.normalizeApostrophes = normalizeApostrophes;
    }
}
