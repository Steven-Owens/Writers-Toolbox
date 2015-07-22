/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.thoughtAndSpeech;

import com.civprod.writerstoolbox.SceneDect.SceneDetectorFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import opennlp.tools.ml.model.MaxentModel;
import opennlp.tools.util.BaseToolFactory;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.model.BaseModel;

/**
 *
 * @author Steven Owens
 */
public class ThoughtAndSpeechModel extends BaseModel {
    private static final String COMPONENT_NAME = "ThoughtAndSpeechParserME";
  private static final String THOUGHT_AND_SPEECH_ENTRY_NAME = "thought_and_speech.model";
  
  public ThoughtAndSpeechModel(File modelFile) throws IOException{
        super(COMPONENT_NAME,modelFile);
    }
    
    public ThoughtAndSpeechModel(InputStream in) throws IOException{
        super(COMPONENT_NAME,in);
    }
    
    public ThoughtAndSpeechModel(URL modelURL) throws IOException{
        super(COMPONENT_NAME,modelURL);
    }
    
    public ThoughtAndSpeechModel(String languageCode, MaxentModel thoughtAndSpeechModel,
      Map<String, String> manifestInfoEntries, ThoughtAndSpeechParserFactory factory) {
    super(COMPONENT_NAME, languageCode, manifestInfoEntries, factory);
    artifactMap.put(THOUGHT_AND_SPEECH_ENTRY_NAME, thoughtAndSpeechModel);
    checkArtifactMap();
  }
    
    public ThoughtAndSpeechModel(String languageCode, MaxentModel thoughtAndSpeechModel, ThoughtAndSpeechParserFactory factory) {
    this(languageCode, thoughtAndSpeechModel, null, factory);
  }
    
    @Override
  protected void validateArtifactMap() throws InvalidFormatException {
    super.validateArtifactMap();

    if (!(artifactMap.get(THOUGHT_AND_SPEECH_ENTRY_NAME) instanceof MaxentModel)) {
      throw new InvalidFormatException("Chunker model is incomplete!");
    }
  }

  public MaxentModel getThoughtAndSpeechModel() {
    return (MaxentModel) artifactMap.get(THOUGHT_AND_SPEECH_ENTRY_NAME);
  }
  
  @Override
  protected Class<? extends BaseToolFactory> getDefaultFactory() {
    return ThoughtAndSpeechParserFactory.class;
  }

  
  public ThoughtAndSpeechParserFactory getFactory() {
    return (ThoughtAndSpeechParserFactory) this.toolFactory;
  }
}
