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
public class PipelineStart extends AbstractProcessingUnit {
    
    @Override
    public List<String> process(String inText){
        return process(java.util.Collections.singletonList(inText));
    }
    
    public List<String> process(List<String> inText){
        List<String> rList = passToNextUnitIfExist(inText);
        //do any finalizshion here
        return rList;
    }
    
    @Override
    protected boolean canGuaranteeThreadSafety() {
        return true;
    }
    
    public static PipelineStart createPipeline(ProcessingUnit... ProcessingUnits){
        return createPipeline(false,ProcessingUnits);
    }
    
    public static PipelineStart createPipeline(boolean overrideThreadSafety, ProcessingUnit... ProcessingUnits){
        boolean sequentialize = false;
        if (overrideThreadSafety){
            sequentialize = false;
        } else {
            sequentialize = java.util.Arrays.asList(ProcessingUnits).parallelStream().anyMatch((ProcessingUnit curProcessingUnit) -> !curProcessingUnit.isThreadSafe());
        }
        PipelineStart newStart = new PipelineStart();
        newStart.setNextUnit(ProcessingUnits[0]);
        for (int i = 0; i < ProcessingUnits.length-1;i++){
            ProcessingUnits[i].setNextUnit(ProcessingUnits[i+1]);
        }
        if (sequentialize){
            newStart.sequentialize();
        }
        return newStart;
    }

    @Override
    protected List<String> doUnitProcessing(String inText) {
        return java.util.Collections.singletonList(inText);
    }
}
