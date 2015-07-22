/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafeNameFinder implements TokenNameFinder, Wrapper<NameFinderME>, ThreadSafe {
    private final TokenNameFinderModel mTokenNameFinderModel;
    
    ThreadSafeNameFinder(TokenNameFinderModel inTokenNameFinderModel){
        mTokenNameFinderModel = inTokenNameFinderModel;
    }
    
    private final ThreadLocal<NameFinderME> localNameFinderME = new ThreadLocal<NameFinderME>(){
        protected NameFinderME initialValue() {
            return new NameFinderME(mTokenNameFinderModel);
        }
    };

    @Override
    public Span[] find(String[] strings) {
        return localNameFinderME.get().find(strings);
    }

    @Override
    public void clearAdaptiveData() {
        localNameFinderME.get().clearAdaptiveData();
    }

    @Override
    public NameFinderME getInterObject() {
        return localNameFinderME.get();
    }
}
