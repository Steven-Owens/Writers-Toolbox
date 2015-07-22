/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class CenteringAlgorithm extends BasePronounResolver {
    
    private final Comparator<Parse> mRankingComparator;
    
    public CenteringAlgorithm(Comparator<Parse> inRankingComparator){
        mRankingComparator = inRankingComparator;
    }

    @Override
    public String[][] labelPronouns(String[][] document, Parse[] parses) {
        String[][] labels = new String[document.length][];
        SortedSet<Parse> forwardLookingCenters = new java.util.TreeSet<>(mRankingComparator);
        String center = null;
        for (int i = 0; i < document.length; i++) {
            String[] curSentence = document[i];
            String[] curSentenceLabels = new String[curSentence.length];
            Parse curRootParse = parses[i];
            Parse[] curTokenParses = getTokenParses(curRootParse).toArray(new Parse[0]);
            
            //cashe forwardLookingCenters and center
            SortedSet<Parse> forwardLookingCentersMinusOne = forwardLookingCenters;
            forwardLookingCenters = new java.util.TreeSet<>(mRankingComparator);
            String centerMinusOne = center;
            //add all directly mentioned entities
            addAllNPNodes(forwardLookingCenters,curRootParse);
            SortedSet<Parse> possibleforwardLookingCenters = new java.util.TreeSet<>(forwardLookingCenters);
            Map<Parse,Set<Parse>> possibleMatchesMap = new HashMap<>(0);
            for (int j = 0; j < curSentence.length; j++) {
                Parse curTokenParse = curTokenParses[j];
                Parse curPOSParse = curTokenParse.getParent();
                if (curPOSParse.getType().equalsIgnoreCase("PRP") || curPOSParse.getType().equalsIgnoreCase("PRP$")) {
                    String curToken = curSentence[j];
                    Set<Parse> possibleMatches = new HashSet<>();
                    for (Parse curPossibleMatch : forwardLookingCentersMinusOne){
                        if (consistent(curTokenParse,curPossibleMatch)){
                            possibleMatches.add(curPossibleMatch);
                            possibleforwardLookingCenters.add(curPossibleMatch);
                        }
                    }
                    for (Parse curPossibleMatch : forwardLookingCenters){
                        if (consistent(curTokenParse,curPossibleMatch)){
                            possibleMatches.add(curPossibleMatch);
                            possibleforwardLookingCenters.add(curPossibleMatch);
                        }
                    }
                    possibleMatchesMap.put(curTokenParse, possibleMatches);
                } 
            }
            //select Center
            for (int j = 0; j < curSentence.length; j++) {
                Parse curTokenParse = curTokenParses[j];
                Parse curPOSParse = curTokenParse.getParent();
                if (curPOSParse.getType().equalsIgnoreCase("PRP") || curPOSParse.getType().equalsIgnoreCase("PRP$")) {
                    String curToken = curSentence[j];
                    Set<Parse> possibleMatches = possibleMatchesMap.get(curTokenParse);
                    //assign labels
                } else {
                    curSentenceLabels[j] = OTHER;
                }
            }
            labels[i] = curSentenceLabels;
        }
        return labels;
    }

    private void addAllNPNodes(SortedSet<Parse> forwardLookingCenters, Parse curRootParse) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean consistent(Parse curTokenParse, Parse curPossibleMatch) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
