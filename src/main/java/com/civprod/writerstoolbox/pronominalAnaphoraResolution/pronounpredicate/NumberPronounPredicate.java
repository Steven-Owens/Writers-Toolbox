/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate;

import static com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns.doubleNumberPattern;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.ResolverUtils;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.EntityNumber;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Gender;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.ParseEntity;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class NumberPronounPredicate extends EntityPrimaryPronounPredicate {
    
    private final Dictionary singularCardinalNumberDictionary;
    private final Dictionary pluralCardinalNumberDictionary;
    private final Dictionary massNounDictionary;
    private final Dictionary singularDeterminerDictionary;
    private final Dictionary pluralDeterminerDictionary;
    
    public NumberPronounPredicate(Dictionary singularDictionary,Dictionary pluralDictionary){
        this(singularDictionary,pluralDictionary,pluralDictionary);
    }
    
    public NumberPronounPredicate(Dictionary singularDictionary,Dictionary pluralDictionary,Dictionary massNounDictionary){
        this(singularDictionary,pluralDictionary,massNounDictionary,singularDictionary,pluralDictionary);
    }
    
    public NumberPronounPredicate(Dictionary singularCardinalNumberDictionary,Dictionary pluralCardinalNumberDictionary,Dictionary massNounDictionary,Dictionary singularDeterminerDictionary,Dictionary pluralDeterminerDictionary){
        this.singularCardinalNumberDictionary = singularCardinalNumberDictionary;
        this.pluralCardinalNumberDictionary = pluralCardinalNumberDictionary;
        this.massNounDictionary = massNounDictionary;
        this.pluralDeterminerDictionary = pluralDeterminerDictionary;
        this.singularDeterminerDictionary = singularDeterminerDictionary;
    }

    @Override
    public boolean test(Entity t) {
        if ((t.getNumber() == EntityNumber.unknown) || (t.getNumber() == null)){
            if (t instanceof ParseEntity){
                EntityNumber determinedNumberNP = determineNumberNP(((ParseEntity)t).getInterParse());
                if (determinedNumberNP == null){
                    determinedNumberNP = EntityNumber.unknown;
                }
                t.setNumber(determinedNumberNP);
            }
        }
        EntityNumber pronounNumber = determineNumberPronoun(this.PronounToken);
        if ((t.getNumber() == EntityNumber.unknown) || (pronounNumber == EntityNumber.unknown)){
            return true;
        } else {
            return pronounNumber == t.getNumber();
        }
    }

    private EntityNumber determineNumberNP(Parse interParse) {
        EntityNumber rValue = null;
        String coveredText = interParse.getCoveredText().trim().toLowerCase();
        String type = interParse.getType();
        if (type.equalsIgnoreCase("CD")){  
            if (singularCardinalNumberDictionary.asStringSet().contains(coveredText)){
                rValue = EntityNumber.singular;
            } else if (pluralCardinalNumberDictionary.asStringSet().contains(coveredText)){
                rValue = EntityNumber.plural;
            }
            if (doubleNumberPattern.matcher(coveredText).matches()){
                double amount = Double.parseDouble(coveredText);
                if (amount > 1){
                    rValue = EntityNumber.plural;
                } else if (amount < -1) {
                    rValue = EntityNumber.plural;
                } else {
                    rValue = EntityNumber.singular;
                }
            }
        }
        if (type.equalsIgnoreCase("NNS") || type.equalsIgnoreCase("NNPS")){
            rValue = EntityNumber.plural;
        }
        if (type.equalsIgnoreCase("NN")){
            if (massNounDictionary.asStringSet().contains(coveredText)){
                rValue = EntityNumber.plural;
            }
        }
        if (type.equalsIgnoreCase("DT") || type.equalsIgnoreCase("PDT") || type.equalsIgnoreCase("WDT")){
            if (singularDeterminerDictionary.asStringSet().contains(coveredText)){
                rValue = EntityNumber.singular;
            } else if (pluralDeterminerDictionary.asStringSet().contains(coveredText)){
                rValue = EntityNumber.plural;
            }
        }
        if (type.equalsIgnoreCase("PRP") || type.equalsIgnoreCase("PRP$")) {
            rValue = determineNumberPronoun(interParse);
        }
        if (rValue == null) {
            //resolve conflicts smarter
            Parse[] children = interParse.getChildren();
            for (Parse curChild : children){
                EntityNumber returnNumber = determineNumberNP(curChild);
                if (returnNumber != null){
                    if (rValue == null){
                        rValue = returnNumber;
                    } else {
                        if (rValue != returnNumber){
                            rValue = EntityNumber.unknown;
                            break;
                        }
                    }
                }
            }
        }
        return rValue;
    }

    private EntityNumber determineNumberPronoun(Parse PronounToken) {
        String pronoun = PronounToken.getCoveredText();
        if (ResolverUtils.pluralPronounPattern.matcher(pronoun).matches()){
            return EntityNumber.plural;
        } else if (ResolverUtils.singularPronounPattern.matcher(pronoun).matches()){
            return EntityNumber.singular;
        } else {
            return EntityNumber.unknown;
        }
    }
    
}
