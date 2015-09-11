/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import opennlp.tools.util.eval.FMeasure;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechEvaluatorTest {
    
    public ThoughtAndSpeechEvaluatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getFMeasure method, of class ThoughtAndSpeechEvaluator.
     */
    @Test
    public void testGetFMeasure() {
        System.out.println("getFMeasure");
        ThoughtAndSpeechEvaluator instance = null;
        FMeasure expResult = null;
        FMeasure result = instance.getFMeasure();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processSample method, of class ThoughtAndSpeechEvaluator.
     */
    @Test
    public void testProcessSample() {
        System.out.println("processSample");
        ThoughtAndSpeechSample t = null;
        ThoughtAndSpeechEvaluator instance = null;
        ThoughtAndSpeechSample expResult = null;
        ThoughtAndSpeechSample result = instance.processSample(t);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
