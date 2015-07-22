/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.sentdetect.SentenceDetector;


/**
 *
 * @author Steven Owens
 */
public class SentenceDetectorWrapper implements com.civprod.writerstoolbox.NaturalLanguage.util.SentenceDetector, Wrapper<opennlp.tools.sentdetect.SentenceDetector> {
    opennlp.tools.sentdetect.SentenceDetector mSentenceDetector;
    
    public SentenceDetectorWrapper(opennlp.tools.sentdetect.SentenceDetector inSentenceDetector){
        mSentenceDetector = inSentenceDetector;
    }

    @Override
    public List<String> sentenceDetect(String text) {
        return java.util.Arrays.asList(mSentenceDetector.sentDetect(text));
    }

    @Override
    public SentenceDetector getInterObject() {
        return mSentenceDetector;
    }
}
