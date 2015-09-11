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
public interface Entity {
    public String getLabel();
    public void setGender(Gender inGender);
    public Gender getGender();
    public void setNumber(EntityNumber inNumber);
    public EntityNumber getNumber();
    
}
