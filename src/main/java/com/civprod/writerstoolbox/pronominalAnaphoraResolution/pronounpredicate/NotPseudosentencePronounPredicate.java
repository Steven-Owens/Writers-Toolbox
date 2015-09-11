/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class NotPseudosentencePronounPredicate extends ParsePrimaryPronounPredicate {

    @Override
    public boolean test(Parse t) {
        if (t.getChildCount() == 1){
            if (t.getChildren()[0].getType().equalsIgnoreCase("S")){
                return false;
            }
        }
        return true;
    }
    
}
