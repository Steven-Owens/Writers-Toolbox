/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public abstract class BasePronounPredicate implements PronounPredicate {
    
    protected Parse PronounToken;
    
    @Override
    public void setPronoun(Parse PronounToken) {
        this.PronounToken = PronounToken;
    }
}
