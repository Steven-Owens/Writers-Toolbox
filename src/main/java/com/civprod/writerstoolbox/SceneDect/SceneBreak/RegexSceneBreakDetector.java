/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect.SceneBreak;

import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.EntiyTagPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.PeriodPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.WhiteSpacePattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.XMLTagPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.notWordPattern;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.RegexTokenStripper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class RegexSceneBreakDetector implements SceneBreakDetector {
    
    private static RegexStringTokenizer mRegexStringTokenizer;
   
    static {
        List<Pattern> rList = new ArrayList<>(11);
        rList.add(XMLTagPattern);
        rList.add(EntiyTagPattern);
        rList.add(WhiteSpacePattern);
        rList.add(java.util.regex.Pattern.compile("^[^\\w]$"));
        Map<Pattern,Pattern> rMap = new HashMap<>(4);
        rMap.put(XMLTagPattern, XMLTagPattern);
        rMap.put(EntiyTagPattern, EntiyTagPattern);
        mRegexStringTokenizer = new RegexStringTokenizer(rList,rMap,new java.util.HashSet<>(rList));
    }
    
    private final int mLimit;
    
    public RegexSceneBreakDetector(){
        this(0);
    }
    
    public RegexSceneBreakDetector(int inLimit){
        mLimit = inLimit;
    }

    @Override
    public boolean isSceneBreak(String inParagraph) {
        List<String> tokenize = mRegexStringTokenizer.tokenize(inParagraph);
        return tokenize.size() <= mLimit;
    }
    
}
