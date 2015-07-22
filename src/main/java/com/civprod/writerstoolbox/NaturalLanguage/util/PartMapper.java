/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.NullTokenStripper;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TokenStripper;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.PipelineStart;
import com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline.TokenizerProcessingUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class PartMapper {

    //constrat: inPartsList has to been sorted and nonoverlapping 
    //the TokenStripper has to work on the MaximallyTokenized tokens
    public static List<Span> mapTokenList(List<String> maximallyTokenizedWhole, List<List<String>> inMaximallyTokenizedPartsList, TokenStripper inTokenStripper) {
        List<Span> returnPartList = new ArrayList<>(inMaximallyTokenizedPartsList.size());
        int indexIntoWhole = 0;
        int indexIntoParts = 0;
        while ((indexIntoWhole < maximallyTokenizedWhole.size()) && (indexIntoParts < inMaximallyTokenizedPartsList.size())) {
            List<String> curPart = inMaximallyTokenizedPartsList.get(indexIntoParts);
            int curIndexIntoPart = 0;
            while (curIndexIntoPart < curPart.size()) {
                while (!maximallyTokenizedWhole.get(indexIntoWhole).equals(curPart.get(0))) {
                    indexIntoWhole++;
                }
                curIndexIntoPart = 0;
                int wholeOffset = 0;
                while (curIndexIntoPart < curPart.size()) {
                    String curWholeToken = maximallyTokenizedWhole.get(indexIntoWhole + wholeOffset + curIndexIntoPart);
                    if (curWholeToken.equals(curPart.get(curIndexIntoPart))) {
                        curIndexIntoPart++;
                    } else if (inTokenStripper.wouldBeStripped(curWholeToken)) {
                        wholeOffset++;
                    } else {
                        break;
                    }
                }
                if (curIndexIntoPart < curPart.size()) {
                    indexIntoWhole++;
                } else {
                    returnPartList.add(new Span(indexIntoWhole, indexIntoWhole + wholeOffset + curIndexIntoPart));
                }
            }
            indexIntoParts++;
        }
        return returnPartList;
    }
    
    public static List<Span> mapTokenList(List<String> maximallyTokenizedWhole, List<List<String>> inMaximallyTokenizedPartsList){
        return mapTokenList(maximallyTokenizedWhole, inMaximallyTokenizedPartsList, new NullTokenStripper()); 
    }

    /*public static List<Span> mapTokenList(List<String> tokenizedWhole, List<List<String>> inTokenizedPartsList, TokenStripper inTokenStripper, StringTokenizer inStringTokenizer) {
        PipelineStart createdPipeline = PipelineStart.createPipeline(new TokenizerProcessingUnit(inStringTokenizer));
        List<String> newTokenizedWhole = createdPipeline.process(tokenizedWhole);
        List<Span> returnedSpans = mapTokenList(newTokenizedWhole,
                inTokenizedPartsList.parallelStream()
                .map((List<String> curPart) -> createdPipeline.process(curPart))
                .collect(Collectors.toList()),
                inTokenStripper);
        List<Part> tokenizedWholeMap = map(tokenizedWhole,newTokenizedWhole);
        int listSize = inTokenizedPartsList.size();
        List<Span> rList = new ArrayList<>(listSize);
        
    }*/

    /*public static List<Span> mapTokenList(List<String> tokenizedWhole, List<List<String>> inTokenizedPartsList, StringTokenizer inStringTokenizer){
        return mapTokenList(tokenizedWhole,inTokenizedPartsList,new NullTokenStripper(),inStringTokenizer);
    }*/
    
    public static List<Span> mapTokenList(String whole, List<List<String>> inTokenizedPartsList, TokenStripper inTokenStripper, StringTokenizer inStringTokenizer){
        PipelineStart createdPipeline = PipelineStart.createPipeline(new TokenizerProcessingUnit(inStringTokenizer));
        List<String> newTokenizedWhole = createdPipeline.process(whole);
        List<Span> returnedSpans = mapTokenList(createdPipeline.process(whole),
                inTokenizedPartsList.parallelStream()
                .map((List<String> curPart) -> createdPipeline.process(curPart))
                .collect(Collectors.toList()),
                inTokenStripper);
        List<Part> tokenizedWholeMap = map(whole,newTokenizedWhole);
        List<Span> rList = returnedSpans.parallelStream()
                .map((Span preSpan) -> new Span(tokenizedWholeMap.get(preSpan.getStart()).indexIntoOriginal.getStart(),tokenizedWholeMap.get(preSpan.getEnd()).indexIntoOriginal.getEnd()))
                .collect(Collectors.toList());
        return rList;
    }
    
    public static List<Span> mapTokenList(String whole, List<List<String>> inTokenizedPartsList, StringTokenizer inStringTokenizer){
        return mapTokenList(whole, inTokenizedPartsList, new NullTokenStripper(), inStringTokenizer);
    }
    
    public static List<Span> mapTokenList(String whole, List<List<String>> inTokenizedPartsList, TokenStripper inTokenStripper, StringTokenizer inStringTokenizer, SentenceDetector inSentenceDetector){
        PipelineStart createdPipeline = PipelineStart.createPipeline(new TokenizerProcessingUnit(inStringTokenizer));
        List<String> sentences = inSentenceDetector.sentenceDetect(whole);
        List<String> newTokenizedWhole = createdPipeline.process(sentences);
        List<Span> returnedSpans = mapTokenList(newTokenizedWhole, 
                inTokenizedPartsList.parallelStream()
                .map((List<String> curPart) -> createdPipeline.process(curPart))
                .collect(Collectors.toList()), 
                inTokenStripper);
        List<Part> tokenizedWholeMap = map(whole,newTokenizedWhole);
        List<Span> rList = returnedSpans.parallelStream()
                .map((Span preSpan) -> new Span(tokenizedWholeMap.get(preSpan.getStart()).indexIntoOriginal.getStart(),tokenizedWholeMap.get(preSpan.getEnd()).indexIntoOriginal.getEnd()))
                .collect(Collectors.toList());
        return rList;
    }
    
    public static List<Part> map(List<String> maximallyTokenizedWhole, List<String> inMaximallyTokenizedParts, TokenStripper inTokenStripper) {
        List<Span> spanList = mapTokenList(maximallyTokenizedWhole,
                inMaximallyTokenizedParts.parallelStream()
                .map((String curPart) -> java.util.Collections.singletonList(curPart))
                .collect(Collectors.toList()),
                inTokenStripper);
        int listSize = inMaximallyTokenizedParts.size();
        List<Part> rList = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            rList.add(new Part(inMaximallyTokenizedParts.get(i), spanList.get(i)));
        }
        return rList;
    }
    
    public static List<Part> map(List<String> maximallyTokenizedWhole, List<String> inMaximallyTokenizedParts){
        return map(maximallyTokenizedWhole,inMaximallyTokenizedParts, new NullTokenStripper());
    }
    
    /*public static List<Part> map(List<String> tokenizedWhole, List<String> inTokenizedParts, TokenStripper inTokenStripper, StringTokenizer inStringTokenizer){
        PipelineStart createdPipeline = PipelineStart.createPipeline(new TokenizerProcessingUnit(inStringTokenizer));
        List<Span> spanList = mapTokenList(createdPipeline.process(tokenizedWhole),
                inTokenizedParts.parallelStream()
        .map((String curPart) -> createdPipeline.process(curPart))
        .collect(Collectors.toList()),
                inTokenStripper);
        int listSize = inTokenizedParts.size();
        List<Part> rList = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            rList.add(new Part(inTokenizedParts.get(i), spanList.get(i)));
        }
        return rList;
    }

    public static List<Part> map(List<String> tokenizedWhole, List<String> inTokenizedParts, StringTokenizer inStringTokenizer){
        return map(tokenizedWhole,inTokenizedParts,new NullTokenStripper(),inStringTokenizer);
    }*/
    
    
    public static Part[] map(String whole, String parts[]) {
        Part newParts[] = new Part[parts.length];
        int indexIntoWhole = 0;
        int indexIntoParts = 0;
        while ((indexIntoWhole < whole.length()) && (indexIntoParts < parts.length)) {
            String curPart = parts[indexIntoParts];
            int curIndexIntoPart = 0;
            while (curIndexIntoPart < curPart.length()) {
                while (whole.charAt(indexIntoWhole) != curPart.charAt(0)) {
                    indexIntoWhole++;
                }
                curIndexIntoPart = 0;
                while (whole.charAt(indexIntoWhole + curIndexIntoPart) == curPart.charAt(curIndexIntoPart)) {
                    curIndexIntoPart++;
                }
                if (curIndexIntoPart < curPart.length()) {
                    indexIntoWhole++;
                } else {
                    newParts[indexIntoParts] = new Part(curPart, new Span(indexIntoWhole, indexIntoWhole + curIndexIntoPart));
                }
            }
            indexIntoParts++;
        }
        return newParts;
    }

    public static List<Part> map(String whole, List<String> parts) {
        return java.util.Arrays.asList(map(whole, parts.toArray(new String[parts.size()])));
    }
}
