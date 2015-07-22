/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.Counters;

import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.ImmutableWordHandling;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.PorterStemmer;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.WordHandling;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class WordProcesser {
    
    protected final WordHandling myWordMode;
    public static final Pattern NonWordPattern = java.util.regex.Pattern.compile("(^[^\\w0-9']*$)");
    public static final Pattern NumberPattern = java.util.regex.Pattern.compile("(^[0-9.,]*$)");
    protected final PorterStemmer myPorterStemmer;
    protected final StringTokenizer myStringTokenizer;
    
    protected WordProcesser(WordHandling inWordMode)
    {
        if (inWordMode.isMutable()){
            myWordMode = new ImmutableWordHandling(inWordMode);
        } else {
            myWordMode = inWordMode;
        }
        if (myWordMode.stemWords()){
           myPorterStemmer = new PorterStemmer();
        } else {
            myPorterStemmer = null;
        }
        if (myWordMode.useNaturalLanguageTokenizer()){
            myStringTokenizer = new RegexStringTokenizer();
        } else {
            myStringTokenizer = null;
        }
    }
    
    protected List<String> toTokenString(String value){
        List<String> rawList; 
        if (myWordMode.normalizeApostrophes()){
            value = value.replaceAll(CommonRegexPatterns.apostrophe, "'");
        }
        if (myWordMode.useNaturalLanguageTokenizer()){
            rawList = myStringTokenizer.tokenize(value);
        } else {
            rawList = new java.util.ArrayList<>(java.util.Arrays.asList(value.split("\\s")));
        }
        if (myWordMode.dropPunctuation()){
            for (int i = rawList.size()-1; i >=0; i--){
                if (NonWordPattern.matcher(rawList.get(i)).matches()){
                    rawList.remove(i);
                }
            }
        }
        if (myWordMode.maskNumbers()){
            for (int i = rawList.size()-1; i >=0; i--){
                String curWord = rawList.get(i);
                if (NumberPattern.matcher(curWord).matches()){
                    rawList.set(i, curWord.replaceAll("\\d", "X"));
                }
            }
        }
        List<String> rList;
        if (myWordMode.stemWords()){
            rList = new java.util.ArrayList<>(rawList.size());
            for (String curWord : rawList){
                rList.add(myPorterStemmer.stem(curWord));
            }
        } else {
            rList = rawList;
        }
        return rList;
    }
}
