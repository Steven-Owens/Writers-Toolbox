/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution.centering;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.BasePronounResolver;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.entities.Entity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class BasicCenteringRuleSet extends BaseCenteringRuleSet {
    
    public enum ShiftType{
        Continue,Retain,SmoothShift,RoughShift
    }

    @Override
    public CenterLabel getAssignment(final String[] curSentence, final Parse curRootParse, final SortedSet<Entity> forwardLookingCentersMinusOne, final Entity centerMinusOne) {
        Set<Entity> possibleEntities = new HashSet<>(forwardLookingCentersMinusOne);
        BasePronounResolver.addAllNonPronounNPNodes(possibleEntities, curRootParse);
        Set<CenterLabel> possibleLabelings = createLabels(curSentence,curRootParse,possibleEntities);
        Map<ShiftType,Set<CenterLabel>> shiftSets = possibleLabelings.parallelStream()
                .collect(HashMap<ShiftType,Set<CenterLabel>>::new, 
                (Map<ShiftType,Set<CenterLabel>> coll, CenterLabel curLabeling) -> {
                    Entity PreferredEntity = curLabeling.getPronounAssignmentsSet().parallelStream().max(mRankingComparator).get();
                    ShiftType curShiftType;
                    if (centerMinusOne.equals(curLabeling.getCenter())){
                        if (PreferredEntity.equals(curLabeling.getCenter())) {
                            curShiftType = ShiftType.Continue;
                        } else {
                            curShiftType = ShiftType.Retain;
                        }
                    } else {
                        if (PreferredEntity.equals(curLabeling.getCenter())) {
                            curShiftType = ShiftType.SmoothShift;
                        } else {
                           curShiftType = ShiftType.RoughShift; 
                        }
                    }
                    if (!coll.containsKey(curShiftType)){
                        coll.put(curShiftType, new HashSet<>(1));
                    }
                    coll.get(curShiftType).add(curLabeling);
        }, (Map<ShiftType,Set<CenterLabel>> coll, Map<ShiftType,Set<CenterLabel>> r2) -> coll.putAll(r2));
        if (shiftSets.containsKey(ShiftType.Continue)){
            possibleLabelings = shiftSets.get(ShiftType.Continue);
        } else if (shiftSets.containsKey(ShiftType.Retain)){
            possibleLabelings = shiftSets.get(ShiftType.Retain);
        }  else if (shiftSets.containsKey(ShiftType.SmoothShift)){
            possibleLabelings = shiftSets.get(ShiftType.SmoothShift);
        }  else if (shiftSets.containsKey(ShiftType.RoughShift)){
            possibleLabelings = shiftSets.get(ShiftType.RoughShift);
        }
        return chooseLabeling(possibleLabelings);
    }

    protected final Set<CenterLabel> createLabels(String[] curSentence, Parse curRootParse, Set<Entity> possibleEntities) {
        return createLabels(curSentence,BasePronounResolver.getTokenParses(curRootParse),possibleEntities);
    }
    
    protected final Set<CenterLabel> createLabels(String[] curSentence, List<Parse> curTokenParses, Set<Entity> possibleEntities) {
        return createLabels(curSentence,curTokenParses.toArray(new Parse[curTokenParses.size()]),possibleEntities);
    }
    
    protected final Set<CenterLabel> createLabels(String[] curSentence, Parse[] curTokenParses, Set<Entity> possibleEntities) {
       Set<CenterLabel> prevLabelings = createLabels(curSentence,curTokenParses,possibleEntities,0);
       Set<CenterLabel> rSet = prevLabelings
               .parallelStream()
               .map((CenterLabel curLabeling) -> curLabeling.getPronounAssignmentsSet()
                       .parallelStream()
               .map((Entity curEntity) -> new CenterLabel(curLabeling.getSentenceLabelsEntity(),curEntity))
               .collect(java.util.stream.Collectors.toSet()))
               .collect(HashSet<CenterLabel>::new, Set<CenterLabel>::addAll, Set<CenterLabel>::addAll);
       return rSet;
    }
    
    private final Set<CenterLabel> createLabels(String[] curSentence, Parse[] curTokenParses, Set<Entity> possibleEntities, int index) {
        Set<CenterLabel> rSet;
        if (index < curTokenParses.length){
            Set<CenterLabel> prevLabelings = createLabels(curSentence,curTokenParses,possibleEntities,index+1);
            Parse curTokenParse = curTokenParses[index];
            Parse curPOSParse = curTokenParse.getParent();
            if (curPOSParse.getType().equalsIgnoreCase("PRP") || curPOSParse.getType().equalsIgnoreCase("PRP$")) {
                mPronounPredicate.setPronoun(curTokenParse);
                Set<Entity> possibleEntitiesForPronoun = possibleEntities.parallelStream().filter(mPronounPredicate).collect(java.util.stream.Collectors.toSet());
                rSet = prevLabelings
                        .parallelStream()
                        .map((CenterLabel curLabeling) -> possibleEntitiesForPronoun
                                .parallelStream()
                                .map((Entity curEntity) -> {
                                    Entity[] lowLevelLabeling = curLabeling.getSentenceLabelsEntity();
                                    lowLevelLabeling = java.util.Arrays.copyOf(lowLevelLabeling, lowLevelLabeling.length);
                                    lowLevelLabeling[index] = curEntity;
                                    return new CenterLabel(lowLevelLabeling,curLabeling.getCenter());
                                })
                        .collect(java.util.stream.Collectors.toSet()))
                        .collect(HashSet<CenterLabel>::new, Set<CenterLabel>::addAll, Set<CenterLabel>::addAll);
            } else {
                rSet = prevLabelings;
            }
        } else {
            rSet = new HashSet<>(1);
            rSet.add(new CenterLabel(new Entity[curSentence.length],null));
        }
        return rSet;
    }
    
    protected CenterLabel chooseLabeling(Set<CenterLabel> possibleLabelings) {
        return possibleLabelings.stream().findAny().get();
    }
    
}
