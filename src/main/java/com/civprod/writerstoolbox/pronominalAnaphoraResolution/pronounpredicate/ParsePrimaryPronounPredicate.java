/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;

/**
 *
 * @author Steven Owens
 */
public abstract class ParsePrimaryPronounPredicate extends BasePronounPredicate {
    @Override
    public boolean test(Entity t){
        if (t instanceof ParseEntity){
            return test(((ParseEntity)t).getInterParse());
        } else {
            return true;
        }
    }
}
