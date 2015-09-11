/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.centering;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
import java.util.Comparator;
import java.util.SortedSet;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public interface CenteringRuleSet {

    public CenterLabel getAssignment(String[] curSentence, Parse curRootParse, SortedSet<Entity> forwardLookingCentersMinusOne, Entity centerMinusOne);

    public void setRankingComparator(Comparator<Entity> mRankingComparator);

    public void setPronounPredicate(PronounPredicate mPronounPredicate);
    
}
