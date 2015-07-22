/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.training;

import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorEvaluator;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.eval.FMeasure;

/**
 *
 * @author Steven Owens
 */
public class TestEval {

    public static void main(String args[]) {
        List<Integer> gold = new java.util.ArrayList<>(100);
        for (int i = 0; i < 100; i++) {
            gold.add(i);
        }
        List<Integer> test = new java.util.ArrayList<>(gold);
        test.remove(50);
        test.add(101);
        FMeasure newFMeasure = new FMeasure();
        newFMeasure.updateScores(gold.toArray(new Integer[100]), test.toArray(new Integer[100]));
        System.out.print("F " + newFMeasure.getFMeasure() + " Precision " + newFMeasure.getPrecisionScore() + " Recall " + newFMeasure.getRecallScore() + "\n");
        newFMeasure = new FMeasure();
        java.util.Collections.shuffle(test);
        newFMeasure.updateScores(gold.toArray(new Integer[100]), test.toArray(new Integer[100]));
        System.out.print("F " + newFMeasure.getFMeasure() + " Precision " + newFMeasure.getPrecisionScore() + " Recall " + newFMeasure.getRecallScore() + "\n");
        SentenceDetector stdDetector = null;
        try {
            //stdDetector = OpenNLPUtils.createSentenceDetector(OpenNLPUtils.readSentenceModel(OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-fiction-sent.bin")));
            //stdDetector = OpenNLPUtils.createSentenceDetector();
            stdDetector = OpenNLPUtils.createSentenceDetector(OpenNLPUtils.readSentenceModel(OpenNLPUtils.buildModelFileStream("en-fiction-sent.bin")));

        } catch (IOException ex) {
        }
        System.out.print("\n");
        if (stdDetector != null) {
            File testFile = new File("en-sent.test");
            Charset charset = Charset.forName("UTF-8");
            ObjectStream<String> testingLineStream = null;
            try {
                testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), charset);
                ObjectStream<SentenceSample> sampleStream = null;
                try {
                    sampleStream = new SentenceSampleStream(testingLineStream);
                    SentenceDetectorEvaluator stdEvaluator = new SentenceDetectorEvaluator(stdDetector);
                    SentenceSample curSample = sampleStream.read();
                    while (curSample != null){
                        stdEvaluator.evaluateSample(curSample);
                        FMeasure curFMeasure = stdEvaluator.getFMeasure();
                        System.out.print("F " + curFMeasure.getFMeasure() + " Precision " + curFMeasure.getPrecisionScore() + " Recall " + curFMeasure.getRecallScore() + "\n");
                        curSample = sampleStream.read();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (sampleStream != null) {
                        try {
                            sampleStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (testingLineStream != null) {
                    try {
                        testingLineStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            System.out.print("\n");
            testingLineStream = null;
            try {
                testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), charset);
                ObjectStream<SentenceSample> sampleStream = null;
                try {
                    sampleStream = new SentenceSampleStream(testingLineStream);
                    SentenceDetectorEvaluator stdEvaluator = new SentenceDetectorEvaluator(stdDetector);
                    stdEvaluator.evaluate(sampleStream);
                        FMeasure curFMeasure = stdEvaluator.getFMeasure();
                        System.out.print("F " + curFMeasure.getFMeasure() + " Precision " + curFMeasure.getPrecisionScore() + " Recall " + curFMeasure.getRecallScore() + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (sampleStream != null) {
                        try {
                            sampleStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (testingLineStream != null) {
                    try {
                        testingLineStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }
}
