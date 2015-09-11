/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import opennlp.tools.util.ObjectStream;
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
public class ThoughtAndSpeechSampleStreamTest {
    
    public ThoughtAndSpeechSampleStreamTest() {
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
     * Test of createDefaultHTMLVersion method, of class ThoughtAndSpeechSampleStream.
     */
    @Test
    public void testCreateDefaultHTMLVersion() {
        System.out.println("createDefaultHTMLVersion");
        ObjectStream<String> sentences = null;
        ThoughtAndSpeechSampleStream expResult = null;
        ThoughtAndSpeechSampleStream result = ThoughtAndSpeechSampleStream.createDefaultHTMLVersion(sentences);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of read method, of class ThoughtAndSpeechSampleStream.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        ThoughtAndSpeechSampleStream instance = null;
        ThoughtAndSpeechSample expResult = null;
        ThoughtAndSpeechSample result = instance.read();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
