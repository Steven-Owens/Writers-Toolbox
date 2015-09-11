/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities;

import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class ParseEntity implements Entity{
    
    private final Parse interParse;
    private Gender mGender;
    private EntityNumber mNumber;
    
    public ParseEntity(Parse inParse){
        interParse = inParse;
        mGender = Gender.unknown;
        mNumber = EntityNumber.unknown;
    }

    @Override
    public String getLabel() {
        return interParse.getCoveredText();
    }

    @Override
    public void setGender(Gender inGender) {
        mGender = inGender;
    }

    @Override
    public Gender getGender() {
        return mGender;
    }

    @Override
    public void setNumber(EntityNumber inNumber) {
        mNumber = inNumber;
    }

    @Override
    public EntityNumber getNumber() {
        return mNumber;
    }

    /**
     * @return the interParse
     */
    public Parse getInterParse() {
        return interParse;
    }
    
}
