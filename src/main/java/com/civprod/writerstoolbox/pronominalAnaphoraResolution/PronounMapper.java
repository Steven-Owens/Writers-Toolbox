/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import static com.civprod.writerstoolbox.pronominalAnaphoraResolution.HobbsAlgorithm.NPOrSPattern;
import java.util.HashMap;
import java.util.Map;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class PronounMapper {

    private Map<Parse, Parse> PronounMap = new HashMap<>();

    public void addPronoun(Parse curPronounTokenParse, Parse antecedent) {
        PronounMap.put(curPronounTokenParse, antecedent);
        Parse curPOSParse = curPronounTokenParse.getParent();
        PronounMap.put(curPOSParse, antecedent);
        /*Parse X = curPOSParse.getParent();
        while ((X != null) && (!NPOrSPattern.matcher(X.getType()).matches())) {
            X = X.getParent();
        }
        PronounMap.put(X, antecedent);*/
    }

    public Parse findImmediateAntecedent(Parse curNPParse) {
        Parse antecedent = PronounMap.get(curNPParse);
        if (antecedent == null) {
            if (curNPParse.getChildCount()==1){
                Parse curChild = curNPParse.getChildren()[0];
                if (curChild.getType().contains("PRP")) {
                    antecedent = PronounMap.get(curChild);
                    if (antecedent == null) {
                        for (Parse curChild2 : curChild.getChildren()) {
                            antecedent = PronounMap.get(curChild2);
                            if (antecedent != null) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return antecedent;
    }
    
    public Parse findUltimateAntecedent(Parse curNPParse) {
        Parse ultimateAntecedent = curNPParse;
        Parse newAntecedent = curNPParse;
        while (newAntecedent != null){
            ultimateAntecedent = newAntecedent;
            newAntecedent = findImmediateAntecedent(newAntecedent);
        }
        return ultimateAntecedent;
    }
}
