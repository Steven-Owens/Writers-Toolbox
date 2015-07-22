/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.testarea;

import com.civprod.writerstoolbox.NaturalLanguage.util.Chunker;
import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.POSTagger;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.SentenceDetector;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.OpenNLP.ChunkerWrapper;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import com.civprod.writerstoolbox.OpenNLP.POSTaggerWrapper;
import com.civprod.writerstoolbox.OpenNLP.SentenceDetectorWrapper;
import com.civprod.writerstoolbox.apps.Context;
import com.civprod.writerstoolbox.apps.ToolSuite;
import com.civprod.writerstoolbox.data.Document;
import com.civprod.writerstoolbox.data.component.ChunkerComponentFactory;
import com.civprod.writerstoolbox.data.component.NamesComponentFactory;
import com.civprod.writerstoolbox.data.component.PartOfSpeechComponentFactory;
import com.civprod.writerstoolbox.data.io.localFS.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;

/**
 *
 * @author Steven Owens
 */
public class DriverForm extends javax.swing.JFrame {

    final Context appContext;
    /**
     * Creates new form DriverForm
     */
    public DriverForm() {
        initComponents();
        appContext = new Context.Builder().setModifiable(false).build();
        ToolSuite toolSuite = appContext.getToolSuite();
        try {
            final SentenceDetector sentenceDetector = new SentenceDetectorWrapper(OpenNLPUtils.createSentenceDetector(OpenNLPUtils.readSentenceModel(OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-fiction-sent.bin"))));
            toolSuite.addTool(sentenceDetector);
        } catch (IOException ex) {
            Logger.getLogger(com.civprod.writerstoolbox.DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Pattern> defualtApplyOrder = RegexStringTokenizer.getDefualtApplyOrder();
        Map<Pattern, Pattern> defualtIgnoreMapping = RegexStringTokenizer.getDefualtIgnoreMapping();
        Pattern evilLaughPattern = java.util.regex.Pattern.compile("(M|m)wa(ha)+~");
        defualtApplyOrder.add(defualtApplyOrder.indexOf(CommonRegexPatterns.dashedWord), evilLaughPattern);
        defualtIgnoreMapping.put(evilLaughPattern, evilLaughPattern);
        StringTokenizer createTokenizer = new RegexStringTokenizer(defualtApplyOrder, defualtIgnoreMapping);
        toolSuite.addTool(createTokenizer);
        
        try {
            POSTagger mPOSTagger = new POSTaggerWrapper(OpenNLPUtils.createPOSTagger());
            toolSuite.addTool(mPOSTagger);
            PartOfSpeechComponentFactory mPartOfSpeechComponentFactory = new PartOfSpeechComponentFactory(mPOSTagger);
            mPartOfSpeechComponentFactory.setStringTokenizer(createTokenizer);
            toolSuite.addTool(mPartOfSpeechComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(com.civprod.writerstoolbox.DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Chunker mChunker = new ChunkerWrapper(OpenNLPUtils.createChunker());
            toolSuite.addTool(mChunker);
            ChunkerComponentFactory mChunkerComponentFactory = new ChunkerComponentFactory(mChunker);
            mChunkerComponentFactory.setStringTokenizer(createTokenizer);
            mChunkerComponentFactory.setPartOfSpeechComponentFactory(toolSuite.getTool(PartOfSpeechComponentFactory.class));
            toolSuite.addTool(mChunkerComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(com.civprod.writerstoolbox.DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            TokenNameFinderModel readNameFinderModel = OpenNLPUtils.readNameFinderModel("data\\OpenNLP\\en-ner-person.bin");
            NameFinderME nameFinder = new NameFinderME(readNameFinderModel);
            toolSuite.addTool(nameFinder);
            NamesComponentFactory newNamesComponentFactory = new NamesComponentFactory(nameFinder);
            toolSuite.addTool(newNamesComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(com.civprod.writerstoolbox.DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        toolSuite.setSealed(true);
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
        cmdLoadFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOut = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("test area");

        cmdLoadFile.setText("load File");
        cmdLoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadFileActionPerformed(evt);
            }
        });

        txtOut.setColumns(20);
        txtOut.setRows(5);
        jScrollPane1.setViewportView(txtOut);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 671, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdLoadFile))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdLoadFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadFileActionPerformed
        final DriverForm tempthis = this;
        new Thread(() -> {
            txtOut.setText("");
            int returnval = myFileChooser.showOpenDialog(tempthis);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = myFileChooser.getSelectedFile();
                try {
                    Document<?> readDocument = FileUtils.readDocument(selectedFile,appContext);
                    final SentenceDetector sentenceDetector = appContext.getToolSuite().getTool(SentenceDetector.class);
                    StringTokenizer createTokenizer = appContext.getToolSuite().getTool(StringTokenizer.class);
                    List<List<String>> segmentedFile = UnsupervisedDiscourseSegmentation.segment(readDocument, sentenceDetector, createTokenizer);
                    String collect = segmentedFile.parallelStream()
                            .map((List<String> curSegment) -> curSegment.parallelStream().collect(Collectors.joining(" ")))
                            .collect(Collectors.joining("\n<hr>\n"));
                    txtOut.setText(collect);
                } catch (IOException ex) {
                    Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }//GEN-LAST:event_cmdLoadFileActionPerformed

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
            java.util.logging.Logger.getLogger(DriverForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DriverForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DriverForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DriverForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DriverForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cmdLoadFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFileChooser myFileChooser;
    private javax.swing.JTextArea txtOut;
    // End of variables declaration//GEN-END:variables
}
