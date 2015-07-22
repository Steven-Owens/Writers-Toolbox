/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class BreakIteratorSentenceDetector implements SentenceDetector, opennlp.tools.sentdetect.SentenceDetector, ThreadSafe {

    @Override
    public List<String> sentenceDetect(String text) {
        return java.util.Arrays.asList(sentDetect(text));
    }

    @Override
    public String[] sentDetect(String string) {
        Span[] spans = sentPosDetect(string);
        String[] rSentences = new String[spans.length];
        for (int i = 0; i < spans.length; i++){
            Span curSpan = spans[i];
            rSentences[i] = string.substring(curSpan.getStart(), curSpan.getEnd());
        }
        return rSentences;
    }

    @Override
    public Span[] sentPosDetect(String string) {
        List<Span> sentPosDetectAsList = sentPosDetectAsList(string);
        return sentPosDetectAsList.toArray(new Span[sentPosDetectAsList.size()]);
    }
        
    public List<Span> sentPosDetectAsList(String string) {
        List<Span> rList = new ArrayList<>(1);
        BreakIterator sentenceInstance = java.text.BreakIterator.getSentenceInstance();
        sentenceInstance.setText(string);
        int last = 0;
        while (sentenceInstance.next() != BreakIterator.DONE){
            int current = sentenceInstance.current();
            if (last != current){
                rList.add(new Span(last,current,"sentence"));
            }
            last = current;
        }
        return rList;
    }
    
}
