/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafePorterStemmer implements Stemmer, ThreadSafe {
    public static final ThreadSafePorterStemmer instance = new ThreadSafePorterStemmer();
    
    private ThreadLocal<PorterStemmer> localPorterStemmer = new ThreadLocal<PorterStemmer>() {
        protected PorterStemmer initialValue() {
            return new PorterStemmer();
        }
    };

    @Override
    public void add(char ch) {
        localPorterStemmer.get().add(ch);
    }

    @Override
    public void add(char[] w, int wLen) {
        localPorterStemmer.get().add(w, wLen);
    }

    @Override
    public void clear() {
        localPorterStemmer.get().clear();
    }

    @Override
    public char[] getResultBuffer() {
        return localPorterStemmer.get().getResultBuffer();
    }

    @Override
    public int getResultLength() {
        return localPorterStemmer.get().getResultLength();
    }

    @Override
    public void stem() {
        localPorterStemmer.get().stem();
    }

    @Override
    public String stem(String word) {
        return localPorterStemmer.get().stem(word);
    }

    @Override
    public CharSequence stem(CharSequence cs) {
        return localPorterStemmer.get().stem(cs);
    }
}
