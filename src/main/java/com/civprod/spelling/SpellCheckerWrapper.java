/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.spelling;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.WordTokenizer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Steven Owens
 */
public class SpellCheckerWrapper implements SpellCheckListener {
    private SpellChecker mSpellChecker;
    
    private final List<SpellCheckEvent> SpellingEvents;
    private final List<SpellCheckEvent> unmodifiableSpellingEvents;
    
    public SpellCheckerWrapper(String dictFilePath) throws IOException{
        this(new File(dictFilePath));
    }
    
    public SpellCheckerWrapper(String dictFilePath,String phonetFilePath) throws IOException{
        this(new File(dictFilePath),new File(phonetFilePath));
    }
    
    public SpellCheckerWrapper(File dictFile) throws IOException{
        this(dictFile,null);
    }
    
    public SpellCheckerWrapper(File dictFile,File phonetFile) throws IOException{
        this(new SpellDictionaryHashMap(dictFile,phonetFile));
    }
    
    public SpellCheckerWrapper(SpellDictionary dictionary)
    {
        this(new SpellChecker(dictionary));
    }
    
    public SpellCheckerWrapper(SpellChecker spellCheck){
        SpellingEvents = new ArrayList<>();
        unmodifiableSpellingEvents = org.apache.commons.collections4.ListUtils.unmodifiableList(SpellingEvents);
        mSpellChecker = spellCheck;
        mSpellChecker.addSpellCheckListener(this);
    }
    
    public void SpellCheck(String DocumentOrEquivalent){
        SpellCheck(new StringWordTokenizer(DocumentOrEquivalent));
    }
    
    public void SpellCheck(WordTokenizer DocumentOrEquivalentWordTokenizer){
        SpellingEvents.clear();
        mSpellChecker.checkSpelling(DocumentOrEquivalentWordTokenizer);
    }

    @Override
    public void spellingError(SpellCheckEvent event) {
        SpellingEvents.add(event);
    }

    /**
     * @return the unmodifiableSpellingEvents
     */
    public List<SpellCheckEvent> getUnmodifiableSpellingEvents() {
        return unmodifiableSpellingEvents;
    }
    
    
}
