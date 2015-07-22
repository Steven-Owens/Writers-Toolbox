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
public interface TokenFilter extends java.util.function.Predicate<String> {
    public default List<String> filter(List<String> tokens){
        return tokens.stream().filter(this).collect(Collectors.toList());
    }
}
