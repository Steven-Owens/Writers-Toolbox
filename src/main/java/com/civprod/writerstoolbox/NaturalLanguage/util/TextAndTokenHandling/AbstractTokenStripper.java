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
public abstract class AbstractTokenStripper implements TokenStripper {

    @Override
    public abstract String strip(String text);

    @Override
    public String[] strip(String[] tokens) {
        return strip(java.util.Arrays.asList(tokens)).toArray(new String[tokens.length]);
    }

    @Override
    public List<String> strip(List<String> tokens) {
        return tokens.parallelStream()
                .filter((String curToken) -> !wouldBeStripped(curToken))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public abstract boolean wouldBeStripped(String token);

    @Override
    public String transform(String inText) {
        return strip(inText);
    }

    @Override
    public String[] transform(String[] inText) {
        return strip(inText);
    }

    @Override
    public List<String> transform(List<String> inText) {
        return strip(inText);
    }
    
}
