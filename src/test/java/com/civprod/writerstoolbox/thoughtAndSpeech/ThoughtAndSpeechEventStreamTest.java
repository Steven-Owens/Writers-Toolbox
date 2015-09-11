/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import java.util.Iterator;
import opennlp.tools.ml.model.Event;
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
public class ThoughtAndSpeechEventStreamTest {
    
    public ThoughtAndSpeechEventStreamTest() {
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
     * Test of createEvents method, of class ThoughtAndSpeechEventStream.
     */
    @Test
    public void testCreateEvents() {
        System.out.println("createEvents");
        ThoughtAndSpeechSample sample = null;
        ThoughtAndSpeechEventStream instance = null;
        Iterator<Event> expResult = null;
        Iterator<Event> result = instance.createEvents(sample);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
