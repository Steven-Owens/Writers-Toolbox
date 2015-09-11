/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public interface PronounPredicate extends java.util.function.Predicate<Entity> {

    public boolean test(Parse t);
    public boolean test(Entity t);
    public void setPronoun(Parse PronounToken);
    
}
