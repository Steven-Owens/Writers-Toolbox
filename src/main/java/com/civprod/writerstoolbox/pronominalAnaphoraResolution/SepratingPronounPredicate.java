/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class SepratingPronounPredicate implements PronounPredicate {
    private Parse PronounToken;

    @Override
    public boolean test(Parse t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPronoun(Parse PronounToken) {
        this.PronounToken = PronounToken;
    }
    
}
