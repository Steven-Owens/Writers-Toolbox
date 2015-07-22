/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafeSentenceDetector implements SentenceDetector, Wrapper<SentenceDetectorME>, ThreadSafe {
    private final SentenceModel mSentenceModel;

    public ThreadSafeSentenceDetector(SentenceModel inSentenceModel) {
        mSentenceModel = inSentenceModel;
    }
    public final ThreadLocal<SentenceDetectorME> localSentenceDetector = new ThreadLocal<SentenceDetectorME>() {
        protected SentenceDetectorME initialValue() {
            return new SentenceDetectorME(mSentenceModel);
        }
    };

    @Override
    public String[] sentDetect(String string) {
        return localSentenceDetector.get().sentDetect(string);
    }

    @Override
    public Span[] sentPosDetect(String string) {
        return localSentenceDetector.get().sentPosDetect(string);
    }

    @Override
    public SentenceDetectorME getInterObject() {
        return localSentenceDetector.get();
    }
    
}
