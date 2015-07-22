/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.BaseToolFactory;
import opennlp.tools.util.InvalidFormatException;

/**
 *
 * @author Steven Owens
 */
public class SceneDetectorModel extends opennlp.tools.util.model.BaseModel {
    
    private static final String COMPONENT_NAME = "SceneBreakDetectorME";
    private static final String SCENE_BREAK_DETECTOR_MODEL_ENTRY_NAME = "SceneBreakDetector.model";
    
    public SceneDetectorModel(File modelFile) throws IOException{
        super(COMPONENT_NAME,modelFile);
    }
    
    public SceneDetectorModel(InputStream in) throws IOException{
        super(COMPONENT_NAME,in);
    }
    
    public SceneDetectorModel(URL modelURL) throws IOException{
        super(COMPONENT_NAME,modelURL);
    }
    
    public SceneDetectorModel(String languageCode, MaxentModel sceneBreakDetectorModel,
      Map<String, String> manifestInfoEntries, SceneDetectorFactory factory) {
    super(COMPONENT_NAME, languageCode, manifestInfoEntries, factory);
    artifactMap.put(SCENE_BREAK_DETECTOR_MODEL_ENTRY_NAME, sceneBreakDetectorModel);
    checkArtifactMap();
  }
    
    public SceneDetectorModel(String languageCode, MaxentModel sceneBreakDetectorModel, SceneDetectorFactory factory) {
    this(languageCode, sceneBreakDetectorModel, null, factory);
  }
    
    @Override
  protected void validateArtifactMap() throws InvalidFormatException {
    super.validateArtifactMap();

    if (!(artifactMap.get(SCENE_BREAK_DETECTOR_MODEL_ENTRY_NAME) instanceof MaxentModel)) {
      throw new InvalidFormatException("Chunker model is incomplete!");
    }
  }
  
  public MaxentModel getSceneBreakDetectorModel() {
    return (MaxentModel) artifactMap.get(SCENE_BREAK_DETECTOR_MODEL_ENTRY_NAME);
  }
  
  @Override
  protected Class<? extends BaseToolFactory> getDefaultFactory() {
    return SceneDetectorFactory.class;
  }
  
  public SceneDetectorFactory getFactory() {
    return (SceneDetectorFactory) this.toolFactory;
  }
}
