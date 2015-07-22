/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.writerstoolbox.NaturalLanguage.util.Counters.Counter;
import com.civprod.writerstoolbox.NaturalLanguage.util.Counters.CounterUtils;
import static com.civprod.writerstoolbox.testarea.UnsupervisedDiscourseSegmentation.defaultStemmer;
import static com.civprod.writerstoolbox.testarea.UnsupervisedDiscourseSegmentation.defaultTokenFilter;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Steven Owens
 */
public class TokenUtil {
    
    public static Counter<String> buildFreqMap(List<String> wordList){
        return CounterUtils.count(wordList);
    }
    
    public static List<String> filterList(List<String> wordList){
        return defaultTokenFilter.filter(wordList);
    }
    
    public static List<String> stemmList(List<String> wordList){
        return wordList.parallelStream().map(defaultStemmer::stem).collect(Collectors.toList());
    }
    
    public static List<String> stemmAndFilterList(List<String> wordList){
        return wordList.parallelStream().filter(defaultTokenFilter).map(defaultStemmer::stem).collect(Collectors.toList());
    }

    private TokenUtil() {
    }
}
