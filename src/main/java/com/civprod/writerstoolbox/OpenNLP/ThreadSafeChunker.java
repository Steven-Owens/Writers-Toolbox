/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.ThreadSafe;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.chunker.Chunker;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.util.Sequence;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class ThreadSafeChunker implements Chunker, Wrapper<ChunkerME>, ThreadSafe {
    
    private final ChunkerModel mChunkerModel;
    
    public ThreadSafeChunker(ChunkerModel inChunkerModel) {
        mChunkerModel = inChunkerModel;
    }
    
    public final ThreadLocal<ChunkerME> localChunker = new ThreadLocal<ChunkerME>() {
        protected ChunkerME initialValue() {
            return new ChunkerME(mChunkerModel);
        }
    };

    @Override
    @Deprecated
    public List<String> chunk(List<String> tokens, List<String> tags) {
        return localChunker.get().chunk(tokens, tags);
    }

    @Override
    public String[] chunk(String[] tokens, String[] tags) {
        return localChunker.get().chunk(tokens, tags);
    }

    @Override
    public Span[] chunkAsSpans(String[] tokens, String[] tags) {
        return localChunker.get().chunkAsSpans(tokens, tags);
    }

    @Override
    @Deprecated
    public Sequence[] topKSequences(List<String> tokens, List<String> tags) {
        return localChunker.get().topKSequences(tokens, tags);
    }

    @Override
    public Sequence[] topKSequences(String[] tokens, String[] tags) {
        return localChunker.get().topKSequences(tokens, tags);
    }

    @Override
    public Sequence[] topKSequences(String[] tokens, String[] tags, double minSequenceScore) {
        return localChunker.get().topKSequences(tokens, tags, minSequenceScore);
    }
    
    public ChunkerME getLocalChunkerME(){
        return localChunker.get();
    }

    @Override
    public ChunkerME getInterObject() {
        return localChunker.get();
    }
    
}
