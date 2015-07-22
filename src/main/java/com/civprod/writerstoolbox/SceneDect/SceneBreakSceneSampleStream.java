/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import opennlp.tools.sentdetect.EmptyLinePreprocessorStream;
import opennlp.tools.util.FilterObjectStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class SceneBreakSceneSampleStream extends FilterObjectStream<String, SceneSample> {

    public SceneBreakSceneSampleStream(ObjectStream<String> sentences) {
        super(new EmptyLinePreprocessorStream(sentences));
    }

    @Override
    public SceneSample read() throws IOException {
        StringBuilder paragraphString = new StringBuilder();
        List<Span> sceneSpans = new LinkedList<>();

        int begin = 0;
        int count = 0;
        String paragraph;
        while ((paragraph = samples.read()) != null && !paragraph.isEmpty()) {
            paragraph = paragraph.trim();
            if (paragraph.startsWith("<START:scene break>") && paragraph.endsWith("<END>")) {
                int end = count;
                sceneSpans.add(new Span(begin, end));
                begin = count + 1;
            } else {
                paragraphString.append(paragraph);
                paragraphString.append("\n");
            }
            count++;
        }
        int end = count;
        sceneSpans.add(new Span(begin, end));

        if (sceneSpans.size() > 0) {
            return new SceneSample(paragraphString.toString(), sceneSpans.toArray(new Span[sceneSpans.size()]));
        } else {
            return null;
        }
    }
}
