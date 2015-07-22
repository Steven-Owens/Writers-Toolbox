/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.tokenize;

import com.civprod.util.TimeComplexity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import opennlp.tools.tokenize.DefaultTokenContextGenerator;
import opennlp.tools.tokenize.TokenContextGenerator;
import opennlp.tools.util.StringUtil;

/**
 *
 * @author Steven Owens
 */
public class WordSplittingContext implements TokenContextGenerator {

    protected final Set<String> wordList;
    protected final TokenContextGenerator mTokenContextGenerator;
    protected final TimeComplexity mTimeComplexity;
    protected final int maxWordLengh;

    public WordSplittingContext(TokenContextGenerator inTokenContextGenerator, Set<String> wordList, TimeComplexity inTimeComplexity) {
        this.wordList = wordList;
        if (!wordList.isEmpty()) {
            maxWordLengh = wordList.parallelStream().mapToInt((String curWord) -> curWord.length()).max().getAsInt();
        } else {
            maxWordLengh = 0;
        }
        mTokenContextGenerator = inTokenContextGenerator;
        if (inTimeComplexity == null) {
            inTimeComplexity = TimeComplexity.linear;
        }
        mTimeComplexity = inTimeComplexity;
    }

    /**
     * Returns an {@link ArrayList} of features for the specified sentence
     * string at the specified index. Extensions of this class can override this
     * method to create a customized {@link TokenContextGenerator}
     *
     * @param sentence the token been analyzed
     * @param index the index of the character been analyzed
     * @return an {@link ArrayList} of features for the specified sentence
     * string at the specified index.
     */
    protected Collection<String> createContext(String sentence, int index) {
        Collection<String> preds = new HashSet<>(java.util.Arrays.asList(mTokenContextGenerator.getContext(sentence, index)));
        String whiteSpaceTokenizedPrefix = findWhiteSpaceTokenizedPrefix(sentence, index);
        addStringPreds("wtp", whiteSpaceTokenizedPrefix, preds);
        String whiteSpaceTokenizedSuffix = findWhiteSpaceTokenizedSuffix(sentence, index);
        addStringPreds("wts", whiteSpaceTokenizedSuffix, preds);
        String whiteSpaceToken = whiteSpaceTokenizedPrefix + whiteSpaceTokenizedSuffix;
        addStringPreds("cwt", whiteSpaceToken, preds);
        return preds;
    }

    protected String findWhiteSpaceTokenizedPrefix(String sentence, int index) {
        String prefix;
        int start = index;
        while ((start >= 0) && (!StringUtil.isWhitespace(sentence.charAt(start)))) {
            start--;
        }
        start++;
        if (start == index) {
            prefix = "";
        } else {
            prefix = sentence.substring(start, index);
        }
        return prefix;
    }

    protected String findWhiteSpaceTokenizedSuffix(String sentence, int index) {
        String suffix;
        int end = index;
        while ((end < sentence.length()) && (!StringUtil.isWhitespace(sentence.charAt(end)))) {
            end++;
        }
        if (end == index) {
            suffix = "";
        } else {
            suffix = sentence.substring(index, end);
        }
        return suffix;
    }

    private void addStringPreds(String key, String value, Collection<String> preds) {
        final int valueLength = value.length();
        preds.add(key + "=" + value);
        String lowercaseValue = value.toLowerCase();
        if (wordList.contains(lowercaseValue)) {
            preds.add(key + "_isWord");
        }
        int index = Math.max(valueLength - (this.maxWordLengh + 1),0);
        final String endsInWordKey = key + "_endInWord";
        while (index < valueLength) {
            String valueSuffix = value.substring(index);
            if (wordList.contains(valueSuffix)) {
                if (!preds.contains(endsInWordKey)) {
                    preds.add(endsInWordKey);
                }
                String curWordSizeKey = endsInWordKey + "[" + valueSuffix.length() + "]";
                if (!preds.contains(curWordSizeKey)) {
                    preds.add(curWordSizeKey);
                }
                preds.add(endsInWordKey + "(" + index + ")");
                preds.add(endsInWordKey + "(" + index + ")" + "[" + valueSuffix.length() + "]");
                preds.add(endsInWordKey + "=" + valueSuffix);
            }
            index++;
        }
        index = Math.min(value.length(),(this.maxWordLengh + 1));
        final String beginsInWordKey = key + "_beginsInWord";
        while (index > 0) {
            String valuePrefix = value.substring(0, index);
            if (wordList.contains(valuePrefix)) {
                if (!preds.contains(beginsInWordKey)) {
                    preds.add(beginsInWordKey);
                }
                String curWordSizeKey = beginsInWordKey + "[" + valuePrefix.length() + "]";
                if (!preds.contains(curWordSizeKey)) {
                    preds.add(curWordSizeKey);
                }
                int steps = value.length() - index;
                preds.add(beginsInWordKey + "(" + steps + ")");
                preds.add(beginsInWordKey + "(" + steps + ")" + "[" + valuePrefix.length() + "]");
                preds.add(beginsInWordKey + "=" + valuePrefix);
            }
            index--;
        }
        if (this.mTimeComplexity.compareTo(TimeComplexity.quadraticTime) >= 0) {
            final String containsWordKey = key + "_containsWord";
            for (int start = 0; start < value.length(); start++) {
                for (int end = start; (end <= value.length()) && ((end - start) <= this.maxWordLengh); end++) {
                    String subString = value.substring(start, end);
                    if (wordList.contains(subString)) {
                        if (!preds.contains(containsWordKey)) {
                            preds.add(containsWordKey);
                        }
                        String curWordSizeKey = containsWordKey + "[" + subString.length() + "]";
                        if (!preds.contains(curWordSizeKey)) {
                            preds.add(curWordSizeKey);
                        }
                        int endSteps = value.length() - end;
                        preds.add(containsWordKey + "(" + start + "," + endSteps + ")");
                        preds.add(containsWordKey + "[" + subString.length() + "]" + "(" + start + "," + endSteps + ")");
                        preds.add(containsWordKey + "=" + subString);
                        preds.add(containsWordKey + "(" + start + "," + endSteps + ")" + "=" + subString);
                    }
                }
            }
            if (this.mTimeComplexity.compareTo(mTimeComplexity.exponentialTime) >= 0) {
                boolean found = findWordString(key, value, 0, new ArrayList<>(), preds);
                if (found) {
                    preds.add(key + "_foundWords");
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see opennlp.tools.tokenize.TokenContextGenerator#getContext(java.lang.String, int)
     */
    public String[] getContext(String sentence, int index) {
        Collection<String> preds = createContext(sentence, index);
        String[] context = new String[preds.size()];
        preds.toArray(context);
        return context;
    }

    private boolean findWordString(String key, String value, final int startIndex, List<String> foundWords, Collection<String> preds) {
        if (startIndex >= value.length()) {
            String foundWordsKey = key + "_foundWords";
            String foundWordsNumberKey = foundWordsKey + "(" + foundWords.size() + ")";
            for (String curWord : foundWords) {
                String curWordKey = foundWordsKey + "=" + curWord;
                String curWordNumberKey = foundWordsNumberKey + "=" + curWord;
                String curSizeKey = foundWordsKey + "[" + curWord.length() + "]";
                String curSizeNumberKey = foundWordsNumberKey + "[" + curWord.length() + "]";
                if (!preds.contains(curWordKey)) {
                    preds.add(curWordKey);
                }
                if (!preds.contains(curWordNumberKey)) {
                    preds.add(curWordNumberKey);
                }
                if (!preds.contains(curSizeKey)) {
                    preds.add(curSizeKey);
                }
                if (!preds.contains(curSizeNumberKey)) {
                    preds.add(curSizeNumberKey);
                }
            }
            if (!preds.contains(foundWordsNumberKey)) {
                preds.add(foundWordsNumberKey);
            }
            return true;
        }
        boolean found = false;
        int curIndex = startIndex + 1;
        while ((curIndex <= value.length()) && ((curIndex - startIndex) <= this.maxWordLengh)) {
            String subString = value.substring(startIndex, curIndex);
            if (wordList.contains(subString)) {
                List<String> newList = new ArrayList<>(foundWords);
                newList.add(subString);
                found |= findWordString(key, value, curIndex, newList, preds);
            }
            curIndex++;
        }
        return found;
    }
}
