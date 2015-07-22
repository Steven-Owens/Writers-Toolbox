/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.textProcessingPipeline;

import com.civprod.writerstoolbox.NaturalLanguage.util.PossiblyThreadSafe;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public interface ProcessingUnit extends PossiblyThreadSafe {
    public List<String> process(String inText);
    public void setNextUnit(ProcessingUnit inProcessingUnit);
    public ProcessingUnit getNextUnit();
    @Override
    public boolean isThreadSafe();
    public void overRideThreadSafe(boolean isThreadSafe);
    public void sequentialize();
}
