/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.Sequence;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafePOSTagger implements POSTagger, Wrapper<POSTaggerME>, ThreadSafe {
    private final POSModel mPOSModel;
    
    public ThreadSafePOSTagger(POSModel inPOSModel) {
        mPOSModel = inPOSModel;
    }
    
    public final ThreadLocal<POSTaggerME> localPOSTagger = new ThreadLocal<POSTaggerME>() {
        protected POSTaggerME initialValue() {
            return new POSTaggerME(mPOSModel);
        }
    };

    @Override
    @Deprecated
    public List<String> tag(List<String> list) {
        return localPOSTagger.get().tag(list);
    }

    @Override
    public String[] tag(String[] strings) {
        return localPOSTagger.get().tag(strings);
    }

    @Override
    public String[] tag(String[] strings, Object[] os) {
        return localPOSTagger.get().tag(strings, os);
    }

    @Override
    @Deprecated
    public String tag(String string) {
        return localPOSTagger.get().tag(string);
    }

    @Override
    @Deprecated
    public Sequence[] topKSequences(List<String> list) {
        return localPOSTagger.get().topKSequences(list);
    }

    @Override
    public Sequence[] topKSequences(String[] strings) {
        return localPOSTagger.get().topKSequences(strings);
    }

    @Override
    public Sequence[] topKSequences(String[] strings, Object[] os) {
        return localPOSTagger.get().topKSequences(strings, os);
    }
    
    public POSTaggerME getPOSTaggerME(){
        return localPOSTagger.get();
    }

    @Override
    public POSTaggerME getInterObject() {
        return localPOSTagger.get();
    }
}
