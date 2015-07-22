/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Steven Owens
 */
public abstract class AbstractTextTransformer implements TextTransformer {
    
    @Override
    public abstract String transform(String inText);
    
    @Override
    public String[] transform(String[] inText) {
        return transform(java.util.Arrays.asList(inText)).toArray(new String[inText.length]);
    }

    @Override
    public List<String> transform(List<String> inText) {
        return inText.parallelStream().map((String curToken) -> transform(curToken)).collect(Collectors.toList());
    }
    
}
