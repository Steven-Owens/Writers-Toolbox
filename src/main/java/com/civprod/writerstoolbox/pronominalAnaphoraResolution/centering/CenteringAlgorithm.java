/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.centering;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.BasePronounResolver;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
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
    
    private final Comparator<Entity> mRankingComparator;
    private final CenteringRuleSet mCenteringRuleSet;
    
    public CenteringAlgorithm(PronounPredicate inPronounPredicate, Comparator<Entity> inRankingComparator, CenteringRuleSet inCenteringRuleSet){
        super(inPronounPredicate);
        mRankingComparator = inRankingComparator;
        mCenteringRuleSet = inCenteringRuleSet;
    }

    @Override
    public String[][] labelPronouns(String[][] document, Parse[] parses) {
        mCenteringRuleSet.setRankingComparator(mRankingComparator);
        mCenteringRuleSet.setPronounPredicate(mPronounPredicate);
        String[][] labels = new String[document.length][];
        SortedSet<Entity> forwardLookingCenters = new java.util.TreeSet<>(mRankingComparator);
        Entity center = null;
        for (int i = 0; i < document.length; i++) {
            String[] curSentence = document[i];
            Parse curRootParse = parses[i];
            
            //cashe forwardLookingCenters and center
            SortedSet<Entity> forwardLookingCentersMinusOne = forwardLookingCenters;
            Entity centerMinusOne = center;
            CenterLabel rCenterLabel = mCenteringRuleSet.getAssignment(curSentence,curRootParse,forwardLookingCentersMinusOne,centerMinusOne);
            
            forwardLookingCenters = new java.util.TreeSet<>(mRankingComparator);
            //add all directly mentioned entities
            addAllNonPronounNPNodes(forwardLookingCenters,curRootParse);
            forwardLookingCenters.addAll(rCenterLabel.getPronounAssignmentsSet());
            center = rCenterLabel.getCenter();
            //assign labels
            labels[i] = rCenterLabel.getSentenceLabels();
        }
        return labels;
    }
}
