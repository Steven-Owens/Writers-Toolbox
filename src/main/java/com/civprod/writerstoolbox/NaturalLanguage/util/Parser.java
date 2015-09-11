/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import java.util.List;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public interface Parser {

    Parse parse(List<String> tokens) throws ParsingError;

    Parse parse(String whiteSpacedTokenizedSentence) throws ParsingError;

    Parse parse(String sentence, StringTokenizer tokenizerToUse) throws ParsingError;

    public static class ParsingError extends CustomException {

        public ParsingError() {
            super();
        }

        public ParsingError(String message) {
            super(message);
        }

    }
}
