/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP;

import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.List;
import opennlp.tools.chunker.Chunker;

/**
 *
 * @author Steven Owens
 */
public class ChunkerWrapper implements com.civprod.writerstoolbox.NaturalLanguage.util.Chunker, Wrapper<opennlp.tools.chunker.Chunker> {
    private final opennlp.tools.chunker.Chunker mChunker;
    
    public ChunkerWrapper(opennlp.tools.chunker.Chunker inChunker){
        mChunker = inChunker;
    }

    /**
     * @return the mChunker
     */
    public opennlp.tools.chunker.Chunker getChunker() {
        return mChunker;
    }

    @Override
    public List<String> chunk(List<String> tokens, List<String> tags) {
        return java.util.Arrays.asList(mChunker.chunk(tokens.toArray(new String[tokens.size()]), tags.toArray(new String[tags.size()])));
    }

    @Override
    public Chunker getInterObject() {
        return mChunker;
    }
    
    
}
