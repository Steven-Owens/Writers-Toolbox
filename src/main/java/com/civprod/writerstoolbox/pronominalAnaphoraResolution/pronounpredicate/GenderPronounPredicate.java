/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.ResolverUtils;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Gender;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class GenderPronounPredicate extends EntityPrimaryPronounPredicate {
    
    private final Dictionary femaleDictionary;
    private final Dictionary maleDictionary;
    private final Dictionary neuterDictionary;
    private final double threshold;
    
    public static final double oneThird = 1.0/3.0;
    
    public GenderPronounPredicate(Dictionary femaleDictionary,Dictionary maleDictionary,Dictionary neuterDictionary, double threshold){
        this.femaleDictionary = femaleDictionary;
        this.maleDictionary = maleDictionary;
        this.neuterDictionary = neuterDictionary;
        if (threshold <= oneThird){
            this.threshold = oneThird;
        } else {
            this.threshold = threshold;
        }
    }

    @Override
    public boolean test(Entity t) {
        if (t.getGender() == Gender.unknown){
            if (t instanceof ParseEntity){
                t.setGender(determineGenderNP(((ParseEntity)t).getInterParse()));
            }
        }
        Gender pronounGender = determineGenderPronoun(this.PronounToken);
        if ((t.getGender() == Gender.unknown) || (pronounGender == Gender.unknown)){
            return true;
        } else {
            return pronounGender == t.getGender();
        }
    }

    private Gender determineGenderNP(Parse interParse) {
        Gender outputGender = Gender.unknown;
       double simpleFemaleRating = ResolverUtils.dictionaryRate(femaleDictionary,ResolverUtils.femalePronounPattern,interParse);
       double simpleMaleRating = ResolverUtils.dictionaryRate(maleDictionary,ResolverUtils.malePronounPattern,interParse);
       double simpleNeuterRating = ResolverUtils.dictionaryRate(neuterDictionary,ResolverUtils.neuterPronounPattern,interParse);
       double totalRating = simpleFemaleRating + simpleMaleRating + simpleNeuterRating;
       if ((simpleFemaleRating > .001) && (simpleMaleRating < .0001) && (simpleNeuterRating < .0001)){
           outputGender = Gender.female;
       } else if ((simpleMaleRating > .001) && (simpleFemaleRating < .0001) && (simpleNeuterRating < .0001)){
           outputGender = Gender.male;
       } else if ((simpleNeuterRating > .001) && (simpleMaleRating < .0001) && (simpleFemaleRating < .0001)){
           outputGender = Gender.neuter;
       }
       if (totalRating > 0.0001){
           if ((simpleFemaleRating/totalRating)>threshold){
               outputGender = Gender.female;
           } else if ((simpleMaleRating/totalRating)>threshold){
               outputGender = Gender.male;
           } else if ((simpleNeuterRating/totalRating)>threshold){
               outputGender = Gender.neuter;
           }
       }
       return outputGender;
    }

    private Gender determineGenderPronoun(Parse PronounToken) {
        String pronoun = PronounToken.getCoveredText();
        if (ResolverUtils.femalePronounPattern.matcher(pronoun).matches()){
            return Gender.female;
        } else if (ResolverUtils.malePronounPattern.matcher(pronoun).matches()){
            return Gender.male;
        } else if (ResolverUtils.neuterPronounPattern.matcher(pronoun).matches()){
            return Gender.neuter;
        } else {
            return Gender.unknown;
        }
    }
    
}
