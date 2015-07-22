/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.SceneDect;

/**
 *
 * @author Steven Owens
 */
public interface SceneContextGenerator {
    public abstract String[] getContext(String[] inParagraphs, int pos);
}
