/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Steven Owens
 */
public class RegexTransformer extends AbstractTextTransformer implements TextTransformer, ThreadSafe {
    private final Map<String,String> transformationRules;
    
    public RegexTransformer(Map<String,String> inTransformationRules){
        transformationRules = new HashMap<>(inTransformationRules);
    }

    @Override
    public String transform(String inText) {
        String rValue = inText;
        for (Map.Entry<String,String> curRule : transformationRules.entrySet()){
            rValue = rValue.replaceAll(curRule.getKey(), curRule.getValue());
        }
        return rValue;
    }
}
