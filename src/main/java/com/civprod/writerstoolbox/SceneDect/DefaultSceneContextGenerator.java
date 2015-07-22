/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.EntiyTagPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.PeriodPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.WhiteSpacePattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.XMLTagPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.apostropheOnBoundaryPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.canTContractionPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.contractionIgnorePattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.contractionPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.dashedWord;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.notContractionPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.notWordPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.notWordPreceedByWord;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.wonTContractionPattern;
import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.wordPreceedByNotWord;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.SceneDect.SceneBreak.RegexSceneBreakDetector;
import com.civprod.writerstoolbox.SceneDect.SceneBreak.SceneBreakDetector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class DefaultSceneContextGenerator implements SceneContextGenerator {

    private final List<SceneBreakDetector> mSceneBreakDetectors;
    private final StringTokenizer mStringTokenizer;
    private final List<String> markerWords;
    private final Map<String, Pattern> markerPatterns;

    private static StringTokenizer createDefaultStringTokenizer(String langCode) {
        List<Pattern> rList = new ArrayList<>(11);
        Map<Pattern, Pattern> rMap = new HashMap<>(4);
        Set<Pattern> rSet = new HashSet<>(1);
        rList.add(XMLTagPattern);
        rMap.put(XMLTagPattern, XMLTagPattern);
        rSet.add(XMLTagPattern);
        rList.add(EntiyTagPattern);
        rMap.put(EntiyTagPattern, EntiyTagPattern);
        rSet.add(EntiyTagPattern);
        if (langCode.equalsIgnoreCase("en")) {
            rList.add(WhiteSpacePattern);
            rSet.add(WhiteSpacePattern);
            rList.add(apostropheOnBoundaryPattern);
            rList.add(canTContractionPattern);
            rMap.put(canTContractionPattern, canTContractionPattern);
            rList.add(wonTContractionPattern);
            rMap.put(wonTContractionPattern,wonTContractionPattern);
            rList.add(notContractionPattern);
            rMap.put(notContractionPattern, notContractionPattern);
            rList.add(contractionPattern);
            rMap.put(contractionPattern, contractionIgnorePattern);
            rList.add(dashedWord);
            rMap.put(dashedWord, dashedWord);
            rList.add(notWordPreceedByWord);
            rList.add(wordPreceedByNotWord);
            rList.add(notWordPattern);
            rSet.add(notWordPattern);
        }
        return new RegexStringTokenizer(rList, rMap, rSet);
    }

    private DefaultSceneContextGenerator(StringTokenizer inStringTokenizer, List<SceneBreakDetector> inSceneBreakDetectors,
            List<String> inMarkerWords, Map<String, Pattern> inMarkerPatterns) {
        mSceneBreakDetectors = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(inSceneBreakDetectors));
        mStringTokenizer = inStringTokenizer;
        markerWords = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(inMarkerWords));
        markerPatterns = inMarkerPatterns;
    }

    /*public DefaultSceneContextGenerator(StringTokenizer inStringTokenizer, List<SceneBreakDetector> inSceneBreakDetectors, List<String> markerWords){
     this(inStringTokenizer,inSceneBreakDetectors,markerWords,markerWords);
     } 
    
     @SuppressWarnings("unchecked")
     public DefaultSceneContextGenerator(StringTokenizer inStringTokenizer, List<SceneBreakDetector> inSceneBreakDetectors){
     this(inStringTokenizer,inSceneBreakDetectors, java.util.Collections.EMPTY_LIST);
        
     }
    
     public DefaultSceneContextGenerator(StringTokenizer inStringTokenizer, SceneBreakDetector... inSceneBreakDetectors){
     this(inStringTokenizer,java.util.Arrays.asList(inSceneBreakDetectors));
     }
    
     public DefaultSceneContextGenerator(SceneBreakDetector... inSceneBreakDetectors){
     this(createDefaultStringTokenizer(),inSceneBreakDetectors);
     }
    
     public DefaultSceneContextGenerator(StringTokenizer inStringTokenizer){
     this(inStringTokenizer, 
     new RegexSceneBreakDetector(0), 
     new RegexSceneBreakDetector(1), 
     new RegexSceneBreakDetector(2), 
     new RegexSceneBreakDetector(3), 
     new RegexSceneBreakDetector(4), 
     new RegexSceneBreakDetector(5), 
     new RegexSceneBreakDetector(6), 
     new RegexSceneBreakDetector(7), 
     new RegexSceneBreakDetector(8), 
     new RegexSceneBreakDetector(9));
     }
    
     public DefaultSceneContextGenerator(){
     this(createDefaultStringTokenizer());
     }*/
    @Override
    public String[] getContext(String[] inParagraphs, int pos) {
        List<String> labels = new ArrayList<>();
        String curParagraph = inParagraphs[pos];
        for (int i = 0; i < mSceneBreakDetectors.size(); i++) {
            SceneBreakDetector curSceneBreakDetector = mSceneBreakDetectors.get(i);
            if (curSceneBreakDetector.isSceneBreak(curParagraph)) {
                labels.add("SceneBreak" + i);
            }
        }
        int tokenizedSize = mStringTokenizer.tokenize(curParagraph).size();
        for (int i = 0; i < tokenizedSize; i++) {
            labels.add("Long Paragraph > " + i + " words");
        }
        for (int i = 100; i > tokenizedSize; i--) {
            labels.add("Short Paragraph < " + i + " words");
        }
        int tokenSwitches = curParagraph.replaceAll("<[^>]*>", "").replaceAll("(.)\\1*", "x").length();
        for (int i = 0; i < tokenSwitches; i++) {
            labels.add("char Switches > " + i);
        }
        for (int i = 100; i > tokenSwitches; i--) {
            labels.add("char Switches < " + i);
        }
        for (String curMarkerWord : markerWords) {
            if (curParagraph.contains(curMarkerWord)) {
                labels.add("curMarkerWord:" + curMarkerWord);
            }
        }
        for (Map.Entry<String, Pattern> curPattern : markerPatterns.entrySet()) {
            if (curPattern.getValue().matcher(curParagraph).find()) {
                labels.add("curMarkerPattern:" + curPattern.getKey());
            }
        }
        if (pos > 0) {
            String prevParagraph = inParagraphs[pos - 1];
            for (String curMarkerWord : markerWords) {
                if (prevParagraph.contains(curMarkerWord)) {
                    labels.add("preMarkerWord:" + curMarkerWord);
                }
            }
            for (Map.Entry<String, Pattern> curPattern : markerPatterns.entrySet()) {
                if (curPattern.getValue().matcher(prevParagraph).find()) {
                    labels.add("preMarkerPattern:" + curPattern.getKey());
                }
            }
            if (pos > 1) {
                String prevPrevParagraph = inParagraphs[pos - 2];
                for (String curMarkerWord : markerWords) {
                    if (prevPrevParagraph.contains(curMarkerWord)) {
                        labels.add("prePreMarkerWord:" + curMarkerWord);
                    }
                }
                for (Map.Entry<String, Pattern> curPattern : markerPatterns.entrySet()) {
                    if (curPattern.getValue().matcher(prevPrevParagraph).find()) {
                        labels.add("prePreMarkerPattern:" + curPattern.getKey());
                    }
                }
            }
        }
        if (pos < (inParagraphs.length - 1)) {
            String nextParagraph = inParagraphs[pos + 1];
            for (String curMarkerWord : markerWords) {
                if (nextParagraph.contains(curMarkerWord)) {
                    labels.add("postMarkerWord:" + curMarkerWord);
                }
            }
            for (Map.Entry<String, Pattern> curPattern : markerPatterns.entrySet()) {
                if (curPattern.getValue().matcher(nextParagraph).find()) {
                    labels.add("postMarkerPattern:" + curPattern.getKey());
                }
            }
            if (pos < (inParagraphs.length - 2)) {
                String nextNextParagraph = inParagraphs[pos + 2];
                for (String curMarkerWord : markerWords) {
                    if (nextNextParagraph.contains(curMarkerWord)) {
                        labels.add("postPostMarkerWord:" + curMarkerWord);
                    }
                }
                for (Map.Entry<String, Pattern> curPattern : markerPatterns.entrySet()) {
                    if (curPattern.getValue().matcher(nextNextParagraph).find()) {
                        labels.add("postPostMarkerPattern:" + curPattern.getKey());
                    }
                }
            }
        }
        java.util.Collections.sort(labels);
        labels.add(curParagraph);
        return labels.toArray(new String[labels.size()]);
    }

    public static class Builder {

        private String langCode = null;
        private StringTokenizer mStringTokenizer = null;
        private List<SceneBreakDetector> mSceneBreakDetectors = null;
        private List<String> markerWords = null;
        private Map<String, Pattern> markerPatterns = null;

        public DefaultSceneContextGenerator build() {
            if (langCode == null) {
                langCode = "en";
            }
            if (mStringTokenizer == null) {
                mStringTokenizer = createDefaultStringTokenizer(langCode);
            }
            if (mSceneBreakDetectors == null) {
                addSceneBreakDetector(new RegexSceneBreakDetector(0));
                addSceneBreakDetector(new RegexSceneBreakDetector(1));
                addSceneBreakDetector(new RegexSceneBreakDetector(2));
                addSceneBreakDetector(new RegexSceneBreakDetector(3));
                addSceneBreakDetector(new RegexSceneBreakDetector(4));
                addSceneBreakDetector(new RegexSceneBreakDetector(5));
                addSceneBreakDetector(new RegexSceneBreakDetector(6));
                addSceneBreakDetector(new RegexSceneBreakDetector(7));
                addSceneBreakDetector(new RegexSceneBreakDetector(8));
                addSceneBreakDetector(new RegexSceneBreakDetector(9));
            }
            if (markerWords == null) {
                markerWords = java.util.Collections.EMPTY_LIST;
            }
            if (markerPatterns == null) {
                markerPatterns = java.util.Collections.EMPTY_MAP;
            }
            return new DefaultSceneContextGenerator(mStringTokenizer, mSceneBreakDetectors, markerWords, markerPatterns);
        }

        public Builder addSceneBreakDetector(SceneBreakDetector inSceneBreakDetector) {
            if (mSceneBreakDetectors == null) {
                mSceneBreakDetectors = new java.util.ArrayList<>(1);
            }
            mSceneBreakDetectors.add(inSceneBreakDetector);
            return this;
        }

        public Builder addSceneBreakDetector(SceneBreakDetector... inSceneBreakDetectors) {
            if (mSceneBreakDetectors == null) {
                mSceneBreakDetectors = new java.util.ArrayList<>(inSceneBreakDetectors.length);
            }
            mSceneBreakDetectors.addAll(java.util.Arrays.asList(inSceneBreakDetectors));
            return this;
        }

        public Builder addSceneBreakDetector(Collection<SceneBreakDetector> inSceneBreakDetectors) {
            if (mSceneBreakDetectors == null) {
                mSceneBreakDetectors = new java.util.ArrayList<>(inSceneBreakDetectors.size());
            }
            mSceneBreakDetectors.addAll(inSceneBreakDetectors);
            return this;
        }

        /**
         * @param mStringTokenizer the mStringTokenizer to set
         */
        public Builder setStringTokenizer(StringTokenizer mStringTokenizer) {
            this.mStringTokenizer = mStringTokenizer;
            return this;
        }

        public Builder addMarkerWord(String inMarkerWord) {
            if (markerWords == null) {
                markerWords = new java.util.ArrayList<>(1);
            }
            markerWords.add(inMarkerWord);
            return this;
        }

        public Builder addMarkerWord(String... inMarkerWords) {
            if (markerWords == null) {
                markerWords = new java.util.ArrayList<>(inMarkerWords.length);
            }
            markerWords.addAll(java.util.Arrays.asList(inMarkerWords));
            return this;
        }

        public Builder addMarkerWord(Collection<String> inMarkerWords) {
            if (markerWords == null) {
                markerWords = new java.util.ArrayList<>(inMarkerWords.size());
            }
            markerWords.addAll(inMarkerWords);
            return this;
        }

        public Builder addMarkerPattern(String inLabel, Pattern inMarkerPattern) {
            if (markerPatterns == null) {
                markerPatterns = new java.util.HashMap<>(1);
            }
            markerPatterns.put(inLabel, inMarkerPattern);
            return this;
        }

        public Builder addMarkerPattern(Map<String, Pattern> inMarkerPatternMap) {
            if (markerPatterns == null) {
                markerPatterns = new java.util.HashMap<>(inMarkerPatternMap.size());
            }
            markerPatterns.putAll(inMarkerPatternMap);
            return this;
        }

        public Builder addMarkerPattern(String inLabel, String inMarkerRegex) {
            return this.addMarkerPattern(inLabel, Pattern.compile(inMarkerRegex));
        }

        /**
         * @return the langCode
         */
        public String getLangCode() {
            return langCode;
        }

        /**
         * @param langCode the langCode to set
         * @return
         */
        public Builder setLangCode(String langCode) {
            this.langCode = langCode;
            return this;
        }
    }

}
