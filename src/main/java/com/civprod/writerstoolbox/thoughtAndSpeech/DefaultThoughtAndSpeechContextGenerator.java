/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Steven Owens
 */
public class DefaultThoughtAndSpeechContextGenerator implements ThoughtAndSpeechContextGenerator {
    
    protected boolean isSaidWord(String prevToken) {
        return false;
    }

    protected boolean isThoughtWord(String prevToken) {
        return false;
    }
    
    protected Set<String> getContextAsList(int index, String[] sequence, String[] priorDecisions, Object[] additionalContext) {
        Set<String> labels = new HashSet<>(1);
        String prevPrevToken = "bop";
        String prevPrevLabel = "bop";
        String prevToken = "bop";
        String prevLabel = "bop";
        String curToken = sequence[index];
        String nextToken = "eop";
        String nextNextToken = "eop";
        if (index > 0){
            prevToken = sequence[index-1];
            prevLabel = priorDecisions[index-1];
            if (index > 1){
                prevPrevToken = sequence[index-2];
                prevPrevLabel = priorDecisions[index-2];
            }
            /*labels.add("prev label:" + prevLabel);
            labels.add("prev token: " + prevToken);
            if (!prevLabel.equalsIgnoreCase("o")){
                labels.add("prev label trimmed:" + prevLabel.substring(2));
            }*/
        }
        if (index < (sequence.length-1)){
            nextToken = sequence[index+1];
            if (index < (sequence.length-2)){
                nextNextToken = sequence[index+2];
            }
            //labels.add("next token: " + nextToken);
        }
        
        if (isSaidWord(prevToken)) {
                labels.add("prev said");
            }
            if (isThoughtWord(prevToken)) {
                labels.add("prev thought");
            }
            if (isSaidWord(prevPrevToken)) {
                    labels.add("prev prev said");
                }
                if (isThoughtWord(prevPrevToken)) {
                    labels.add("prev prev thought");
                }
                if (isSaidWord(curToken)) {
            labels.add("cur said");
        }
        if (isThoughtWord(curToken)) {
            labels.add("cur thought");
        }
        if (isSaidWord(nextToken)) {
                labels.add("next said");
            }
            if (isThoughtWord(nextToken)) {
                labels.add("next thought");
            }
            if (isSaidWord(nextNextToken)) {
                    labels.add("next next said");
                }
                if (isThoughtWord(nextNextToken)) {
                    labels.add("next next thought");
                }
        
        labels.add("prevPrevToken:"+ prevPrevToken);
        labels.add("prevToken:"+ prevToken);
        labels.add("cur token:" + curToken);
        labels.add("nextToken:"+ nextToken);
        labels.add("nextNextToken:"+ nextNextToken);
        labels.add("prev cur bigram:" + prevToken + ":" + curToken);
        labels.add("cur next bigram:" + curToken + ":" + nextToken);
        labels.add(prevPrevToken);
        labels.add(prevToken);
        labels.add(curToken);
        labels.add(nextToken);
        labels.add(nextNextToken);
        labels.add(prevToken + ":" + curToken);
        labels.add(curToken + ":" + nextToken);
        
        labels.add("prevPrevLabel:"+prevPrevLabel);
        labels.add("prevLabel:"+prevLabel);
        labels.add("prev two Labels:"+prevPrevLabel+":"+prevLabel);
        labels.add(prevPrevLabel);
        labels.add(prevLabel);
        labels.add(prevPrevLabel+":"+prevLabel);
        
        labels.add("prevPrevLabelNormal:"+normalizeLabel(prevPrevLabel));
        labels.add("prevLabelNormal:"+normalizeLabel(prevLabel));
        labels.add("prev two Labels Normal:"+normalizeLabel(prevPrevLabel)+":"+normalizeLabel(prevLabel));
        labels.add(normalizeLabel(prevPrevLabel));
        labels.add(normalizeLabel(prevLabel));
        labels.add(normalizeLabel(prevPrevLabel)+":"+normalizeLabel(prevLabel));
        
        labels.add("prevPrevTokenLabel:"+prevLabel+":"+ prevPrevToken);
        labels.add("prevTokenLabel:"+prevLabel+":"+ prevToken);
        labels.add("cur tokenLabel:"+prevLabel+":"+ curToken);
        labels.add("nextTokenLabel:"+prevLabel+":"+ nextToken);
        labels.add("nextNextTokenLabel:"+prevLabel+":"+ nextNextToken);
        labels.add("prev cur bigramLabel:"+prevLabel+":"+ prevToken + ":" + curToken);
        labels.add("cur next bigramLabel:"+prevLabel+":" + curToken + ":" + nextToken);
        labels.add(prevLabel+":"+ prevPrevToken);
        labels.add(prevLabel+":"+ prevToken);
        labels.add(prevLabel+":"+ curToken);
        labels.add(prevLabel+":"+ nextToken);
        labels.add(prevLabel+":"+ nextNextToken);
        labels.add(prevLabel+":"+ prevToken + ":" + curToken);
        labels.add(prevLabel+":" + curToken + ":" + nextToken);
        
        labels.add("prevPrevTokenLabelNormal:"+normalizeLabel(prevLabel)+":"+ prevPrevToken);
        labels.add("prevTokenLabelNormal:"+normalizeLabel(prevLabel)+":"+ prevToken);
        labels.add("cur tokenLabelNormal:"+normalizeLabel(prevLabel)+":"+ curToken);
        labels.add("nextTokenLabelNormal:"+normalizeLabel(prevLabel)+":"+ nextToken);
        labels.add("nextNextTokenLabelNormal:"+normalizeLabel(prevLabel)+":"+ nextNextToken);
        labels.add("prev cur bigramLabelNormal:"+normalizeLabel(prevLabel)+":"+ prevToken + ":" + curToken);
        labels.add("cur next bigramLabelNormal:"+normalizeLabel(prevLabel)+":" + curToken + ":" + nextToken);
        labels.add(normalizeLabel(prevLabel)+":"+ prevPrevToken);
        labels.add(normalizeLabel(prevLabel)+":"+ prevToken);
        labels.add(normalizeLabel(prevLabel)+":"+ curToken);
        labels.add(normalizeLabel(prevLabel)+":"+ nextToken);
        labels.add(normalizeLabel(prevLabel)+":"+ nextNextToken);
        labels.add(normalizeLabel(prevLabel)+":"+ prevToken + ":" + curToken);
        labels.add(normalizeLabel(prevLabel)+":" + curToken + ":" + nextToken);
        
        return labels;
    }

    protected final String normalizeLabel(String inLabel) {
        if (ThoughtAndSpeechParserME.START.equalsIgnoreCase(inLabel) || ThoughtAndSpeechParserME.CONTINUE.equalsIgnoreCase(inLabel)){
            return ThoughtAndSpeechParserME.PART;
        } else {
            return inLabel;
        }
    }

    @Override
    public String[] getContext(int index, String[] sequence, String[] priorDecisions, Object[] additionalContext) {
        Set<String> labelsList =  getContextAsList(index,sequence,priorDecisions,additionalContext);
        additionalContext[index] = java.util.Collections.unmodifiableSet(new java.util.HashSet<>(labelsList));
       return labelsList.toArray(new String[labelsList.size()]);
    }
    
}
