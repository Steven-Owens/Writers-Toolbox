/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech.lang.en;

import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import com.civprod.writerstoolbox.thoughtAndSpeech.ThoughtAndSpeechContextGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Steven Owens
 */
public class DefaultThoughtAndSpeechContextGenerator extends com.civprod.writerstoolbox.thoughtAndSpeech.DefaultThoughtAndSpeechContextGenerator {
    
    public final String ITALICS_BEGIN_TAG = "<i>";
    public final String ITALICS_END_TAG = "</i>";
    public final String DOUBLE_QOUTE = "\"";
    public final String SINGLE_QOUTE = "\"";
    public final String ITALICS_LABEL = "italics";
    public final String ITALICS_TAG_LABEL = "italic tag";
    public final String DOUBLE_QOUTE_STRING_LABEL = "double qoute string";
    public final String DOUBLE_QOUTE_STRING_BEGIN_LABEL = DOUBLE_QOUTE_STRING_LABEL + " begin";
    public final String DOUBLE_QOUTE_STRING_END_LABEL = DOUBLE_QOUTE_STRING_LABEL + " end";
    public final String SINGLE_QOUTE_STRING_LABEL = "single qoute string";
    public final String SINGLE_QOUTE_STRING_BEGIN_LABEL = SINGLE_QOUTE_STRING_LABEL + " begin";
    public final String SINGLE_QOUTE_STRING_END_LABEL = SINGLE_QOUTE_STRING_LABEL + " end";
    public final String JUST_ENDDED = " just endded";

    public DefaultThoughtAndSpeechContextGenerator(Set<String> saidWords, Set<String> thoughtWords) {
        super(saidWords, thoughtWords);
    }

    @Override
    public Set<String> getContextAsList(int index, String[] sequence, String[] priorDecisions, Object[] additionalContext) {
        Set<String> labels = super.getContextAsList(index, sequence, priorDecisions, additionalContext);
        String curToken = sequence[index];
        if (index > 0) {
            String prevToken = sequence[index - 1];
            Collection<String> prevLabels = (Collection<String>) additionalContext[index - 1];
            if (prevToken.equalsIgnoreCase(ITALICS_END_TAG)) {
                labels.add(ITALICS_LABEL + JUST_ENDDED);
            } else if (prevLabels.contains(ITALICS_LABEL)) {
                labels.add(ITALICS_LABEL);
            }
            if (prevLabels.contains(DOUBLE_QOUTE_STRING_LABEL) && (!prevToken.equalsIgnoreCase(DOUBLE_QOUTE))) {
                labels.add(DOUBLE_QOUTE_STRING_LABEL);
                if (curToken.equalsIgnoreCase(DOUBLE_QOUTE)) {
                    labels.add(DOUBLE_QOUTE_STRING_END_LABEL);
                }
            } else {
                if (curToken.equalsIgnoreCase(DOUBLE_QOUTE)) {
                    labels.add(DOUBLE_QOUTE_STRING_BEGIN_LABEL);
                }
            }
            if (prevLabels.contains(DOUBLE_QOUTE_STRING_END_LABEL)){
                labels.add(DOUBLE_QOUTE_STRING_LABEL + JUST_ENDDED);
            }
            if (prevLabels.contains(SINGLE_QOUTE_STRING_LABEL) && (!prevToken.equalsIgnoreCase(SINGLE_QOUTE))) {
                labels.add(SINGLE_QOUTE_STRING_LABEL);
                if (curToken.equalsIgnoreCase(SINGLE_QOUTE)) {
                    labels.add(SINGLE_QOUTE_STRING_END_LABEL);
                }
            } else {
                if (curToken.equalsIgnoreCase(SINGLE_QOUTE)) {
                    labels.add(SINGLE_QOUTE_STRING_BEGIN_LABEL);
                }
            }
            if (prevLabels.contains(SINGLE_QOUTE_STRING_END_LABEL)){
                labels.add(SINGLE_QOUTE_STRING_LABEL + JUST_ENDDED);
            }
        }
        if (curToken.equalsIgnoreCase(SINGLE_QOUTE)) {
            labels.add(SINGLE_QOUTE_STRING_LABEL);
            labels.add("single qoute");
        }
        if (curToken.equalsIgnoreCase(DOUBLE_QOUTE)) {
            labels.add(DOUBLE_QOUTE_STRING_LABEL);
            labels.add("double qoute");
        }
        if (curToken.equalsIgnoreCase(ITALICS_BEGIN_TAG)) {
            labels.add(ITALICS_LABEL);
            labels.add(ITALICS_TAG_LABEL);
            labels.add("start italics");
        }
        if (curToken.equalsIgnoreCase(ITALICS_END_TAG)) {
            labels.add(ITALICS_LABEL);
            labels.add(ITALICS_TAG_LABEL);
            labels.add("end italics");
        }
        return labels;
    }

}
