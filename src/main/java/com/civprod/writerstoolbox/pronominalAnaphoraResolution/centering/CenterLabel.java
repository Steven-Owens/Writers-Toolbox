/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.centering;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.PronounResolver;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import java.util.Set;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class CenterLabel {

    private String[] sentenceLabelsText = null;
    private final Entity[] sentenceLabelsEntity;
    private Set<Entity> pronounAssignmentsSet = null;
    private final Entity center;

    private static String[] convertToStringArray(Entity[] sentenceLabels) {
        return java.util.Arrays.asList(sentenceLabels)
                .parallelStream()
                .map((Entity curEntity) -> {
                    if (curEntity != null) {
                        return curEntity.getLabel();
                    } else {
                        return PronounResolver.OTHER;
                    }
                })
                .collect(java.util.stream.Collectors.toList())
                .toArray(new String[sentenceLabels.length]);
    }
    
    private static Set<Entity> convertToSet(Entity[] sentenceLabels) {
        return java.util.Arrays.asList(sentenceLabels)
                .parallelStream()
                .filter((Object curItem) -> curItem != null)
                .collect(java.util.stream.Collectors.toSet());
    }

    public CenterLabel(Entity[] sentenceLabels, Entity center) {
        sentenceLabelsEntity = sentenceLabels;
        this.center = center;
    }

    String[] getSentenceLabels() {
        if (sentenceLabelsText == null)
        {
            sentenceLabelsText = convertToStringArray(sentenceLabelsEntity);
        }
        return sentenceLabelsText;
    }

    public Set<Entity> getPronounAssignmentsSet() {
        if (pronounAssignmentsSet == null){
            pronounAssignmentsSet = convertToSet(sentenceLabelsEntity);
        }
        return pronounAssignmentsSet;
    }

    public Entity getCenter() {
        return center;
    }

    /**
     * @return the sentenceLabelsEntity
     */
    public Entity[] getSentenceLabelsEntity() {
        return sentenceLabelsEntity;
    }

}
