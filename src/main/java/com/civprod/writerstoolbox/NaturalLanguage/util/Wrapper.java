/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

/**
 *
 * @author Steven Owens
 */
public interface Wrapper<T> extends PossiblyThreadSafe {
    public T getInterObject();
    
    @Override
    public default boolean isThreadSafe(){
        boolean threadSafe;
        if (PossiblyThreadSafe.isThreadSafeClassOrInterface(this)){
            threadSafe = true;
        } else {
           T interObject = getInterObject();
           if (PossiblyThreadSafe.isThreadSafeClassOrInterface(interObject)){
               threadSafe = true;
           } else if (interObject instanceof PossiblyThreadSafe){
               PossiblyThreadSafe possiblyThreadSafeInterObject = (PossiblyThreadSafe)interObject;
               threadSafe = possiblyThreadSafeInterObject.isThreadSafe();
           } else {
               threadSafe = false;
           }
        }
        return threadSafe;
    }
}
