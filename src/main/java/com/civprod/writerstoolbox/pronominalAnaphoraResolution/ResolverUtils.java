/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import java.util.regex.Pattern;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class ResolverUtils {
    /** Regular expression for English singular third person pronouns. */
  public static final Pattern singularThirdPersonPronounPattern = Pattern.compile("^(he|she|it|him|her|his|hers|its|himself|herself|itself)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English plural third person pronouns. */
  public static final Pattern pluralThirdPersonPronounPattern = Pattern.compile("^(they|their|theirs|them|themselves)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English speech pronouns. */
  public static final Pattern speechPronounPattern = Pattern.compile("^(I|me|my|you|your|you|we|us|our|ours)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English female pronouns. */
  public static final Pattern femalePronounPattern = Pattern.compile("^(she|her|hers|herself)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English neuter pronouns. */
  public static final Pattern neuterPronounPattern = Pattern.compile("^(it|its|itself)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English first person pronouns. */
  public static final Pattern firstPersonPronounPattern = Pattern.compile("^(I|me|my|we|our|us|ours)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English singular second person pronouns. */
  public static final Pattern secondPersonPronounPattern = Pattern.compile("^(you|your|yours)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English third person pronouns. */
  public static final Pattern thirdPersonPronounPattern = Pattern.compile("^(he|she|it|him|her|his|hers|its|himself|herself|itself|they|their|theirs|them|themselves)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English singular pronouns. */
  public static final Pattern singularPronounPattern = Pattern.compile("^(I|me|my|he|she|it|him|her|his|hers|its|himself|herself|itself)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English plural pronouns. */
  public static final Pattern pluralPronounPattern = Pattern.compile("^(we|us|our|ours|they|their|theirs|them|themselves)$",Pattern.CASE_INSENSITIVE);
  /** Regular expression for English male pronouns. */
  public static final Pattern malePronounPattern = Pattern.compile("^(he|him|his|himself)$",Pattern.CASE_INSENSITIVE);
  
  public static double dictionaryRate(Dictionary genderDictionary, Pattern pronounPattern, Parse interParse) {
        String type = interParse.getType();
        double rVlaue = 0;
        if (!"TK".equalsIgnoreCase(type)) {
            if (genderDictionary.asStringSet().contains(interParse.getCoveredText())){
                rVlaue += 1.0;
            } 
        }
        if (type.equalsIgnoreCase("PRP") || type.equalsIgnoreCase("PRP$")) {
            if (pronounPattern.matcher(interParse.getCoveredText()).matches()){
                rVlaue += 1.0;
            }
        }
        for (Parse curSubParse : interParse.getChildren()){
            rVlaue += dictionaryRate(genderDictionary, pronounPattern,curSubParse);
        }
        return rVlaue;
    }

    private ResolverUtils() {
    }
}
