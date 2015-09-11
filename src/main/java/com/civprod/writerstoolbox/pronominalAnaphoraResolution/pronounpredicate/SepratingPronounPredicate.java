/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.HobbsAlgorithm;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class SepratingPronounPredicate extends ParsePrimaryPronounPredicate {
    
    public boolean test(Parse t) {
        Parse head = t.getCommonParent(PronounToken);
        Parse curLink = t;
        boolean foundSOrNP = false;
        while ((!foundSOrNP) && (!curLink.equals(head))){
            curLink = curLink.getParent();
            if (HobbsAlgorithm.NPOrSPattern.matcher(curLink.getType()).matches()){
                foundSOrNP = true;
                break;
            }
        }
        curLink = PronounToken;
        while ((!foundSOrNP) && (!curLink.equals(head))){
            curLink = curLink.getParent();
            if (HobbsAlgorithm.NPOrSPattern.matcher(curLink.getType()).matches()){
                foundSOrNP = true;
                break;
            }
        }
        return foundSOrNP;
    }
}
