/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.writerstoolbox.NaturalLanguage.util.PossiblyThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;

/**
 *
 * @author Steven Owens
 */
public interface WrapperProcessingUnit<T> extends Wrapper<T>, ProcessingUnit{
    
    @Override
    public default boolean isThreadSafe(){
        boolean threadSafe;
        if (this instanceof ThreadSafe){
            threadSafe = true;
        } else {
           T interObject = getInterObject();
           if (interObject instanceof ThreadSafe){
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
    
    default boolean canGuaranteeThreadSafety() {
        boolean threadSafe;
        if (this instanceof ThreadSafe){
            threadSafe = true;
        } else {
           T interObject = getInterObject();
           if (interObject instanceof ThreadSafe){
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
