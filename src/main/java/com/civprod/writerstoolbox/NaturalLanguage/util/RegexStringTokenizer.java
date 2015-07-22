/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections4.ListUtils;

/**
 *
 * @author Steven Owens
 */
public class RegexStringTokenizer implements StringTokenizer, ThreadSafe {
    
    private final List<Pattern> applyOrder;
    private final Map<Pattern,Pattern> ignoreMapping;
    private final Set<Pattern> removePatterns;
    
    public static List<Pattern> getDefualtApplyOrder(){
        List<Pattern> rList = new ArrayList<>(11);
        rList.add(PeriodPattern);
        rList.add(XMLTagPattern);
        rList.add(EntiyTagPattern);
        rList.add(WhiteSpacePattern);
        rList.add(apostropheOnBoundaryPattern);
        rList.add(canTContractionPattern);
        rList.add(wonTContractionPattern);
        rList.add(notContractionPattern);
        rList.add(contractionPattern);
        rList.add(dashedWord);
        rList.add(notWordPreceedByWord);
        rList.add(wordPreceedByNotWord);
        rList.add(notWordPattern);
        return rList;
    }
    
    public static Map<Pattern,Pattern> getDefualtIgnoreMapping(){
        Map<Pattern,Pattern> rMap = new HashMap<>(4);
        rMap.put(XMLTagPattern, XMLTagPattern);
        rMap.put(EntiyTagPattern, EntiyTagPattern);
        rMap.put(canTContractionPattern,canTContractionPattern);
        rMap.put(wonTContractionPattern,wonTContractionPattern);
        rMap.put(notContractionPattern,notContractionPattern);
        rMap.put(contractionPattern,contractionIgnorePattern);
        rMap.put(dashedWord,dashedWord);
        return rMap;
    }
    
    public static Set<Pattern> getRemovePatterns(){
        Set<Pattern> rList = new HashSet<>(1);
        rList.add(WhiteSpacePattern);
        return rList;
    }
    
    public RegexStringTokenizer(){
        this(getDefualtApplyOrder(),getDefualtIgnoreMapping());
    }
    
    public RegexStringTokenizer(List<Pattern> applyOrder, Map<Pattern,Pattern> ignoreMapping){
        this(applyOrder, ignoreMapping, getRemovePatterns());
    }
    
    public RegexStringTokenizer(List<Pattern> applyOrder, Map<Pattern,Pattern> ignoreMapping, Set<Pattern> removePatterns){
        this.applyOrder = ListUtils.unmodifiableList(new ArrayList<>(applyOrder));
        this.ignoreMapping = org.apache.commons.collections4.MapUtils.unmodifiableMap(new HashMap<>(ignoreMapping));
        this.removePatterns = org.apache.commons.collections4.SetUtils.unmodifiableSet(new HashSet<>(removePatterns));
    }

    public List<String> TokenizeFileAsOneLine(File inFile) {
        List<String> ReturnList = new java.util.ArrayList<String>(0);
        try {
            java.io.BufferedReader fin = new java.io.BufferedReader(new java.io.FileReader(inFile));
            try {
                while (fin.ready()) {
                    String Line = fin.readLine();
                    ReturnList.addAll(tokenize(Line));
                }
            } catch (IOException ex) {
                Logger.getLogger(RegexStringTokenizer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fin.close();
                } catch (IOException ex) {
                    Logger.getLogger(RegexStringTokenizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RegexStringTokenizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ReturnList;
    }
    

    @Override
    public List<String> tokenize(String Text) {
        List<String> ReturnList = new java.util.ArrayList<>(1);
        ReturnList.add(Text);
        List<Pattern> ignorePatterns = new java.util.ArrayList<>(ignoreMapping.size());
        for (Pattern curPattern : applyOrder){
            ReturnList = Tokenize(ReturnList, curPattern, ignorePatterns, ReturnList.size());
            if (ignoreMapping.containsKey(curPattern)){
                ignorePatterns.add(ignoreMapping.get(curPattern));
            }
        }
        for (Pattern curRemovePattern : removePatterns){
            ReturnList = ReturnList.parallelStream()
                    .filter((String curWord) -> !curRemovePattern.matcher(curWord).matches())
                    .collect(Collectors.toList());
        }
        return ReturnList;
    }

    private static List<String> Tokenize(List<String> ReturnList, Pattern possivePattern, List<Pattern> ignorePatterns, int sizeGuess) {
        List<String> TempList = new java.util.ArrayList<>(sizeGuess);
        for (String curWord : ReturnList) {
            if (ignorePatterns.parallelStream().noneMatch((Pattern curPattern) -> curPattern.matcher(curWord).matches())) {
                Matcher matcher = possivePattern.matcher(curWord);
                int lastEnd = 0;
                while (matcher.find()) {
                    String pre = curWord.substring(lastEnd, matcher.start());
                    if (!pre.isEmpty()) {
                        TempList.add(pre);
                    }
                    String match = curWord.substring(matcher.start(), matcher.end());
                    if (!match.isEmpty()) {
                        TempList.add(match);
                    }
                    lastEnd = matcher.end();
                }
                String end = curWord.substring(lastEnd);
                if (!end.isEmpty()) {
                    TempList.add(end);
                }
            } else {
               TempList.add(curWord); 
            }
        }
        return TempList;
    }
}