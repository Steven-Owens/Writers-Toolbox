/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class RegexTokenStripper extends AbstractTokenStripper implements TokenStripper, ThreadSafe {
    private final Set<String> removeRegEx;
    private final Set<Pattern> removePattern;
    
    public RegexTokenStripper(Collection<String> inRegex){
        removeRegEx = org.apache.commons.collections4.SetUtils.unmodifiableSet(new HashSet<>(inRegex));
        removePattern = org.apache.commons.collections4.SetUtils.unmodifiableSet(removeRegEx.parallelStream().map((String curRegEx) -> Pattern.compile(curRegEx)).collect(java.util.stream.Collectors.toSet()));
    }
    
    public RegexTokenStripper(String... inRegexes){
        this(java.util.Arrays.asList(inRegexes));
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (o instanceof RegexTokenStripper){
            return this.removeRegEx.equals(((RegexTokenStripper)o).removeRegEx);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.removeRegEx);
        return hash;
    }

    @Override
    public String strip(String text) {
        String rString = text;
        for (String curRegEx : removeRegEx){
            rString = rString.replaceAll(curRegEx, "");
        }
        return rString;
    }

    @Override
    public boolean wouldBeStripped(String token) {
        return removePattern.parallelStream().anyMatch((Pattern curPattern) -> curPattern.matcher(token).matches());
    }
}
