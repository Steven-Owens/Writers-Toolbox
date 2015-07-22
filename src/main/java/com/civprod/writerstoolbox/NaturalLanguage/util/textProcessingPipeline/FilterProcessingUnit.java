/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.writerstoolbox.NaturalLanguage.util.PossiblyThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * stops processing on tokens that match the passed in regular expressions
 *
 * @author Steven Owens
 */
public final class FilterProcessingUnit extends AbstractWrapperProcessingUnit<Predicate<String>> implements ThreadSafe {

    private final Predicate<String> mFilter;
    private final java.util.concurrent.locks.ReentrantLock filterLock;

    public static boolean isFilterGuaranteedToBeThreadSafe(Predicate<String> inFilter) {
        boolean threadSafe;
        if (inFilter instanceof ThreadSafe) {
            threadSafe = true;
        } else if (inFilter instanceof PossiblyThreadSafe) {
            PossiblyThreadSafe possiblyThreadSafeFilter = (PossiblyThreadSafe) inFilter;
            threadSafe = possiblyThreadSafeFilter.isThreadSafe();
        } else {
            threadSafe = false;
        }
        if (!threadSafe) {
            Predicate<String> interNegatedFilter = inFilter.negate();
            if (interNegatedFilter instanceof ThreadSafe) {
                threadSafe = true;
            } else if (interNegatedFilter instanceof PossiblyThreadSafe) {
                PossiblyThreadSafe possiblyThreadSafeFilter = (PossiblyThreadSafe) interNegatedFilter;
                threadSafe = possiblyThreadSafeFilter.isThreadSafe();
            } else {
                threadSafe = false;
            }
        }
        return threadSafe;
    }

    public FilterProcessingUnit(Predicate<String> inFilter) {
        mFilter = inFilter;
        if ((mFilter instanceof ThreadSafe) || (mFilter.negate() instanceof ThreadSafe)) {
            filterLock = null;
        } else {
            filterLock = new java.util.concurrent.locks.ReentrantLock(false);
        }
    }

    @Override
    public List<String> process(String inText) {
        startedProcessing.set(true);
        List<String> rList = java.util.Collections.singletonList(inText);
        boolean passed;
        if (isSequential() || isFilterGuaranteedToBeThreadSafe(mFilter)) {
            passed = mFilter.test(inText);
        } else {
            filterLock.lock();
            try {
                passed = mFilter.test(inText);
            } finally {
                filterLock.unlock();
            }
        }
        if (passed) {
            rList = passToNextUnitIfExist(rList);
        }
        return rList;
    }

    @Override
    protected List<String> doUnitProcessing(String inText) {
        return java.util.Collections.singletonList(inText);
    }

    @Override
    public Predicate<String> getInterObject() {
        return this.mFilter;
    }

    @Override
    protected boolean canGuaranteeThreadSafety() {
        return true;
    }
}
