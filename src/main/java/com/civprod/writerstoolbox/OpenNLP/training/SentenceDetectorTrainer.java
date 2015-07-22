/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.training;

import com.civprod.swing.CollectionListModel;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
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
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorEvaluator;
import opennlp.tools.sentdetect.SentenceDetectorFactory;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.eval.FMeasure;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Steven Owens
 */
public class SentenceDetectorTrainer extends EditorWindow<SentenceModel> {

    private Dictionary mAbbreviationDictionary = null;
    private CollectionListModel<File> mFileCollectionListModel;

    /**
     * Creates new form SentenceDetectorTrainer
     */
    public SentenceDetectorTrainer() {
        initComponents();
        mFileCollectionListModel = new CollectionListModel<>();
        listFiles.setModel(mFileCollectionListModel);
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
        txtEosChars = new javax.swing.JTextField();
        cbUseTokenEnd = new javax.swing.JCheckBox();
        cmdTrainSentenceDetector = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList<File>();
        cmdLoadTrainingFile = new javax.swing.JButton();
        cmdDeleteTrainingFile = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textTestResults = new javax.swing.JTextArea();

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

        txtEosChars.setText(".!?");
        txtEosChars.setMinimumSize(new java.awt.Dimension(22, 22));
        txtEosChars.setPreferredSize(new java.awt.Dimension(40, 22));

        cbUseTokenEnd.setText("useTokenEnd");

        cmdTrainSentenceDetector.setText("train Sentence Detector");
        cmdTrainSentenceDetector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdTrainSentenceDetectorActionPerformed(evt);
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

        textTestResults.setColumns(20);
        textTestResults.setRows(5);
        jScrollPane2.setViewportView(textTestResults);

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
                            .addComponent(txtEosChars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbUseTokenEnd)
                            .addComponent(cmdTrainSentenceDetector)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmdLoadTrainingFile)
                                    .addComponent(cmdDeleteTrainingFile))))
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
                .addComponent(txtEosChars, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbUseTokenEnd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdLoadTrainingFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdDeleteTrainingFile)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmdTrainSentenceDetector)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdCreateAbbreviationDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdCreateAbbreviationDictionaryActionPerformed
        final SentenceDetectorTrainer tempThis = this;
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
        final SentenceDetectorTrainer tempThis = this;
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

    private void cmdLoadTrainingFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadTrainingFileActionPerformed
        final SentenceDetectorTrainer tempThis = this;
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

    private void cmdTrainSentenceDetectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdTrainSentenceDetectorActionPerformed
        final SentenceDetectorTrainer tempThis = this;
        new Thread(() -> {
            textTestResults.setText("");
            Charset charset = Charset.forName("UTF-8");
            //read other models
            SentenceDetector stdDetector = null;
            try {
                stdDetector = OpenNLPUtils.createSentenceDetector();

            } catch (IOException ex) {
            }

            List<FileSplit> FileSplits = FileSplit.generateFileSplitsLOO(mFileCollectionListModel);
            File trainingFile = new File("en-sent.train");
            File testFile = new File("en-sent.test");
            SummaryStatistics curFStats = new SummaryStatistics();
            SummaryStatistics curRecallStats = new SummaryStatistics();
            SummaryStatistics curPrecisionStats = new SummaryStatistics();
            SummaryStatistics stdFStats = new SummaryStatistics();
            SummaryStatistics stdRecallStats = new SummaryStatistics();
            SummaryStatistics stdPrecisionStats = new SummaryStatistics();
            java.io.BufferedOutputStream trainingFileWriter = null;
            for (FileSplit curFileSplit : FileSplits) {
                try {
                    //create training file
                    trainingFileWriter = new java.io.BufferedOutputStream(new java.io.FileOutputStream(trainingFile));
                    for (File curTrainingFile : curFileSplit.getTrainingFiles()) {
                        java.io.BufferedInputStream curTrainingFileReader = null;
                        try {
                            curTrainingFileReader = new java.io.BufferedInputStream(new java.io.FileInputStream(curTrainingFile));
                            while (curTrainingFileReader.available() > 0) {
                                trainingFileWriter.write(curTrainingFileReader.read());
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
                java.io.BufferedOutputStream testFileWriter = null;
                try {
                    //create training file
                    testFileWriter = new java.io.BufferedOutputStream(new java.io.FileOutputStream(testFile));
                    for (File curTrainingFile : curFileSplit.getTestFiles()) {
                        String testingFileName = curTrainingFile.getCanonicalPath();
                        textTestResults.setText(textTestResults.getText() + "testing with " + testingFileName + "\n");
                        java.io.BufferedInputStream curTrainingFileReader = null;
                        try {
                            curTrainingFileReader = new java.io.BufferedInputStream(new java.io.FileInputStream(curTrainingFile));
                            while (curTrainingFileReader.available() > 0) {
                                int read = curTrainingFileReader.read();
                                testFileWriter.write(read);
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
                //create SentenceDetectorFactory part of the training context
                SentenceDetectorFactory mySentenceDetectorFactory = new SentenceDetectorFactory("EN", cbUseTokenEnd.isSelected(), mAbbreviationDictionary, txtEosChars.getText().toCharArray());
                
                ObjectStream<String> trainingLineStream = null;
                SentenceModel train = null;
                try {
                    trainingLineStream = new PlainTextByLineStream(new FileInputStream(trainingFile), charset);
                    ObjectStream<SentenceSample> sampleStream = null;
                    try {
                        sampleStream = new SentenceSampleStream(trainingLineStream);
                        train = SentenceDetectorME.train("EN", sampleStream, mySentenceDetectorFactory, TrainingParameters.defaultParams());
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
                trainingLineStream = null;
                if (train != null) {
                    ObjectStream<String> testingLineStream = null;
                    try {
                        testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), charset);
                        ObjectStream<SentenceSample> sampleStream = null;
                        try {
                            sampleStream = new SentenceSampleStream(testingLineStream);
                            SentenceDetectorME testDetector = new SentenceDetectorME(train);
                            SentenceDetectorEvaluator evaluator = new SentenceDetectorEvaluator(testDetector);
                            evaluator.evaluate(sampleStream);
                            FMeasure testFMeasure = evaluator.getFMeasure();
                            curFStats.addValue(testFMeasure.getFMeasure());
                            curRecallStats.addValue(testFMeasure.getRecallScore());
                            curPrecisionStats.addValue(testFMeasure.getPrecisionScore());
                            textTestResults.setText(textTestResults.getText() + testFMeasure.getFMeasure() + " " + testFMeasure.getPrecisionScore() + " " + testFMeasure.getRecallScore() + "\n");
                            if (stdDetector != null) {
                                testingLineStream = new PlainTextByLineStream(new FileInputStream(testFile), charset);
                                sampleStream = new SentenceSampleStream(testingLineStream);
                                SentenceDetectorEvaluator stdEvaluator = new SentenceDetectorEvaluator(stdDetector);
                                stdEvaluator.evaluate(sampleStream);
                                FMeasure stdFMeasure = stdEvaluator.getFMeasure();
                            stdFStats.addValue(stdFMeasure.getFMeasure());
                            stdRecallStats.addValue(stdFMeasure.getRecallScore());
                            stdPrecisionStats.addValue(stdFMeasure.getPrecisionScore());
                            textTestResults.setText(textTestResults.getText() + " " + stdFMeasure.getFMeasure() + " " + stdFMeasure.getPrecisionScore() + " " + stdFMeasure.getRecallScore()  + "\n");
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
            //create combinded training file
            trainingFileWriter = null;
            try {
                trainingFileWriter = new java.io.BufferedOutputStream(new java.io.FileOutputStream(trainingFile));
                for (File curTrainingFile : mFileCollectionListModel) {
                    java.io.BufferedInputStream curTrainingFileReader = null;
                    try {
                        curTrainingFileReader = new java.io.BufferedInputStream(new java.io.FileInputStream(curTrainingFile));
                        while (curTrainingFileReader.available() > 0) {
                            trainingFileWriter.write(curTrainingFileReader.read());
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
            //create SentenceDetectorFactory part of the training context
            SentenceDetectorFactory mySentenceDetectorFactory = new SentenceDetectorFactory("EN", cbUseTokenEnd.isSelected(), mAbbreviationDictionary, txtEosChars.getText().toCharArray());
            //create and train model
            ObjectStream<String> lineStream = null;
            this.createdObject = null;
            try {
                lineStream = new PlainTextByLineStream(new FileInputStream(trainingFile), charset);
                ObjectStream<SentenceSample> sampleStream = null;
                try {
                    sampleStream = new SentenceSampleStream(lineStream);
                    this.createdObject = SentenceDetectorME.train("EN", sampleStream, mySentenceDetectorFactory, TrainingParameters.defaultParams());
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
                File modelFile = new File("en-fiction-sent.bin");
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
    }//GEN-LAST:event_cmdTrainSentenceDetectorActionPerformed

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
            java.util.logging.Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SentenceDetectorTrainer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SentenceDetectorTrainer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox cbUseTokenEnd;
    private javax.swing.JButton cmdCreateAbbreviationDictionary;
    private javax.swing.JButton cmdDeleteTrainingFile;
    private javax.swing.JButton cmdLoadAbbreviationDictionary;
    private javax.swing.JButton cmdLoadTrainingFile;
    private javax.swing.JButton cmdTrainSentenceDetector;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<File> listFiles;
    private javax.swing.JFileChooser myFileChooser;
    private javax.swing.JTextArea textTestResults;
    private javax.swing.JTextField txtEosChars;
    // End of variables declaration//GEN-END:variables
}
