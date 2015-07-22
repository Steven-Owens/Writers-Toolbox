/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import opennlp.tools.chunker.ChunkerContextGenerator;
import opennlp.tools.chunker.DefaultChunkerContextGenerator;
import opennlp.tools.chunker.DefaultChunkerSequenceValidator;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.SequenceValidator;
import opennlp.tools.util.ext.ExtensionLoader;

/**
 *
 * @author Steven Owens
 */
public class SceneDetectorFactory extends opennlp.tools.util.BaseToolFactory {
    
    /**
   * Creates a {@link SceneBreakDetectorFactory} that provides the default implementation
   * of the resources.
   */
  public SceneDetectorFactory() {
  }
  
  public static SceneDetectorFactory create(String subclassName)
      throws InvalidFormatException {
    if (subclassName == null) {
      // will create the default factory
      return new SceneDetectorFactory();
    }
    try {
      SceneDetectorFactory theFactory = ExtensionLoader.instantiateExtension(SceneDetectorFactory.class, subclassName);
      return theFactory;
    } catch (Exception e) {
      String msg = "Could not instantiate the " + subclassName
          + ". The initialization throw an exception.";
      System.err.println(msg);
      e.printStackTrace();
      throw new InvalidFormatException(msg, e);
    }
  }

    @Override
    public void validateArtifactMap() throws InvalidFormatException {
        // no additional artifacts
    }

    public SceneContextGenerator getSceneContextGenerator() {
        //todo: allow parms to be passed in
        return new DefaultSceneContextGenerator.Builder().build();
    }
    
}
