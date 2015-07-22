/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.Counters;

import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.PorterStemmer;
import com.civprod.writerstoolbox.NaturalLanguage.util.CommonWordModes;
import com.civprod.writerstoolbox.NaturalLanguage.util.MutableWordHandling;
import com.civprod.writerstoolbox.NaturalLanguage.util.WordHandling;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class BigramCount extends WordProcesser {
    
    public BigramCount(WordHandling inWordMode)
    {
        super(inWordMode);
    }
    
    public CounterMap<String,String> countBigrams(String value){
        List<String> rList = this.toTokenString(value);
        CounterMap<String,String> counts = new CounterMap<>();
        for (int i = 1; i < rList.size(); i++){
            if (counts.getCounter(rList.get(i-1)).containsKey(rList.get(i))){
                counts.incrementCount(rList.get(i-1), rList.get(i), 1);
            } else {
                counts.setCount(rList.get(i-1), rList.get(i), 1);
            }
        }
        return counts;
    }
}
