/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

/**
 *
 * @author Steven Owens
 */
public interface Entity {
    public boolean matches(String possibleMatch);
    public double EstimatedChanceOfMatch(String possibleMatch);
    public void extendEntity(String possibleMatch);
}
