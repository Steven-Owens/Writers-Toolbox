/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import java.util.regex.Pattern;

/**
 *
 * @author Steven Owens
 */
public class CommonRegexPatterns {
    public static final String doubleQoute = "[\"“”]";
    public static final String apostrophe = "('|\\u2019|\\u2018)";
    public static final String wordClassNoApostrophe = "[\\w0-9\\.]";
    public static final String wordClass = "[\\w0-9\\.'\\u2019\\u2018]";
    public static final String notWordClass = "[^\\w0-9\\.'\\u2019\\u2018]";
    public static final String XMLTagRegex = "<[^>]+>";
    public static final String EntiyRegEx = "&[\\w0-9]+;";
    public static final String WhiteSpaceRegex = "[\\s\\u00a0]";
    public static final String doubleNumberRegEx = "^-?\\d+(\\.\\d+)?$";
    public static final String allWhiteSpaceRegex = "^" +WhiteSpaceRegex + "*$";
    public static final Pattern allWhiteSpacePattern = Pattern.compile(allWhiteSpaceRegex);
    public static final Pattern doubleNumberPattern = Pattern.compile(doubleNumberRegEx);
    public static final Pattern PeriodPattern = java.util.regex.Pattern.compile("(?<=[^\\.])\\.(?=("+WhiteSpaceRegex+"*"+XMLTagRegex+")*"+doubleQoute+"?("+WhiteSpaceRegex+"*"+XMLTagRegex+")*$)");
    public static final Pattern XMLTagPattern = java.util.regex.Pattern.compile(XMLTagRegex);
    public static final Pattern EntiyTagPattern = java.util.regex.Pattern.compile(EntiyRegEx);
    public static final Pattern WhiteSpacePattern = Pattern.compile(WhiteSpaceRegex+"+");
    public static final Pattern apostropheOnBoundaryPattern = java.util.regex.Pattern.compile("(^"+ apostrophe+")|("+ apostrophe+"$)");
    public static final Pattern canTContractionPattern = java.util.regex.Pattern.compile("((C|c)an"+ apostrophe+"t)|(CAN"+ apostrophe+"T)$");
    public static final Pattern wonTContractionPattern = java.util.regex.Pattern.compile("((W|w)on"+ apostrophe+"t)|(WON"+ apostrophe+"T)$");
    public static final Pattern notContractionPattern = java.util.regex.Pattern.compile("(n"+ apostrophe+"t)|(N"+ apostrophe+"T)$");
    public static final Pattern contractionPattern = java.util.regex.Pattern.compile("(?<="+wordClassNoApostrophe+")"+ apostrophe+wordClassNoApostrophe+"+");
    public static final Pattern contractionIgnorePattern = java.util.regex.Pattern.compile(apostrophe+wordClassNoApostrophe+"+");
    public static final Pattern dashedWord = java.util.regex.Pattern.compile("("+wordClass+"+-)+("+wordClass+"+)");
    public static final Pattern notWordPreceedByWord = java.util.regex.Pattern.compile(notWordClass+"+(?="+wordClass+")");
    public static final Pattern wordPreceedByNotWord = java.util.regex.Pattern.compile("(?<="+wordClass+")"+notWordClass+"+");
    public static final Pattern notWordPattern = java.util.regex.Pattern.compile("("+notWordClass+")\\1*");
}
