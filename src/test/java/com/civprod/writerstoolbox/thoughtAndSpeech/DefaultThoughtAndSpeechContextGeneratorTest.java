/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import java.util.Set;
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
public class DefaultThoughtAndSpeechContextGeneratorTest {
    
    public DefaultThoughtAndSpeechContextGeneratorTest() {
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
     * Test of normalizeWord method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testNormalizeWord() {
        System.out.println("normalizeWord");
        String token = "";
        DefaultThoughtAndSpeechContextGenerator instance = null;
        String expResult = "";
        String result = instance.normalizeWord(token);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isSaidWord method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testIsSaidWord() {
        System.out.println("isSaidWord");
        String prevToken = "";
        DefaultThoughtAndSpeechContextGenerator instance = null;
        boolean expResult = false;
        boolean result = instance.isSaidWord(prevToken);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of isThoughtWord method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testIsThoughtWord() {
        System.out.println("isThoughtWord");
        String prevToken = "";
        DefaultThoughtAndSpeechContextGenerator instance = null;
        boolean expResult = false;
        boolean result = instance.isThoughtWord(prevToken);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContextAsList method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testGetContextAsList() {
        System.out.println("getContextAsList");
        int index = 0;
        String[] sequence = null;
        String[] priorDecisions = null;
        Object[] additionalContext = null;
        DefaultThoughtAndSpeechContextGenerator instance = null;
        Set<String> expResult = null;
        Set<String> result = instance.getContextAsList(index, sequence, priorDecisions, additionalContext);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of normalizeLabel method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testNormalizeLabel() {
        System.out.println("normalizeLabel");
        String inLabel = "";
        DefaultThoughtAndSpeechContextGenerator instance = null;
        String expResult = "";
        String result = instance.normalizeLabel(inLabel);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContext method, of class DefaultThoughtAndSpeechContextGenerator.
     */
    @Test
    public void testGetContext() {
        System.out.println("getContext");
        int index = 0;
        String[] sequence = null;
        String[] priorDecisions = null;
        Object[] additionalContext = null;
        DefaultThoughtAndSpeechContextGenerator instance = null;
        String[] expResult = null;
        String[] result = instance.getContext(index, sequence, priorDecisions, additionalContext);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
