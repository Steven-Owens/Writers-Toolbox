/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.NaturalLanguage.util.Part;
import com.civprod.writerstoolbox.data.Paragraph;
import java.util.List;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
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
public class ThoughtAndSpeechParserMETest {
    
    public ThoughtAndSpeechParserMETest() {
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
     * Test of parse method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testParse() {
        System.out.println("parse");
        Paragraph inParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        String[] expResult = null;
        String[] result = instance.parse(inParagraph);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parsePos method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testParsePos() {
        System.out.println("parsePos");
        Paragraph inParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        Span[] expResult = null;
        Span[] result = instance.parsePos(inParagraph);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBestSequence method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testGetBestSequence_StringArr() {
        System.out.println("getBestSequence");
        String[] tokenizedParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        Sequence expResult = null;
        Sequence result = instance.getBestSequence(tokenizedParagraph);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBestSequence method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testGetBestSequence_List() {
        System.out.println("getBestSequence");
        List<String> tokenizedParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        Sequence expResult = null;
        Sequence result = instance.getBestSequence(tokenizedParagraph);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEvalSpans method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testGetEvalSpans() {
        System.out.println("getEvalSpans");
        String[] tokenizedParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        Span[] expResult = null;
        Span[] result = instance.getEvalSpans(tokenizedParagraph);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of parseParts method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testParseParts() {
        System.out.println("parseParts");
        Paragraph inParagraph = null;
        ThoughtAndSpeechParserME instance = null;
        Part[] expResult = null;
        Part[] result = instance.parseParts(inParagraph);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of train method, of class ThoughtAndSpeechParserME.
     */
    @Test
    public void testTrain() throws Exception {
        System.out.println("train");
        String languageCode = "";
        ObjectStream<ThoughtAndSpeechSample> samples = null;
        ThoughtAndSpeechParserFactory sdFactory = null;
        TrainingParameters mlParams = null;
        ThoughtAndSpeechModel expResult = null;
        ThoughtAndSpeechModel result = ThoughtAndSpeechParserME.train(languageCode, samples, sdFactory, mlParams);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
