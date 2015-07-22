/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.postag.POSTagger;

/**
 *
 * @author Steven Owens
 */
public class POSTaggerWrapper implements com.civprod.writerstoolbox.NaturalLanguage.util.POSTagger, Wrapper<opennlp.tools.postag.POSTagger> {
    private opennlp.tools.postag.POSTagger mPOSTagger;
    
    public POSTaggerWrapper(opennlp.tools.postag.POSTagger inPOSTagger){
        mPOSTagger = inPOSTagger;
    }

    @Override
    public List<String> tag(List<String> sentence) {
        return java.util.Arrays.asList(mPOSTagger.tag(sentence.toArray(new String[sentence.size()])));
    }

    /**
     * @return the mPOSTagger
     */
    public opennlp.tools.postag.POSTagger getPOSTagger() {
        return mPOSTagger;
    }

    @Override
    public POSTagger getInterObject() {
        return mPOSTagger;
    }
}
