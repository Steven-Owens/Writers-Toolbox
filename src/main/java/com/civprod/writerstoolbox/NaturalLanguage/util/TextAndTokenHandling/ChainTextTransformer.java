/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling;

import com.civprod.util.ChainListWrapper;
import static com.civprod.writerstoolbox.NaturalLanguage.util.TextAndTokenHandling.ChainTokenStripper.createList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Steven Owens
 */
public class ChainTextTransformer extends ChainListWrapper<TextTransformer> implements TextTransformer {

    public ChainTextTransformer(List<TextTransformer> inTextTransformers){
        super(inTextTransformers);
    }
    
    public ChainTextTransformer(TextTransformer... inTextTransformers){
        super(inTextTransformers);
    }

    @Override
    public String transform(String inText) {
        String rString = inText;
        for (TextTransformer curTextTransformer : this.interList){
            rString = curTextTransformer.transform(rString);
        }
        return rString; 
    }
    
}
