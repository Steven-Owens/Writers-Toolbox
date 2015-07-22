/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.writerstoolbox.NaturalLanguage.util.PossiblyThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.TextTransformer;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public class TextTransformerProcessingUnit extends NonsplitingWrapperProcessingUnit<TextTransformer> implements ThreadSafe {
    private final TextTransformer mTextTransformer;
    private final java.util.concurrent.locks.ReentrantLock transformerLock;
    
    public static boolean isTextTransformerGuaranteedToBeThreadSafe(TextTransformer inTextTransformer) {
        boolean threadSafe;
        if (inTextTransformer instanceof ThreadSafe) {
            threadSafe = true;
        } else if (inTextTransformer instanceof PossiblyThreadSafe) {
            PossiblyThreadSafe possiblyThreadSafeFilter = (PossiblyThreadSafe) inTextTransformer;
            threadSafe = possiblyThreadSafeFilter.isThreadSafe();
        } else {
            threadSafe = false;
        }
        return threadSafe;
    }
    
    public TextTransformerProcessingUnit(TextTransformer inTextTransformer){
        mTextTransformer = inTextTransformer;
        if (mTextTransformer instanceof ThreadSafe) {
            transformerLock = null;
        } else {
            transformerLock = new java.util.concurrent.locks.ReentrantLock(false);
        }
    }

    @Override
    public TextTransformer getInterObject() {
        return this.mTextTransformer;
    }

    @Override
    protected String doSingleProcessing(String inText) {
        String rValue;
        if (isSequential() || isTextTransformerGuaranteedToBeThreadSafe(mTextTransformer)) {
            rValue = mTextTransformer.transform(inText);
        } else {
            transformerLock.lock();
            try {
                rValue = mTextTransformer.transform(inText);
            } finally {
                transformerLock.unlock();
            }
        }
        return rValue;
    }
    
    @Override
    protected boolean canGuaranteeThreadSafety() {
        return true;
    }
}
