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
public abstract class BaseCenteringRuleSet implements CenteringRuleSet {

    protected Comparator<Entity> mRankingComparator;
    protected PronounPredicate mPronounPredicate;

    @Override
    public void setRankingComparator(Comparator<Entity> inRankingComparator) {
        mRankingComparator = inRankingComparator;
    }

    @Override
    public void setPronounPredicate(PronounPredicate inPronounPredicate) {
        mPronounPredicate = inPronounPredicate;
    }

    /**
     * @return the mRankingComparator
     */
    public Comparator<Entity> getRankingComparator() {
        return mRankingComparator;
    }

    /**
     * @return the mPronounPredicate
     */
    public PronounPredicate getPronounPredicate() {
        return mPronounPredicate;
    }

}
