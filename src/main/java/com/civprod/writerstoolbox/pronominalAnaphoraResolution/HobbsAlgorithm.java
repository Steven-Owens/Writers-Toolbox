/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.ChainPronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.SepratingPronounPredicate;
import com.civprod.writerstoolbox.pronominalAnaphoraResolution.pronounpredicate.PronounPredicate;
import com.civprod.writerstoolbox.OpenNLP.OpenNLPUtils;
import com.civprod.writerstoolbox.OpenNLP.ThreadSafeParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Pattern;
import opennlp.tools.parser.Parse;
import opennlp.tools.util.Span;

/**
 *
 * @author Steven Owens
 */
public class HobbsAlgorithm extends BasePronounResolver {

    private final ChainPronounPredicate mSepratingPronounPredicate;
    private final PronounMapper mPronounMapper = new PronounMapper();

    public HobbsAlgorithm(PronounPredicate inPronounPredicate) {
        super(inPronounPredicate);
        mSepratingPronounPredicate = new ChainPronounPredicate(false, new SepratingPronounPredicate(), mPronounPredicate);
    }

    public final static Pattern NPOrSPattern = Pattern.compile("(NP|S|SINV|SBARQ|SQ|TOP)", Pattern.CASE_INSENSITIVE);
    public final static Parse FirstPersonParse = new Parse("FirstPerson",new Span(0,11),"TOP",1.0,0);

    @Override
    public String[][] labelPronouns(String[][] document, Parse[] parses) {
        String[][] labels = new String[document.length][];
        for (int i = 0; i < document.length; i++) {
            String[] curSentence = document[i];
            String[] curSentenceLabels = new String[curSentence.length];
            Parse curRootParse = parses[i];
            Parse[] curTokenParses = getTokenParses(curRootParse).toArray(new Parse[0]);
            for (int j = 0; j < curTokenParses.length; j++) {
                Parse curTokenParse = curTokenParses[j];
                Parse curPOSParse = curTokenParse.getParent();
                if (curPOSParse.getType().equalsIgnoreCase("PRP") || curPOSParse.getType().equalsIgnoreCase("PRP$")) {
                    mPronounPredicate.setPronoun(curTokenParse);
                    String curToken = curSentence[j];
                    //step 1: Begin at the NP node immediately dominating the pronoun
                    Parse immediatelyDominatingNPNode = curPOSParse.getParent();
                    while (!immediatelyDominatingNPNode.getType().equalsIgnoreCase("NP")) {
                        immediatelyDominatingNPNode = immediatelyDominatingNPNode.getParent();
                    }
                    //step 2: go up the tree to the first NP or S node encountered. call this node X, and call the path used to reach it p
                    List<Parse> pathP = new java.util.ArrayList<>();
                    Parse X = findNewX(immediatelyDominatingNPNode, pathP);
                    //step 3: traverse all branches below node X to the left of path p in a left-to-right, breadth-first fashion. Propose as the antecedent any encountered NP node that has an NP or S node between it and X
                    Queue<Parse> traverseQueue = new java.util.ArrayDeque<>();
                    addLeftPaths(traverseQueue, pathP, immediatelyDominatingNPNode);

                    Parse antecedent = findNextAntecedent(traverseQueue, mSepratingPronounPredicate);
                    while (antecedent == null) {

                        //step 4: if node X is highest S node in the sentence, traverse the surface parse trees of previous sentences in the text in order of recency, the most recent first; each tree is traversed in a left-to-right, breadth-first manner, and when an NP node is encountered, it is proposed as antecedent. If X is not the highest S node in the sentence, continue to step 5.
                        if (X == null) {
                            for (int backIndex = i - 1; (backIndex >= 0) && (antecedent == null); backIndex--) {
                                traverseQueue.clear();
                                traverseQueue.add(parses[backIndex]);
                                antecedent = findNextAntecedent(traverseQueue, this.mPronounPredicate);
                            }
                        } else {
                            //step 5: from node X, go up the tree to find the first NP or S node encountered. Call this new node X, and call the path traversed to reach it p.
                            Parse oldX = X;
                            X = findNewX(oldX, pathP);
                            if (X != null) {
                                //step 6: If X is an NP node and if the path p to X did not pass through the Nominal node that X immediately dominates, propose X as the antecedent
                                Parse immediatelyDominatedChild;
                                if (pathP.size() > 2){
                                    immediatelyDominatedChild=pathP.get(1);
                                } else {
                                    immediatelyDominatedChild=oldX;
                                }
                                String immediatelyDominatedChildType = immediatelyDominatedChild.getType();
                                if ((!immediatelyDominatedChildType.startsWith("N")) && (X.getType().equalsIgnoreCase("NP"))) {
                                    if (mPronounPredicate.test(X)) {
                                        antecedent = X;
                                    }
                                }
                                //step 7: Traverse all branches below node X to the left of path p in a left-to-right, breadth-first manner. Propose any NP node encountered as the antecedent.
                                if (antecedent == null) {
                                    traverseQueue.clear();
                                    addLeftPaths(traverseQueue, pathP, oldX);
                                    antecedent = findNextAntecedent(traverseQueue, mPronounPredicate);
                                }
                                //step 8: If X is an S node, traverse all branches of node X to the right of path p in a left-to-right, breadth-first manner, but do not go below NP or S node encountered. Propose any NP node encountered as the antecedent.
                                if ((antecedent == null) && (X.getType().equalsIgnoreCase("S"))) {
                                    traverseQueue.clear();
                                    addRightPaths(traverseQueue, pathP, oldX);
                                    antecedent = findNextAntecedent(traverseQueue, mPronounPredicate);
                                }
                                //step 9: go to step 4
                            }
                        }
                    }
                    curSentenceLabels[j] = antecedent.getCoveredText() + ";" + mPronounMapper.findUltimateAntecedent(antecedent).getCoveredText();
                    mPronounMapper.addPronoun(curTokenParse, antecedent);
                } else {
                    curSentenceLabels[j] = OTHER;
                }
            }
            labels[i] = curSentenceLabels;
        }
        return labels;
    }

    private Parse findNewX(Parse oldX, List<Parse> pathP) {
        pathP.clear();
        Parse X = oldX.getParent();
        while ((X != null) && (!NPOrSPattern.matcher(X.getType()).matches())) {
            pathP.add(0, X);
            X = X.getParent();
        }
        pathP.add(0, X);
        return X;
    }

    public static Parse findNextAntecedent(Queue<Parse> traverseQueue, PronounPredicate mPronounPredicate) {
        Parse antecedent = null;
        while ((!traverseQueue.isEmpty()) && (antecedent == null)) {
            Parse curTestParse = traverseQueue.remove();
            traverseQueue.addAll(java.util.Arrays.asList(curTestParse.getChildren()));
            if (mPronounPredicate.test(curTestParse)) {
                antecedent = curTestParse;
            }
        }
        return antecedent;
    }

    private void addLeftPaths(Queue<Parse> traverseQueue, List<Parse> pathP, Parse oldX) {
        for (int index = 0; index < pathP.size(); index++) {
            Parse curParse = pathP.get(index);
            Parse pathChild;
            if (index < (pathP.size() - 1)) {
                pathChild = pathP.get(index + 1);
            } else {
                pathChild = oldX;
            }
            Parse[] children = curParse.getChildren();
            for (Parse curChild : children) {
                if (curChild.equals(pathChild)) {
                    break;
                }
                traverseQueue.add(curChild);
            }
        }
    }

    private void addRightPaths(Queue<Parse> traverseQueue, List<Parse> pathP, Parse oldX) {
        for (int index = pathP.size() - 1; index >= 0; index--) {
            Parse curParse = pathP.get(index);
            Parse pathChild;
            if (index < (pathP.size() - 1)) {
                pathChild = pathP.get(index + 1);
            } else {
                pathChild = oldX;
            }
            Parse[] children = curParse.getChildren();
            int j = 0;
            Parse curChild = children[j];
            while (!curChild.equals(pathChild)) {
                j++;
                curChild = children[j];
            }
            j++;
            while (j < children.length) {
                curChild = children[j];
                traverseQueue.add(curChild);
                j++;
            }
        }
    }
}
