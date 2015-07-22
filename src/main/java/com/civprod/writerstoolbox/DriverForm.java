/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox;

import com.civprod.writerstoolbox.NaturalLanguage.util.BreakIteratorSentenceDetector;
import com.civprod.writerstoolbox.NaturalLanguage.util.Chunker;
import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.NaturalLanguage.util.RegexStringTokenizer;
import com.civprod.writerstoolbox.NaturalLanguage.util.POSTagger;
import com.civprod.writerstoolbox.NaturalLanguage.util.SentenceDetector;
import com.civprod.writerstoolbox.NaturalLanguage.util.StringTokenizer;
import com.civprod.writerstoolbox.OpenNLP.ChunkerWrapper;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import com.civprod.writerstoolbox.OpenNLP.POSTaggerWrapper;
import com.civprod.writerstoolbox.OpenNLP.SentenceDetectorWrapper;
import com.civprod.writerstoolbox.OpenNLP.StringTokenizerWrapper;
import com.civprod.writerstoolbox.apps.Context;
import com.civprod.writerstoolbox.apps.ToolSuite;
import com.civprod.writerstoolbox.data.*;
import com.civprod.writerstoolbox.data.component.ChunkerComponent;
import com.civprod.writerstoolbox.data.component.ChunkerComponentFactory;
import com.civprod.writerstoolbox.data.component.NamesComponent;
import com.civprod.writerstoolbox.data.component.NamesComponentFactory;
import com.civprod.writerstoolbox.data.component.PartOfSpeechComponent;
import com.civprod.writerstoolbox.data.component.PartOfSpeechComponentFactory;
import com.civprod.writerstoolbox.data.io.localFS.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

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
        
        //TODO: read this from a config file
        System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict\\"); 
        
        appContext = new Context.Builder().setModifiable(false).build();
        ToolSuite toolSuite = appContext.getToolSuite();
        try {
            final SentenceDetector sentenceDetector = new SentenceDetectorWrapper(OpenNLPUtils.createSentenceDetector(OpenNLPUtils.readSentenceModel(OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-fiction-sent.bin"))));
            toolSuite.addTool(sentenceDetector);
            toolSuite.addTool("primary sentence detector", sentenceDetector);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Pattern> defualtApplyOrder = RegexStringTokenizer.getDefualtApplyOrder();
        Map<Pattern, Pattern> defualtIgnoreMapping = RegexStringTokenizer.getDefualtIgnoreMapping();
        Pattern evilLaughPattern = java.util.regex.Pattern.compile("(M|m)wa(ha)+~");
        defualtApplyOrder.add(defualtApplyOrder.indexOf(CommonRegexPatterns.dashedWord), evilLaughPattern);
        defualtIgnoreMapping.put(evilLaughPattern, evilLaughPattern);
        StringTokenizer createTokenizer = new RegexStringTokenizer(defualtApplyOrder, defualtIgnoreMapping);
        toolSuite.addTool(createTokenizer);
        toolSuite.addTool("regex tokenizer",createTokenizer);
        
        try {
            StringTokenizer mStringTokenizer = new StringTokenizerWrapper(
                    OpenNLPUtils.createTokenizer(
                            OpenNLPUtils.readTokenizerModel(
                                    OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-fiction-split-token.bin"))));
            toolSuite.addTool("alpha number spliter tokenizer", mStringTokenizer);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            StringTokenizer mStringTokenizer = new StringTokenizerWrapper(
                    OpenNLPUtils.createTokenizer(
                            OpenNLPUtils.readTokenizerModel(
                                    OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-fiction-token.bin"))));
            toolSuite.addTool(mStringTokenizer);
            toolSuite.addTool("primary tokenizer", mStringTokenizer);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            POSTagger mPOSTagger = new POSTaggerWrapper(OpenNLPUtils.createPOSTagger());
            toolSuite.addTool(mPOSTagger);
            toolSuite.addTool("primary part of speech tagger", mPOSTagger);
            PartOfSpeechComponentFactory mPartOfSpeechComponentFactory = new PartOfSpeechComponentFactory(mPOSTagger);
            mPartOfSpeechComponentFactory.setStringTokenizer(createTokenizer);
            toolSuite.addTool(mPartOfSpeechComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Chunker mChunker = new ChunkerWrapper(OpenNLPUtils.createChunker());
            toolSuite.addTool(mChunker);
            toolSuite.addTool("primary chunker",mChunker);
            ChunkerComponentFactory mChunkerComponentFactory = new ChunkerComponentFactory(mChunker);
            mChunkerComponentFactory.setStringTokenizer(createTokenizer);
            mChunkerComponentFactory.setPartOfSpeechComponentFactory(toolSuite.getTool(PartOfSpeechComponentFactory.class));
            toolSuite.addTool(mChunkerComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            TokenNameFinderModel readNameFinderModel = OpenNLPUtils.readNameFinderModel("data\\OpenNLP\\en-ner-person.bin");
            NameFinderME nameFinder = new NameFinderME(readNameFinderModel);
            toolSuite.addTool(nameFinder);
            toolSuite.addTool("primary person name finder",nameFinder);
            NamesComponentFactory newNamesComponentFactory = new NamesComponentFactory(nameFinder);
            toolSuite.addTool(newNamesComponentFactory);
        } catch (IOException ex) {
            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
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
        cmdLoadRawFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtOut = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        cmdLoadRawFile.setText("load raw file");
        cmdLoadRawFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdLoadRawFileActionPerformed(evt);
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdLoadRawFile)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cmdLoadRawFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdLoadRawFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdLoadRawFileActionPerformed
        final DriverForm tempthis = this;
        Charset utf8 = Charset.forName("UTF-8");
        new Thread(() -> {
            txtOut.setText("");
            int returnval = myFileChooser.showOpenDialog(tempthis);
            if (returnval == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = myFileChooser.getSelectedFile();
                txtOut.setText(txtOut.getText() + "File Selected\n");
                InputStream modelIn = null;
                java.io.BufferedReader fin = null;
                try {
                    txtOut.setText(txtOut.getText() + "reading selected file\n");
                    Document<?> readDocument = FileUtils.readDocument(selectedFile,appContext);
                    
                    txtOut.setText(txtOut.getText() + "reading std OpenNLP sentence model\n");
                    final SentenceDetector oldSentenceDetector = new SentenceDetectorWrapper(OpenNLPUtils.createSentenceDetector());
                    txtOut.setText(txtOut.getText() + "running std OpenNLP sentence detector\n");
                    readDocument.getParagraphs().stream()
                            .forEach((Paragraph<?> curParagraph) -> curParagraph.buildSentences(oldSentenceDetector));
                    java.io.BufferedWriter fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing std OpenNLP sentences\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_found_sentences_old.txt"),utf8));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(oldSentenceDetector)) {
                                fout.write(curSentence.getContent());
                                fout.newLine();
                            }
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing std OpenNLP  sentences\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    txtOut.setText(txtOut.getText() + "creating BreakIterator sentence detector\n");
                    final SentenceDetector javaSentenceDetector = new BreakIteratorSentenceDetector();
                    txtOut.setText(txtOut.getText() + "running BreakIterator sentence detector\n");
                    readDocument.getParagraphs().stream()
                            .forEach((Paragraph<?> curParagraph) -> curParagraph.buildSentences(javaSentenceDetector));
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing BreakIterator sentences\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_found_sentences_java.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(javaSentenceDetector)) {
                                fout.write(curSentence.getContent());
                                fout.newLine();
                            }
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing BreakIterator  sentences\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    txtOut.setText(txtOut.getText() + "reading sentence model\n");
                    final SentenceDetector sentenceDetector = appContext.getToolSuite().getTool(SentenceDetector.class);
                    txtOut.setText(txtOut.getText() + "running sentence detector\n");
                    readDocument.getParagraphs().stream()
                            .forEach((Paragraph<?> curParagraph) -> curParagraph.buildSentences(sentenceDetector));
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing sentences\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_found_sentences.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                                fout.write(curSentence.getContent());
                                fout.newLine();
                            }
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing sentences\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    //StringTokenizer createTokenizer = new StringTokenizerWrapper(OpenNLPUtils.createTokenizer());
                    StringTokenizer createTokenizer = appContext.getToolSuite().getTool(StringTokenizer.class);
                    readDocument.getParagraphs().stream().forEach((Paragraph<?> curParagraph) -> {
                        curParagraph.getSentences(sentenceDetector).stream().forEach((Sentence curSentence) -> {
                            curSentence.createTokensIfEmpty(createTokenizer);
                        });
                    });
                    /*List<Pattern> whitespaceApplyOrder = new ArrayList<>(2);
                    Map<Pattern, Pattern> whitespaceIgnoreMapping = new HashMap<>(1);
                    Set<Pattern> whitespaceRemoveSet = new HashSet<>(1);
                    whitespaceApplyOrder.add(CustomRegexStringTokenizer.XMLTagPattern);
                    whitespaceApplyOrder.add(CustomRegexStringTokenizer.WhiteSpacePattern);
                    whitespaceIgnoreMapping.put(CustomRegexStringTokenizer.XMLTagPattern, CustomRegexStringTokenizer.XMLTagPattern);
                    whitespaceRemoveSet.add(CustomRegexStringTokenizer.WhiteSpacePattern);
                    StringTokenizer WhitespaceTokenizer = new CustomRegexStringTokenizer(whitespaceApplyOrder,whitespaceIgnoreMapping,whitespaceRemoveSet);*/
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing tokens\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_found_tokens.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                                List<String> whitespaceTokens = java.util.Arrays.asList(opennlp.tools.tokenize.WhitespaceTokenizer.INSTANCE.tokenize(curSentence.getContent()));
                                List<String> learnedTokens = curSentence.getTokens();
                                int whitespaceIndex = 0;
                                int learnedIndex = 0;
                                int indexIntoWhitespaceToken = 0;
                                int indexIntoLearnedToken = 0;
                                while ((whitespaceIndex < whitespaceTokens.size()) && (learnedIndex < learnedTokens.size())) {
                                    String curWhitespaceToken = whitespaceTokens.get(whitespaceIndex);
                                    String curLearnedToken = learnedTokens.get(learnedIndex);
                                    if (indexIntoWhitespaceToken > 0) {
                                        fout.append("<SPLIT>");
                                    } else if ((whitespaceIndex > 0) && (indexIntoWhitespaceToken == 0)) {
                                        fout.append(" ");
                                    }
                                    while ((indexIntoWhitespaceToken < curWhitespaceToken.length()) && (indexIntoLearnedToken < curLearnedToken.length())) {
                                        if (curWhitespaceToken.charAt(indexIntoWhitespaceToken) == curLearnedToken.charAt(indexIntoLearnedToken)) {
                                            fout.append(curLearnedToken.charAt(indexIntoLearnedToken));
                                            indexIntoWhitespaceToken++;
                                            indexIntoLearnedToken++;
                                        } else {
                                            if (java.lang.Character.isWhitespace(curWhitespaceToken.charAt(indexIntoWhitespaceToken))) {
                                                indexIntoWhitespaceToken++;
                                            } else {
                                                indexIntoLearnedToken++;
                                            }
                                        }
                                    }
                                    if (indexIntoLearnedToken >= curLearnedToken.length()) {
                                        learnedIndex++;
                                        indexIntoLearnedToken = 0;
                                    }
                                    if (indexIntoWhitespaceToken >= curWhitespaceToken.length()) {
                                        whitespaceIndex++;
                                        indexIntoWhitespaceToken = 0;
                                    }
                                }
                                fout.newLine();
                            }
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing tokens\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing tokenize file\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_tokenize_file.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                                StringBuilder sOut = new StringBuilder();
                                for (String curToken : curSentence.getTokens()) {
                                    if (sOut.length() > 0) {
                                        sOut.append(" ");
                                    }
                                    sOut.append(curToken);
                                }
                                fout.append(sOut.toString());
                                fout.newLine();
                            }
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done tokenize file\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing tokenize pargraph file\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_tokenize_pargraph_file.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                           fout.append(curParagraph.getSentences().parallelStream()
                                            .filter((Object curItem) -> curItem instanceof Sentence)
                                            .map((Object curItem) -> (Sentence)curItem)
                                            .map((Sentence curSentence) -> curSentence.getTokens().parallelStream().collect(Collectors.joining(" ")))
                                            .collect(Collectors.joining(" ")));
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done tokenize pargraph file\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    txtOut.setText(txtOut.getText() + "part of speech tagging\n");
                    PartOfSpeechComponentFactory mPartOfSpeechComponentFactory = appContext.getToolSuite().getTool(PartOfSpeechComponentFactory.class);
                    readDocument.getParagraphs().parallelStream()
                            .forEach((Paragraph<?> curParagraph) -> {
                                curParagraph.getSentences(sentenceDetector).parallelStream()
                                        .forEach((Sentence curSentence) -> {
                                            curSentence.createTokensIfEmpty(createTokenizer);
                                            curSentence.addComponent(mPartOfSpeechComponentFactory);
                                        });
                            });
                    
                    try {
                        txtOut.setText(txtOut.getText() + "writing part of speech tags file\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_POS_tags.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                                StringBuilder sOut = new StringBuilder();
                                List<String> tokens = curSentence.getStrippedTokens(createTokenizer);
                                List<String> posTags = curSentence.getComponent(PartOfSpeechComponent.class).getPOSTags();
                                for (int i = 0; i < tokens.size(); i++) {
                                    if (sOut.length() > 0) {
                                        sOut.append(" ");
                                    }
                                    sOut.append(tokens.get(i));
                                    sOut.append('_');
                                    sOut.append(posTags.get(i));
                                }
                                fout.append(sOut.toString());
                                fout.newLine();
                            }
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done POS file\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    txtOut.setText(txtOut.getText() + "chunking\n");
                    ChunkerComponentFactory mChunkerComponentFactory = appContext.getToolSuite().getTool(ChunkerComponentFactory.class);
                    readDocument.getParagraphs().parallelStream()
                            .forEach((Paragraph<?> curParagraph) -> {
                                curParagraph.getSentences(sentenceDetector).parallelStream()
                                        .forEach((Sentence curSentence) -> {
                                            curSentence.createTokensIfEmpty(createTokenizer);
                                            curSentence.addComponent(mChunkerComponentFactory);
                                        });
                            });
                    
                    try {
                        txtOut.setText(txtOut.getText() + "writing chunking file\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_chunks.txt"))));
                        for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                            for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                                StringBuilder sOut = new StringBuilder();
                                List<String> tokens = curSentence.getStrippedTokens(createTokenizer);
                                List<String> posTags = curSentence.getComponent(PartOfSpeechComponent.class).getPOSTags();
                                List<String> chunks = curSentence.getComponent(ChunkerComponent.class).getChunks();
                                for (int i = 0; i < tokens.size(); i++) {
                                    sOut.append(String.format("%1$-29s", tokens.get(i)));
                                    sOut.append(' ');
                                    sOut.append(String.format("%1$-5s", posTags.get(i)));
                                    sOut.append(' ');
                                    sOut.append(chunks.get(i));
                                    sOut.append("\n");
                                }
                                sOut.append("\n");
                                fout.append(sOut.toString());
                                fout.newLine();
                            }
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing chunking file\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    
                    //ParserTool.parseLine(null, null, returnval)
                    //NameFinderME nameFinder = appContext.getToolSuite().getTool(NameFinderME.class);
                    NamesComponentFactory newNamesComponentFactory = appContext.getToolSuite().getTool(NamesComponentFactory.class);
                    java.util.Set<String> names = new java.util.HashSet<>(3);
                    for (Paragraph<?> curParagraph : readDocument.getParagraphs()) {
                        for (Sentence curSentence : curParagraph.getSentences(sentenceDetector)) {
                            List<String> learnedTokens = curSentence.getTokens();
                            curSentence.addComponent(newNamesComponentFactory);
                            List<Span> find = curSentence.getComponent(NamesComponent.class).getNames();
                            for (Span curSpan : find) {
                                names.add(learnedTokens.subList(curSpan.getStart(), curSpan.getEnd()).stream().collect(Collectors.joining(" ")));
                            }
                        }
                    }
                    fout = null;
                    try {
                        txtOut.setText(txtOut.getText() + "writing names\n");
                        fout = new java.io.BufferedWriter(
                                new java.io.OutputStreamWriter(
                                        new java.io.BufferedOutputStream(
                                                new java.io.FileOutputStream(readDocument.getUploadFileName() + "_found_names.txt"))));
                        for (String curName : names) {
                            fout.write(curName);
                            fout.newLine();
                        }
                        fout.flush();
                        txtOut.setText(txtOut.getText() + "done writing names\n");
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (fout != null) {
                            try {
                                fout.close();
                            } catch (IOException ex) {
                                Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (modelIn != null) {
                        try {
                            modelIn.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (fin != null) {
                        try {
                            fin.close();
                        } catch (IOException ex) {
                            Logger.getLogger(DriverForm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            txtOut.setText(txtOut.getText() + "done\n");
        }).start();
    }//GEN-LAST:event_cmdLoadRawFileActionPerformed

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
    private javax.swing.JButton cmdLoadRawFile;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JFileChooser myFileChooser;
    private javax.swing.JTextArea txtOut;
    // End of variables declaration//GEN-END:variables
}
