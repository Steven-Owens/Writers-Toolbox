/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafeTokenizer implements Tokenizer, Wrapper<TokenizerME>, ThreadSafe {
    private final TokenizerModel mTokenizerModel;

    public ThreadSafeTokenizer(TokenizerModel inTokenizerModel) {
        mTokenizerModel = inTokenizerModel;
    }
    public final ThreadLocal<TokenizerME> localTokenizer = new ThreadLocal<TokenizerME>() {
        protected TokenizerME initialValue() {
            return new TokenizerME(mTokenizerModel);
        }
    };

    @Override
    public String[] tokenize(String string) {
        return localTokenizer.get().tokenize(string);
    }

    @Override
    public Span[] tokenizePos(String string) {
        return localTokenizer.get().tokenizePos(string);
    }

    @Override
    public TokenizerME getInterObject() {
        return localTokenizer.get();
    }
    
}
