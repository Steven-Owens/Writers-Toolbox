/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public abstract class EntityPrimaryPronounPredicate extends BasePronounPredicate {
    @Override
    public boolean test(Parse t) {
        return test(new ParseEntity(t));
    }
}
