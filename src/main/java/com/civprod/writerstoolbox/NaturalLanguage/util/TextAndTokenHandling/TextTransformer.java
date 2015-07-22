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
public interface TextTransformer {
    public String transform(String inText);
    public default String[] transform(String[] inText){
        return transform(java.util.Arrays.asList(inText)).toArray(new String[inText.length]);
    }
    public default List<String> transform(List<String> inText){
        return inText.parallelStream()
                .filter((String curToken) -> curToken != null)
                .map((String curToken) -> transform(curToken))
                .filter((String curToken) -> !curToken.isEmpty())
                .collect(Collectors.toList());
    }
}
