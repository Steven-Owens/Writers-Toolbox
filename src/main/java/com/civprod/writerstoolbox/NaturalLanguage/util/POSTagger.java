/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import java.util.List;

/**
 *
 * @author Steven Owens
 */
public interface POSTagger {
    public List<String> tag(List<String> sentence);
}
