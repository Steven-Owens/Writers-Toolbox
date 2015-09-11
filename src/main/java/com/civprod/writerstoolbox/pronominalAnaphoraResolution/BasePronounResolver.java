/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public abstract class BasePronounResolver implements PronounResolver {
    
    protected final PronounPredicate mPronounPredicate;
    
    public BasePronounResolver(PronounPredicate inPronounPredicate){
        mPronounPredicate = inPronounPredicate;
    }
    
    public static List<Parse> getTokenParses(Parse curParse) {
        Parse[] children = curParse.getChildren();
        String type = curParse.getType();
        if ("TK".equalsIgnoreCase(type) || (children.length == 0)){
            return java.util.Collections.singletonList(curParse);
        } else {
            List<Parse> curTokenParses = new ArrayList(children.length);
            for (Parse curChildParse : children){
                curTokenParses.addAll(getTokenParses(curChildParse));
            }
            return curTokenParses;
        }
    }
    
    public static void addAllNonPronounNPNodes(Set<Entity> forwardLookingCenters, Parse curRootParse) {
        if (curRootParse.getLabel().equalsIgnoreCase("NP")){
            forwardLookingCenters.add(new ParseEntity(curRootParse));
        }
        Parse[] children = curRootParse.getChildren();
        for (Parse curChild : children){
            addAllNonPronounNPNodes(forwardLookingCenters,curChild);
        }
    }
}
