/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.testarea;

import com.civprod.util.stream.FlatListCollector;
import com.civprod.util.stream.SummaryStatisticCollector;
import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.Counters.Counter;
import com.civprod.writerstoolbox.NaturalLanguage.util.Counters.CounterUtils;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.SentenceDetector;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.RegexTokenFilter;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.ThreadSafePorterStemmer;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TokenFilter;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TokenUtil;
import com.civprod.writerstoolbox.data.Document;
import com.civprod.writerstoolbox.data.Paragraph;
import com.civprod.writerstoolbox.data.Sentence;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import opennlp.tools.util.Span;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Steven Owens
 */
public class UnsupervisedDiscourseSegmentation {
    public static final ThreadSafePorterStemmer defaultStemmer = new ThreadSafePorterStemmer();
    public static final String wordPattern = CommonRegexPatterns.wordClassNoApostrophe + CommonRegexPatterns.wordClass + "+" + CommonRegexPatterns.wordClassNoApostrophe;
    public static final TokenFilter defaultTokenFilter = new RegexTokenFilter(wordPattern);
    
    public static double cosineSimilarityUnfilteredUnstemmed(List<String> a, List<String> b){
        return cosineSimilarityStemmedAndFiltered(TokenUtil.stemmAndFilterList(a),TokenUtil.stemmAndFilterList(b));
    }
    
    public static double cosineSimilarityStemmedAndFiltered(List<String> a, List<String> b){
        Counter<String> countA = TokenUtil.buildFreqMap(a);
        Counter<String> countB = TokenUtil.buildFreqMap(b);
        return cosineSimilarityStemmedAndFiltered(countA,countB);
    }
    public static double cosineSimilarityStemmedAndFiltered(Counter<String> countA, Counter<String> countB){
        Set<String> allWords = countA.keySet().parallelStream().filter((String curKey) -> countB.keySet().contains(curKey)).collect(Collectors.toSet());
        double dotProduct = allWords.parallelStream().mapToDouble((String curKey)-> countA.getCount(curKey)*countB.getCount(curKey)).sum();
        double sqrtOfSumA = Math.pow(countA.keySet().parallelStream().mapToDouble((String curKey)->countA.getCount(curKey)).map((double curValue)->curValue*curValue).sum(),.5);
        double sqrtOfSumB = Math.pow(countB.keySet().parallelStream().mapToDouble((String curKey)->countB.getCount(curKey)).map((double curValue)->curValue*curValue).sum(),.5);
        return dotProduct/(sqrtOfSumA*sqrtOfSumB);
    }
    
    private static final FlatListCollector<String> FlatListCollectorInstance = new FlatListCollector<String>();
    
    public static List<String> concatenateTokens(Document<?> inDocument,SentenceDetector inSentenceDetector, StringTokenizer inStringTokenizer){
        return inDocument.getParagraphs().parallelStream()
                 .map((Paragraph<?> curParagraph)-> {
                     return curParagraph
                             .getSentences(inSentenceDetector)
                             .parallelStream()
                             .map((Sentence curSentence)-> curSentence.getStrippedTokens(inStringTokenizer))
                             .collect(FlatListCollectorInstance);
                         })
                .collect(FlatListCollectorInstance);
    }
    
    public static List<List<String>> splitIntoFixLengthLists(List<String> inList, int length){
        int sizeOfRList = ((inList.size()-1)/length)+1;
        List<List<String>> rList = new ArrayList<>(sizeOfRList);
        for (int i = 0; i < sizeOfRList; i++){
            int startIndex = i*length;
            int endIndex = startIndex + length;
            if (endIndex > inList.size()){
                endIndex = inList.size();
            }
            rList.add(inList.subList(startIndex, endIndex));
        }
        return rList;
    }
    
    public static List<List<String>> segment(Document<?> inDocument,SentenceDetector inSentenceDetector, StringTokenizer inStringTokenizer){
        List<String> concatenateTokens = concatenateTokens(inDocument,inSentenceDetector, inStringTokenizer);
        List<String> stemmAndFilterList = TokenUtil.stemmAndFilterList(concatenateTokens);
        List<List<String>> splitIntoFixLengthLists = splitIntoFixLengthLists(stemmAndFilterList,20);
        List<Counter<String>> counters = splitIntoFixLengthLists.parallelStream()
                .map((List<String> curSentence) -> CounterUtils.count(curSentence))
                .collect(Collectors.toList());
        List<Double> cosineSimilarity = new ArrayList<>(counters.size()-20);
        for (int i = 0; i < (counters.size()-20); i++){
            cosineSimilarity.add(cosineSimilarityStemmedAndFiltered(Counter.join(counters.subList(i, i+10)),Counter.join(counters.subList(i+11, i+20))));
        }
        List<Double> valleys = new ArrayList<>(cosineSimilarity.size()-2);
        for (int i = 0; i < valleys.size(); i++){
            double ya1 = cosineSimilarity.get(i);
            double ya2 = cosineSimilarity.get(i+1);
            double ya3 = cosineSimilarity.get(i+2);
            valleys.add((ya1-ya2)+(ya3-ya2));
        }
        SummaryStatistics valleyStatistics = valleys.parallelStream().collect(SummaryStatisticCollector.instance);
        double cutoffThreshold = valleyStatistics.getMean()-valleyStatistics.getStandardDeviation();
        int lastLocation = 0;
        List<Span> spans = new ArrayList<>(1);
        for (int i = 0; i < valleys.size(); i++){
            double curValley = valleys.get(i);
            if (curValley < cutoffThreshold){
                int curLocation = (i + 11)*20;
                spans.add(new Span(lastLocation,curLocation));
                lastLocation = curLocation;
            }
        }
        spans.add(new Span(lastLocation, concatenateTokens.size()));
        return spans.parallelStream().map((Span curSpan) -> concatenateTokens.subList(curSpan.getStart(), curSpan.getEnd())).collect(Collectors.toList());
    }
}
