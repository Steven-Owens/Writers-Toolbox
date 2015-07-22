/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import java.util.List;

/**
 *
 * @author Steven Owens
 */
public interface TokenStripper extends TextTransformer {
    public String strip(String text);
    public default String[] strip(String[] tokens) {
        return strip(java.util.Arrays.asList(tokens)).toArray(new String[tokens.length]);
    }

    public default List<String> strip(List<String> tokens) {
        return tokens.parallelStream()
                .filter((String curToken) -> !wouldBeStripped(curToken))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public boolean wouldBeStripped(String token);
    
    @Override
    public default String transform(String inText) {
        return strip(inText);
    }

    @Override
    public default String[] transform(String[] inText) {
        return strip(inText);
    }

    @Override
    public default List<String> transform(List<String> inText) {
        return strip(inText);
    }
}
