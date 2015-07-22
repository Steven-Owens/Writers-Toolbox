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
public interface WordHandling {
    
    /**
     * @return the useNaturalLanguageStemmer
     */
    public boolean useNaturalLanguageTokenizer();
    
    /**
     * @return the stemWords
     */
    public boolean stemWords();

    /**
     * @return the dropPunctuation
     */
    public boolean dropPunctuation();

    /**
     * @return the maskNumbers
     */
    public boolean maskNumbers();
    
    public boolean normalizeApostrophes();
    
    public boolean isMutable();
}
