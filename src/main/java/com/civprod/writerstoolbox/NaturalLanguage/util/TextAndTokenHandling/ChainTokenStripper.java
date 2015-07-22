/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.util.ChainListWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author Steven Owens
 */
public class ChainTokenStripper extends ChainListWrapper<TokenStripper> implements TokenStripper {
   
    public ChainTokenStripper(List<TokenStripper> inTagStrippers){
        super(inTagStrippers);
    }
    
    public ChainTokenStripper(TokenStripper... inTagStrippers){
        super(inTagStrippers);
    }

    @Override
    public String strip(String text) {
        String rString = text;
        for (TokenStripper curTagStripper : this.interList){
            rString = curTagStripper.strip(rString);
        }
        return rString; 
    }
    
    @Override
    public boolean wouldBeStripped(String token){
        return interList.parallelStream().anyMatch((TokenStripper curTagStripper) -> curTagStripper.wouldBeStripped(token));
    }
}
