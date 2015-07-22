/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util;

/**
 *
 * @author Steven Owens
 */
public enum TimeComplexity implements Comparable<TimeComplexity> {
    constant, logarithmic, linear, NlogN, quadraticTime, cubicTime, polynomial, exponentialTime
}
