/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.CustomException;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import java.util.List;
import java.util.stream.Collectors;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafeParser implements opennlp.tools.parser.Parser, com.civprod.writerstoolbox.NaturalLanguage.util.MutiParser, ThreadSafe {

    private final ParserModel mParserModel;

    public ThreadSafeParser(ParserModel inParserModel) {
        mParserModel = inParserModel;
    }

    public final ThreadLocal<opennlp.tools.parser.Parser> localParser = new ThreadLocal<opennlp.tools.parser.Parser>() {
        protected opennlp.tools.parser.Parser initialValue() {
            return ParserFactory.create(mParserModel);
        }
    };

    @Override
    public Parse[] parse(Parse parse, int i) {
        return localParser.get().parse(parse, i);
    }

    @Override
    public Parse parse(Parse parse) {
        return localParser.get().parse(parse);
    }

    @Override
    public Parse parse(List<String> tokens) throws ParsingError {
        return this.parse(tokens, 1)[0];
    }

    @Override
    public Parse parse(String whiteSpacedTokenizedSentence) throws ParsingError {
        return this.parse(whiteSpacedTokenizedSentence, 1)[0];
    }

    @Override
    public Parse parse(String sentence, StringTokenizer tokenizerToUse) throws ParsingError {
        return this.parse(sentence, tokenizerToUse, 1)[0];
    }

    @Override
    public Parse[] parse(List<String> tokens, int numberOfParses) throws ParsingError {
        return this.parse(tokens.parallelStream().collect(Collectors.joining(" ")), numberOfParses);
    }

    @Override
    public Parse[] parse(String whiteSpacedTokenizedSentence, int numberOfParses) throws ParsingError {
        Parse[] rParses;
        if (CommonRegexPatterns.allWhiteSpacePattern.matcher(whiteSpacedTokenizedSentence).matches()) {
            rParses = new Parse[numberOfParses];
        } else {
            try {
                rParses = ParserTool.parseLine(whiteSpacedTokenizedSentence, this, numberOfParses);
            } catch (Exception ex) {
                ParsingError newEx = new ParsingError("parsing error");
                newEx.initCause(ex);
                newEx.data.put("sentence", whiteSpacedTokenizedSentence);
                newEx.data.put("numberOfParses", numberOfParses);
                throw newEx;
            }
        }
        return rParses;
    }

    @Override
    public Parse[] parse(String sentence, StringTokenizer tokenizerToUse, int numberOfParses) throws ParsingError {
        return this.parse(tokenizerToUse.tokenize(sentence), numberOfParses);
    }
}
