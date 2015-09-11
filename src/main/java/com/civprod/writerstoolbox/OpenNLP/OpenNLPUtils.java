/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.CommonRegexPatterns;
import com.civprod.writerstoolbox.data.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class OpenNLPUtils {
    
    public static InputStream buildModelFileStream(String modelFilePath) throws FileNotFoundException{
        return buildModelFileStream(new File(modelFilePath));
    }
    
    public static InputStream buildModelFileStream(File modelFile) throws FileNotFoundException{
        return new java.io.BufferedInputStream(new FileInputStream(modelFile));
    }

    public static SentenceModel readSentenceModel() throws IOException {
        return readSentenceModel(OpenNLPUtils.buildModelFileStream("data\\OpenNLP\\en-sent.bin"));
    }

    public static SentenceModel readSentenceModel(InputStream modelIn) throws IOException {
        return new SentenceModel(modelIn);
    }

    public static SentenceDetector createSentenceDetector() throws IOException {
        return createSentenceDetector(readSentenceModel());
    }

    public static SentenceDetector createSentenceDetector(SentenceModel model) {
        return new ThreadSafeSentenceDetector(model);
    }

    public static TokenizerModel readTokenizerModel() throws IOException {
        return readTokenizerModel(buildModelFileStream("data\\OpenNLP\\en-token.bin"));
    }

    public static TokenizerModel readTokenizerModel(InputStream modelIn) throws IOException {
        return new TokenizerModel(modelIn);
    }

    public static Tokenizer createTokenizer() throws IOException {
        return createTokenizer(readTokenizerModel());
    }

    public static Tokenizer createTokenizer(TokenizerModel model) {
        return new ThreadSafeTokenizer(model);
    }

    public static TokenNameFinderModel readNameFinderModel(String modelFilePath) throws FileNotFoundException, IOException {
        return readNameFinderModel(buildModelFileStream(modelFilePath));
    }

    public static TokenNameFinderModel readNameFinderModel(InputStream modelIn) throws IOException {
        return new TokenNameFinderModel(modelIn);
    }
    
    public static TokenNameFinder createNameFinder(TokenNameFinderModel inTokenNameFinderModel){
        return new ThreadSafeNameFinder(inTokenNameFinderModel);
    }

    public static POSModel readPOSModel() throws IOException {
        return readPOSModel(buildModelFileStream("data\\OpenNLP\\en-pos-maxent.bin"));
    }

    public static POSModel readPOSModel(InputStream modelIn) throws IOException {
        return new POSModel(modelIn);
    }

    public static POSTagger createPOSTagger() throws IOException {
        return createPOSTagger(readPOSModel());
    }

    public static POSTagger createPOSTagger(POSModel model) {
        return new ThreadSafePOSTagger(model);
    }

    public static ChunkerModel readChunkerModel() throws FileNotFoundException, IOException {
        return readChunkerModel(buildModelFileStream("data\\OpenNLP\\en-chunker.bin"));
    }

    public static ChunkerModel readChunkerModel(InputStream modelIn) throws IOException {
        return new ChunkerModel(modelIn);
    }
    
    public static Chunker createChunker() throws IOException{
        return createChunker(readChunkerModel());
    }
    
    public static Chunker createChunker(ChunkerModel model){
        return new ThreadSafeChunker(model);
    }
    
    public static ParserModel readParserModel() throws IOException{
        return readParserModel(buildModelFileStream("data\\OpenNLP\\en-parser-chunking.bin"));
    }
    
    public static ParserModel readParserModel(InputStream modelIn) throws IOException {
        return new ParserModel(modelIn);
    }
    
    public static Parser createParser() throws IOException{
        return createParser(readParserModel());
    }
    
    public static Parser createParser(ParserModel inParserModel){
        return ParserFactory.create(inParserModel);
    }
    
    public static String walkDownParse(Parse curRootParse) {
        if (curRootParse == null){
            return "";
        }
        int childCount = curRootParse.getChildCount();
        Parse[] children = curRootParse.getChildren();
        String coveredText = curRootParse.getCoveredText();
        String label = curRootParse.getLabel();
        String type = curRootParse.getType();
        if (!"TK".equalsIgnoreCase(type)) {
            StringBuilder rValueBuilder = new StringBuilder(childCount * 3);
            rValueBuilder.append("(").append(type);
            if ((label != null) && (!CommonRegexPatterns.allWhiteSpacePattern.matcher(label).matches())){
                rValueBuilder.append("-").append(label);
            }
            rValueBuilder.append(" ");
            for (Parse curChildParse : children) {
                rValueBuilder.append(walkDownParse(curChildParse));
            }
            rValueBuilder.append(")");
            return rValueBuilder.toString();
        } else {
            return coveredText;
        }
    }
    
}
