/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import com.swabunga.spell.event.WordTokenizer;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public interface StringTokenizer{
    public List<String> tokenize(String Text);
}
