/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.training;

import com.civprod.swing.CollectionListModel;
import com.civprod.util.TimeComplexity;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import com.civprod.writerstoolbox.OpenNLP.tokenize.WordSplittingTokenizerFactory;
import com.civprod.writerstoolbox.data.io.localFS.FileUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerEvaluator;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Steven Owens
 */
public class WordSplitingTokenizerTrainer extends EditorWindow<TokenizerModel> {

    private Dictionary mAbbreviationDictionary = null;
    private Dictionary mSpellingDictionary = null;
    private CollectionListModel<File> mFileCollectionListModel;

    /**
     * Creates new form WordSplitingTokenizer
     */
    public WordSplitingTokenizerTrainer() {
        initComponents();
        mFileCollectionListModel = new CollectionListModel<>();
        listFiles.setModel(mFileCollectionListModel);
        comboTimeComplexity.setModel(new CollectionListModel<>(java.util.EnumSet.allOf(TimeComplexity.class)));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myFileChooser = new javax.swing.JFileChooser();
        cmdCreateAbbreviationDictionary = new javax.swing.JButton();
        cmdLoadAbbreviationDictionary = new javax.swing.JButton();
        cmdCreateSpellingDictionary = new javax.swing.JButton();
        cmdLoadSpellingDictionary = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList<File>();
        cmdLoadTrainingFile = new javax.swing.JButton();
        cmdDeleteTrainingFile = new javax.swing.JButton();
        cmdTrain = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textTestResults = new javax.swing.JTextArea();
        comboTimeComplexity = new javax.swing.JComboBox<TimeComplexity>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmdCreateAbbreviationDictionary.setText("create abbreviation Dictionary");
        cmdCreateAbbreviationDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateAbbreviationDictionaryActionPerformed(evt);
            }
        });

        cmdLoadAbbreviationDictionary.setText("load abbreviation Dictionary");
        cmdLoadAbbreviationDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadAbbreviationDictionaryActionPerformed(evt);
            }
        });

        cmdCreateSpellingDictionary.setText("create spelling Dictionary");
        cmdCreateSpellingDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdCreateSpellingDictionaryActionPerformed(evt);
            }
        });

        cmdLoadSpellingDictionary.setText("load spelling Dictionary");
        cmdLoadSpellingDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadSpellingDictionaryActionPerformed(evt);
            }
        });

        listFiles.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listFiles);

        cmdLoadTrainingFile.setText("load training file");
        cmdLoadTrainingFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadTrainingFileActionPerformed(evt);
            }
        });

        cmdDeleteTrainingFile.setText("delete training file");
        cmdDeleteTrainingFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdDeleteTrainingFileActionPerformed(evt);
            }
        });

        cmdTrain.setText("train model");
        cmdTrain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTrainActionPerformed(evt);
            }
        });

        textTestResults.setColumns(20);
        textTestResults.setRows(5);
        jScrollPane2.setViewportView(textTestResults);

        comboTimeComplexity.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmdCreateAbbreviationDictionary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdLoadAbbreviationDictionary))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cmdCreateSpellingDictionary)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdLoadSpellingDictionary))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmdLoadTrainingFile)
                                    .addComponent(cmdDeleteTrainingFile)))
                            .addComponent(cmdTrain)
                            .addComponent(comboTimeComplexity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCreateAbbreviationDictionary)
                    .addComponent(cmdLoadAbbreviationDictionary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmdCreateSpellingDictionary)
                    .addComponent(cmdLoadSpellingDictionary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboTimeComplexity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdLoadTrainingFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdDeleteTrainingFile)))
                .addGap(18, 18, 18)
                .addComponent(cmdTrain)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(142, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCreateAbbreviationDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateAbbreviationDictionaryActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        new Thread(() -> {
            final DictionaryEditor newDictionaryEditor = new DictionaryEditor();
            newDictionaryEditor.runOnClose(() -> {
                mAbbreviationDictionary = newDictionaryEditor.getCreatedObject();
                tempThis.setVisible(true);
            });
            tempThis.setVisible(false);
            newDictionaryEditor.setVisible(true);
        }).start();
    }//GEN-LAST:event_cmdCreateAbbreviationDictionaryActionPerformed

    private void cmdLoadAbbreviationDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadAbbreviationDictionaryActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        new Thread(() -> {
            tempThis.setVisible(false);
            int returnval = myFileChooser.showOpenDialog(tempThis);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = myFileChooser.getSelectedFile();
                InputStream DictionaryInputStream = null;
                try {
                    DictionaryInputStream = new java.io.BufferedInputStream(new FileInputStream(selectedFile));
                    mAbbreviationDictionary = new Dictionary(DictionaryInputStream);
                } catch (IOException ex) {
                    Logger.getLogger(DictionaryEditor.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (DictionaryInputStream != null) {
                        try {
                            DictionaryInputStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DictionaryEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            tempThis.setVisible(true);
        }).start();
    }//GEN-LAST:event_cmdLoadAbbreviationDictionaryActionPerformed

    private void cmdCreateSpellingDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateSpellingDictionaryActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        new Thread(() -> {
            final DictionaryEditor newDictionaryEditor = new DictionaryEditor();
            newDictionaryEditor.runOnClose(() -> {
                mSpellingDictionary = newDictionaryEditor.getCreatedObject();
                tempThis.setVisible(true);
            });
            tempThis.setVisible(false);
            newDictionaryEditor.setVisible(true);
        }).start();
    }//GEN-LAST:event_cmdCreateSpellingDictionaryActionPerformed

    private void cmdLoadSpellingDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadSpellingDictionaryActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        new Thread(() -> {
            tempThis.setVisible(false);
            int returnval = myFileChooser.showOpenDialog(tempThis);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = myFileChooser.getSelectedFile();
                InputStream DictionaryInputStream = null;
                try {
                    DictionaryInputStream = new java.io.BufferedInputStream(new FileInputStream(selectedFile));
                    mSpellingDictionary = new Dictionary(DictionaryInputStream);
                } catch (IOException ex) {
                    Logger.getLogger(DictionaryEditor.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (DictionaryInputStream != null) {
                        try {
                            DictionaryInputStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DictionaryEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            tempThis.setVisible(true);
        }).start();
    }//GEN-LAST:event_cmdLoadSpellingDictionaryActionPerformed

    private void cmdLoadTrainingFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadTrainingFileActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        new Thread(() -> {
            tempThis.setVisible(false);
            int returnval = myFileChooser.showOpenDialog(tempThis);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = myFileChooser.getSelectedFile();
                mFileCollectionListModel.add(selectedFile);
            }
            tempThis.setVisible(true);
        }).start();
    }//GEN-LAST:event_cmdLoadTrainingFileActionPerformed

    private void cmdDeleteTrainingFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdDeleteTrainingFileActionPerformed
        mFileCollectionListModel.remove(listFiles.getSelectedValue());
    }//GEN-LAST:event_cmdDeleteTrainingFileActionPerformed

    private void cmdTrainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTrainActionPerformed
        final WordSplitingTokenizerTrainer tempThis = this;
        final Charset utf8 = Charset.forName("UTF-8");
        new Thread(() -> {
            textTestResults.setText("");
            //create TokenizerFactory part of the training context
            WordSplittingTokenizerFactory myTokenizerFactory = new WordSplittingTokenizerFactory("EN", mAbbreviationDictionary, false, null, mSpellingDictionary, (TimeComplexity) comboTimeComplexity.getSelectedItem());

            Tokenizer stdTokenizer = null;
            try {
                stdTokenizer = OpenNLPUtils.createTokenizer();
            } catch (IOException ex) {
                Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Tokenizer myNonSplitingTokenizer = null;
            try {
                myNonSplitingTokenizer = OpenNLPUtils.createTokenizer(OpenNLPUtils.readTokenizerModel(OpenNLPUtils.buildModelFileStream(".\\data\\OpenNLP\\en-fiction-token.bin")));
            } catch (IOException ex) {
                Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(Level.SEVERE, null, ex);
            }
            List<FileSplit> FileSplits = FileSplit.generateFileSplitsLOO(mFileCollectionListModel);
            File trainingFile = new File("en-token.train");
            File testFile = new File("en-token.test");
            SummaryStatistics curFStats = new SummaryStatistics();
            SummaryStatistics curRecallStats = new SummaryStatistics();
            SummaryStatistics curPrecisionStats = new SummaryStatistics();
            SummaryStatistics stdFStats = new SummaryStatistics();
            SummaryStatistics stdRecallStats = new SummaryStatistics();
            SummaryStatistics stdPrecisionStats = new SummaryStatistics();
            SummaryStatistics myNonSplitFStats = new SummaryStatistics();
            SummaryStatistics myNonSplitRecallStats = new SummaryStatistics();
            SummaryStatistics myNonSplitPrecisionStats = new SummaryStatistics();
            java.io.BufferedWriter trainingFileWriter = null;
            for (FileSplit curFileSplit : FileSplits) {
                try {
                    //create training file
                    trainingFileWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(trainingFile), utf8));
                    for (File curTrainingFile : curFileSplit.getTrainingFiles()) {
                        java.io.BufferedReader curTrainingFileReader = null;
                        try {
                            Charset fileCharset = FileUtils.determineCharset(curTrainingFile);
                            if (fileCharset == null) {
                                fileCharset = utf8;
                            }
                            curTrainingFileReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(curTrainingFile), fileCharset));
                            while (curTrainingFileReader.ready()) {
                                String curLine = curTrainingFileReader.readLine();
                                trainingFileWriter.append(curLine).append("\n");
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            if (curTrainingFileReader != null) {
                                curTrainingFileReader.close();
                            }
                        }
                    }
                    trainingFileWriter.write('\n');
                } catch (IOException ex) {
                    Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (trainingFileWriter != null) {
                        try {
                            trainingFileWriter.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                //create test file
                java.io.BufferedWriter testFileWriter = null;
                try {
                    //create training file
                    testFileWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(testFile), utf8));
                    for (File curTrainingFile : curFileSplit.getTestFiles()) {
                        String testingFileName = curTrainingFile.getCanonicalPath();
                        textTestResults.setText(textTestResults.getText() + "testing with " + testingFileName + "\n");
                        java.io.BufferedReader curTrainingFileReader = null;
                        try {
                            Charset fileCharset = FileUtils.determineCharset(curTrainingFile);
                            if (fileCharset == null) {
                                fileCharset = utf8;
                            }
                            curTrainingFileReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(curTrainingFile), fileCharset));
                            while (curTrainingFileReader.ready()) {
                                String curLine = curTrainingFileReader.readLine();
                                testFileWriter.append(curLine).append("\n");
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            if (curTrainingFileReader != null) {
                                curTrainingFileReader.close();
                            }
                        }
                    }
                    testFileWriter.write('\n');
                } catch (IOException ex) {
                    Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (testFileWriter != null) {
                        try {
                            testFileWriter.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                //create and train model
                ObjectStream<String> trainingLineStream = null;
                TokenizerModel train = null;
                try {
                    trainingLineStream = new PlainTextByLineStream(new FileInputStream(trainingFile), utf8);
                    ObjectStream<TokenSample> sampleStream = null;
                    try {
                        sampleStream = new TokenSampleStream(trainingLineStream);
                        train = TokenizerME.train(sampleStream, myTokenizerFactory, TrainingParameters.defaultParams());
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
                    if (trainingLineStream != null) {
                        try {
                            trainingLineStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                if (train != null) {
                    ObjectStream<String> testingLineStream = null;
                    try {
                        testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), utf8);
                        ObjectStream<TokenSample> sampleStream = null;
                        try {
                            sampleStream = new TokenSampleStream(testingLineStream);
                            TokenizerME testDetector = new TokenizerME(train);
                            TokenizerEvaluator evaluator = new TokenizerEvaluator(testDetector);
                            evaluator.evaluate(sampleStream);
                            FMeasure testFMeasure = evaluator.getFMeasure();
                            curFStats.addValue(testFMeasure.getFMeasure());
                            curRecallStats.addValue(testFMeasure.getRecallScore());
                            curPrecisionStats.addValue(testFMeasure.getPrecisionScore());
                            textTestResults.setText(textTestResults.getText() + testFMeasure.getFMeasure() + " " + testFMeasure.getPrecisionScore() + " " + testFMeasure.getRecallScore() + "\n");
                            if (stdTokenizer != null) {
                                testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), utf8);
                                sampleStream = new TokenSampleStream(testingLineStream);
                                TokenizerEvaluator stdEvaluator = new TokenizerEvaluator(stdTokenizer);
                                stdEvaluator.evaluate(sampleStream);
                                FMeasure stdFMeasure = stdEvaluator.getFMeasure();
                                stdFStats.addValue(stdFMeasure.getFMeasure());
                                stdRecallStats.addValue(stdFMeasure.getRecallScore());
                                stdPrecisionStats.addValue(stdFMeasure.getPrecisionScore());
                                textTestResults.setText(textTestResults.getText() + " " + stdFMeasure.getFMeasure() + " " + stdFMeasure.getPrecisionScore() + " " + stdFMeasure.getRecallScore() + "\n");
                            }
                            if (myNonSplitingTokenizer != null) {
                                testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), utf8);
                                sampleStream = new TokenSampleStream(testingLineStream);
                                TokenizerEvaluator myNonSplitingEvaluator = new TokenizerEvaluator(myNonSplitingTokenizer);
                                myNonSplitingEvaluator.evaluate(sampleStream);
                                FMeasure myNonSplitFMeasure = myNonSplitingEvaluator.getFMeasure();
                                myNonSplitFStats.addValue(myNonSplitFMeasure.getFMeasure());
                                myNonSplitRecallStats.addValue(myNonSplitFMeasure.getRecallScore());
                                myNonSplitPrecisionStats.addValue(myNonSplitFMeasure.getPrecisionScore());
                                textTestResults.setText(textTestResults.getText() + " " + myNonSplitFMeasure.getFMeasure() + " " + myNonSplitFMeasure.getPrecisionScore() + " " + myNonSplitFMeasure.getRecallScore() + "\n");
                            }
                            textTestResults.setText(textTestResults.getText() + "\n");
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
            textTestResults.setText(textTestResults.getText() + "\n");
            textTestResults.setText(textTestResults.getText() + "test model\n");
            textTestResults.setText(textTestResults.getText() + "f score mean " + curFStats.getMean() + " stdDev " + curFStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "recall mean " + curRecallStats.getMean() + " stdDev " + curRecallStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "precision score mean " + curPrecisionStats.getMean() + " stdDev " + curPrecisionStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "std model\n");
            textTestResults.setText(textTestResults.getText() + "f score mean " + stdFStats.getMean() + " stdDev " + stdFStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "recall mean " + stdRecallStats.getMean() + " stdDev " + stdRecallStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "precision score mean " + stdPrecisionStats.getMean() + " stdDev " + stdPrecisionStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "my non spliting model\n");
            textTestResults.setText(textTestResults.getText() + "f score mean " + myNonSplitFStats.getMean() + " stdDev " + myNonSplitFStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "recall mean " + myNonSplitRecallStats.getMean() + " stdDev " + myNonSplitRecallStats.getStandardDeviation() + "\n");
            textTestResults.setText(textTestResults.getText() + "precision score mean " + myNonSplitPrecisionStats.getMean() + " stdDev " + myNonSplitPrecisionStats.getStandardDeviation() + "\n");
            //create combinded training file
            trainingFileWriter = null;
            try {
                trainingFileWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(new java.io.FileOutputStream(trainingFile), utf8));
                for (File curTrainingFile : mFileCollectionListModel) {
                    java.io.BufferedReader curTrainingFileReader = null;
                    try {
                        Charset fileCharset = FileUtils.determineCharset(curTrainingFile);
                        if (fileCharset == null) {
                            fileCharset = utf8;
                        }
                        curTrainingFileReader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(curTrainingFile), fileCharset));
                        while (curTrainingFileReader.ready()) {
                            String curLine = curTrainingFileReader.readLine();
                            trainingFileWriter.append(curLine).append("\n");
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (curTrainingFileReader != null) {
                            curTrainingFileReader.close();
                        }
                    }
                }
                trainingFileWriter.write('\n');
            } catch (IOException ex) {
                Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (trainingFileWriter != null) {
                    try {
                        trainingFileWriter.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            //create and train model
            ObjectStream<String> lineStream = null;
            this.createdObject = null;
            try {
                lineStream = new PlainTextByLineStream(new FileInputStream(trainingFile), utf8);
                ObjectStream<TokenSample> sampleStream = null;
                try {
                    sampleStream = new TokenSampleStream(lineStream);
                    this.createdObject = TokenizerME.train(sampleStream, myTokenizerFactory, TrainingParameters.defaultParams());
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
                if (lineStream != null) {
                    try {
                        lineStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (createdObject != null) {
                OutputStream modelOut = null;
                File modelFile = new File("en-fiction-token.bin");
                try {
                    modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
                    createdObject.serialize(modelOut);
                } catch (IOException ex) {
                    Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (modelOut != null) {
                        try {
                            modelOut.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            textTestResults.setText(textTestResults.getText() + "done");
        }).start();
    }//GEN-LAST:event_cmdTrainActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WordSplitingTokenizerTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WordSplitingTokenizerTrainer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdCreateAbbreviationDictionary;
    private javax.swing.JButton cmdCreateSpellingDictionary;
    private javax.swing.JButton cmdDeleteTrainingFile;
    private javax.swing.JButton cmdLoadAbbreviationDictionary;
    private javax.swing.JButton cmdLoadSpellingDictionary;
    private javax.swing.JButton cmdLoadTrainingFile;
    private javax.swing.JButton cmdTrain;
    private javax.swing.JComboBox<TimeComplexity> comboTimeComplexity;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<File> listFiles;
    private javax.swing.JFileChooser myFileChooser;
    private javax.swing.JTextArea textTestResults;
    // End of variables declaration//GEN-END:variables
}
