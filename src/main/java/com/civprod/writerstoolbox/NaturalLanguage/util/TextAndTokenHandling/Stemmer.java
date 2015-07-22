/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

/**
 *
 * @author Steven Owens
 */
public interface Stemmer extends opennlp.tools.stemmer.Stemmer, TextTransformer {
    
    public default String transform(String inText){
        return stem(inText);
    }

    /**
     * Add a character to the word being stemmed.  When you are finished
     * adding characters, you can call stem(void) to stem the word.
     */
    void add(char ch);

    /** Adds wLen characters to the word being stemmed contained in a portion
     * of a char[] array. This is like repeated calls of add(char ch), but
     * faster.
     */
    void add(char[] w, int wLen);

    /** Clears the buffer, essentially re-starting the PorterStemmer instance.
     * Good for when you want to just stem one word at a time
     */
    void clear();

    /**
     * Returns a reference to a character buffer containing the results of
     * the stemming process.  You also need to consult getResultLength()
     * to determine the length of the result.
     */
    char[] getResultBuffer();

    /**
     * Returns the length of the word resulting from the stemming process.
     */
    int getResultLength();

    /** Stem the word placed into the PorterStemmer buffer through calls to
     * add(). Returns true if the stemming process resulted in a word different
     * from the input.  You can retrieve the result with
     * getResultLength()/getResultBuffer() or toString().
     */
    void stem();

    /** Function to just stem one word. */
    String stem(String word);

    /**
     * method of OpenNLP stemmer
     * @author Steven Owens
     * @param cs
     * @return
     */
    CharSequence stem(CharSequence cs);

    /**
     * After a word has been stemmed, it can be retrieved by toString(),
     * or a reference to the internal buffer can be retrieved by getResultBuffer
     * and getResultLength (which is generally more efficient.)
     */
    String toString();
    
}
