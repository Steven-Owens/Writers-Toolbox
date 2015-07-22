/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class StringTokenizerWrapper implements com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer, Wrapper<Tokenizer> {
    
    private final Tokenizer mTokenizer;

    public StringTokenizerWrapper(Tokenizer wrappedTokenizer){
        mTokenizer = wrappedTokenizer;
    }

    @Override
    public List<String> tokenize(String Text) {
        return java.util.Arrays.asList(mTokenizer.tokenize(Text));
    }

    /**
     * @return the mTokenizer
     */
    public Tokenizer getTokenizer() {
        return mTokenizer;
    }

    @Override
    public Tokenizer getInterObject() {
        return mTokenizer;
    }
    
}
