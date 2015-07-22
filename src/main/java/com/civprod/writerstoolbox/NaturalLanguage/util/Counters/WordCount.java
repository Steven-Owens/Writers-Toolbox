/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.Counters;

import com.civprod.writerstoolbox.NaturalLanguage.util.Counters.Counter;
import com.civprod.writerstoolbox.NaturalLanguage.util.WordHandling;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public class WordCount extends WordProcesser {
    
    public WordCount(WordHandling inWordMode)
    {
        super(inWordMode);
    }

    public Counter<String> countWords(String value) {
        List<String> rList = this.toTokenString(value);
        Counter<String> counter = new Counter<>();
        for (String curWord : rList){
            if (counter.containsKey(curWord)){
                counter.incrementCount(curWord, 1);
            } else {
                counter.setCount(curWord, 1);
            }
        }
        return counter;
    }
    
}
