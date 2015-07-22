/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.util.stream.FlatListCollector;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author Steven Owens
 */
public abstract class AbstractProcessingUnit implements ProcessingUnit {
    
    private ProcessingUnit mNextProcessingUnit = null;
    private boolean threadSafeFlag = false;
    private boolean sequentialized = false;
    protected final AtomicBoolean startedProcessing = new AtomicBoolean(false);
    
    public List<String> process(String inText){
        startedProcessing.set(true);
        return passToNextUnitIfExist(doUnitProcessing(inText));
    }
    
    protected List<String> passToNextUnitIfExist(List<String> inText){
        startedProcessing.set(true);
        //FlatListCollector
        List<String> rList;
        if (mNextProcessingUnit != null){
            if (sequentialized){
                rList = inText.stream().map((String curToken) -> mNextProcessingUnit.process(curToken)).collect(FlatListCollector.FlatListCollectorString);
            } else {
            rList = inText.parallelStream().map((String curToken) -> mNextProcessingUnit.process(curToken)).collect(FlatListCollector.FlatListCollectorString);
            }
        } else {
           rList = inText;
        }
        return rList;
    }
    
    public void setNextUnit(ProcessingUnit inNextProcessingUnit){
        mNextProcessingUnit = inNextProcessingUnit;
    }
    public ProcessingUnit getNextUnit(){
        return mNextProcessingUnit;
    }

    protected abstract List<String> doUnitProcessing(String inText);
    
    public final boolean isThreadSafe(){
        if (threadSafeFlag){
            return true;
        } else {
            return canGuaranteeThreadSafety();
        }
    }
    public final void overRideThreadSafe(boolean isThreadSafe){
        if (startedProcessing.get()){
            throw new java.lang.IllegalStateException("Can't override thread safe after processing has started");
        }
        threadSafeFlag = isThreadSafe;
    }

    protected abstract boolean canGuaranteeThreadSafety();
    
    public void sequentialize(){
        sequentialized = true;
        if (mNextProcessingUnit != null){
            mNextProcessingUnit.sequentialize();
        }
    }
    
    public boolean isSequential(){
        return sequentialized;
    }
}
