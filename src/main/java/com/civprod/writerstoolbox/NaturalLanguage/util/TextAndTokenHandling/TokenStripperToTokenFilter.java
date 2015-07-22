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
public class TokenStripperToTokenFilter implements TokenFilter {
    private TokenStripper mTokenStripper;
    
    public TokenStripperToTokenFilter(TokenStripper inTokenStripper){
        mTokenStripper =inTokenStripper;
    }

    @Override
    public boolean test(String t) {
        return !mTokenStripper.wouldBeStripped(t);
    }
    
    public List<String> filter(List<String> tokens){
        return tokens.parallelStream().filter(this).collect(Collectors.toList());
    }
}
