/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

import java.util.Map;

/**
 *
 * @author Steven Owens
 */
public class CustomException extends Exception {
    public Map<String,Object> data = new java.util.HashMap<>(0);
    public CustomException(){
        super();
    }
    
    public CustomException(String message){
        super(message);
    }
}
