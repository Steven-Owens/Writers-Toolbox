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
import java.util.function.Predicate;

/**
 *
 * @author Steven Owens
 */
public final class TokenizerProcessingUnit extends AbstractWrapperProcessingUnit<StringTokenizer> implements ThreadSafe {

    private final StringTokenizer mStringTokenizer;
    private final java.util.concurrent.locks.ReentrantLock tokenizerLock;

    public static boolean isTokenizerGuaranteedToBeThreadSafe(StringTokenizer inStringTokenizer) {
        boolean threadSafe;
        if (inStringTokenizer instanceof ThreadSafe) {
            threadSafe = true;
        } else if (inStringTokenizer instanceof PossiblyThreadSafe) {
            PossiblyThreadSafe possiblyThreadSafeFilter = (PossiblyThreadSafe) inStringTokenizer;
            threadSafe = possiblyThreadSafeFilter.isThreadSafe();
        } else {
            threadSafe = false;
        }
        return threadSafe;
    }

    public TokenizerProcessingUnit(StringTokenizer inStringTokenizer) {
        mStringTokenizer = inStringTokenizer;
        if (mStringTokenizer instanceof ThreadSafe) {
            tokenizerLock = null;
        } else {
            tokenizerLock = new java.util.concurrent.locks.ReentrantLock(false);
        }

    }

    @Override
    protected List<String> doUnitProcessing(String inText) {
        List<String> rList;
        if (isSequential() || isTokenizerGuaranteedToBeThreadSafe(mStringTokenizer)) {
            rList = mStringTokenizer.tokenize(inText);
        } else {
            tokenizerLock.lock();
            try {
                rList = mStringTokenizer.tokenize(inText);
            } finally {
                tokenizerLock.unlock();
            }
        }
        return rList;
    }

    @Override
    public StringTokenizer getInterObject() {
        return mStringTokenizer;
    }
    
    @Override
    protected boolean canGuaranteeThreadSafety() {
        return true;
    }
}
