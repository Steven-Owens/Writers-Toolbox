/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TokenStripper;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public class NullTokenStripper implements TokenStripper, ThreadSafe {
    
    public static final NullTokenStripper instance = new NullTokenStripper();

    @Override
    public String strip(String text) {
        return text;
    }

    @Override
    public List<String> strip(List<String> tokens) {
        return tokens;
    }

    @Override
    public boolean wouldBeStripped(String token) {
        return false;
    }
    
    @Override
    public boolean equals(Object o){
        return o instanceof NullTokenStripper;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public String[] strip(String[] tokens) {
        return tokens;
    }

    @Override
    public String transform(String inText) {
        return inText;
    }

    @Override
    public String[] transform(String[] inText) {
        return inText;
    }

    @Override
    public List<String> transform(List<String> inText) {
        return inText;
    }
    
}
