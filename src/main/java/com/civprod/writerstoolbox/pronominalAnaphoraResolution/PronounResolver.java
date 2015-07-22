/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public interface PronounResolver {
    public static String OTHER = "NonRef";
    
    public String[][] labelPronouns(String[][] document, Parse[] parses);
}
