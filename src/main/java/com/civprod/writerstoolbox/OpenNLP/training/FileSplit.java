/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.OpenNLP.training;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Steven Owens
 */
public class FileSplit {
    
    public static List<FileSplit> generateFileSplitsKFolds(List<File> inTrainingFiles, int k){
        int foldSize = (int) Math.round(((double)inTrainingFiles.size())/((double)k));
        return generateFileSplitsFoldsOfK(inTrainingFiles,foldSize);
    }
    
    public static List<FileSplit> generateFileSplitsFoldsOfK(List<File> inTrainingFiles, int foldSize){
        List<List<File>> partition = org.apache.commons.collections4.ListUtils.partition(inTrainingFiles, foldSize);
        List<FileSplit> fileSplits = new java.util.ArrayList<>(partition.size());
        for (int i = 0; i < partition.size(); i ++){
            List<File> testFiles = partition.get(i);
            List<File> trainingFiles = org.apache.commons.collections4.ListUtils.removeAll(inTrainingFiles, testFiles);
            fileSplits.add(new FileSplit("folds of K" + i, trainingFiles, testFiles));
        }
        return fileSplits;
    }
    
    public static List<FileSplit> generateFileSplitsLOO(List<File> inTrainingFiles){
        int numberOfFiles = inTrainingFiles.size();
        int numberOfTestFiles = 1;
        int numberOfTrainingFiles = numberOfFiles - numberOfTestFiles;
        List<FileSplit> fileSplits = new java.util.ArrayList<>(numberOfFiles);
        for (int i = 0; i < numberOfFiles; i++){
            Set<File> testFiles = java.util.Collections.singleton(inTrainingFiles.get(i));
            List<File> trainingFiles = org.apache.commons.collections4.ListUtils.removeAll(inTrainingFiles, testFiles);
            fileSplits.add(new FileSplit("LOO" + i, trainingFiles, testFiles));
        }
        return fileSplits;
    }
    
    private final String mSplitName;
    private final Collection<File> mTrainingFiles;
    private final Collection<File> mTestFiles;
    
    public FileSplit(String inSplitName,Collection<File> inTrainingFiles,Collection<File> inTestFiles){
        mSplitName = inSplitName;
        mTrainingFiles = org.apache.commons.collections4.CollectionUtils.unmodifiableCollection(new java.util.ArrayList<>(inTrainingFiles));
        mTestFiles = org.apache.commons.collections4.CollectionUtils.unmodifiableCollection(new java.util.ArrayList<>(inTestFiles));
    }

    /**
     * @return the mSplitName
     */
    public String getSplitName() {
        return mSplitName;
    }

    /**
     * @return the mTrainingFiles
     */
    public Collection<File> getTrainingFiles() {
        return mTrainingFiles;
    }

    /**
     * @return the mTestFiles
     */
    public Collection<File> getTestFiles() {
        return mTestFiles;
    }
}
