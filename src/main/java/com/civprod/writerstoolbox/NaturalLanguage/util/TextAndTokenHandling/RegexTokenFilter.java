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
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 * @author Steven Owens
 */
public class RegexTokenFilter implements TokenFilter, ThreadSafe {
    private final Set<String> filterRegEx;
    private final Set<Pattern> filterPattern;
    
    public RegexTokenFilter(Collection<String> inRegex){
        filterRegEx = org.apache.commons.collections4.SetUtils.unmodifiableSet(new HashSet<>(inRegex));
        filterPattern = org.apache.commons.collections4.SetUtils.unmodifiableSet(filterRegEx.parallelStream().map((String curRegEx) -> Pattern.compile(curRegEx)).collect(java.util.stream.Collectors.toSet()));
    }
    
    public RegexTokenFilter(String... inRegexes){
        this(java.util.Arrays.asList(inRegexes));
    }

    @Override
    public boolean test(String t) {
        return filterPattern.parallelStream().anyMatch((Pattern curPattern)-> curPattern.asPredicate().test(t));
    }
    
    public List<String> filter(List<String> tokens){
        return tokens.parallelStream().filter(this).collect(Collectors.toList());
    }
    
}
