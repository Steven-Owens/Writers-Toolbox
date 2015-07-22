/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.writerstoolbox.NaturalLanguage.util.PossiblyThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public abstract class AbstractWrapperProcessingUnit<T> extends AbstractProcessingUnit implements Wrapper<T> {
    
    @Override
    protected boolean canGuaranteeThreadSafety() {
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
