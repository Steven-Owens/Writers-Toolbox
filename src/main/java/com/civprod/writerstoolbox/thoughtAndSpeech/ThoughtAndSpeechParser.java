/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.data.Paragraph;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public interface ThoughtAndSpeechParser {
    
    public String[] parse(Paragraph<?> inParagraph);
    public Span[] parsePos(Paragraph<?> inParagraph);
}
