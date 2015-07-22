/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class Part {
    public final String content;
    public final Span indexIntoOriginal;
    
    public Part(String inToken, Span inIndexIntoOriginal){
        content = inToken;
        indexIntoOriginal = inIndexIntoOriginal;
    }
}
