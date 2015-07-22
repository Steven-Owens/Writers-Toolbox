/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import java.util.List;

/**
 *
 * @author Steven Owens
 */
public abstract class NonsplitingWrapperProcessingUnit<T> extends AbstractWrapperProcessingUnit<T> {

    @Override
    protected final List<String> doUnitProcessing(String inText) {
        return java.util.Collections.singletonList(doSingleProcessing(inText));
    }
    
    protected abstract String doSingleProcessing(String inText);
    
}
