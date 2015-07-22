/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util.concurrent.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Steven Owens
 */
public class ReadWriteConfigLock implements ReadWriteLock {
    
    private CompositeLock readLock;
    private CompositeLock writeLock;
    private CompositeLock configLock;
    
    public ReadWriteConfigLock(boolean fair){
        this(new ReentrantReadWriteLock(fair),new ReentrantReadWriteLock(fair));
    }
    
    public ReadWriteConfigLock(){
        this(new ReentrantReadWriteLock(),new ReentrantReadWriteLock());
    }
    
    private ReadWriteConfigLock(ReadWriteLock dataReadWriteLock,ReadWriteLock overallReadWriteLock){
        readLock = new CompositeLock(overallReadWriteLock.readLock(), dataReadWriteLock.readLock());
        writeLock = new CompositeLock(overallReadWriteLock.readLock(), dataReadWriteLock.writeLock());
        configLock = new CompositeLock(overallReadWriteLock.writeLock(), dataReadWriteLock.writeLock());
    }
    
    

    @Override
    public Lock readLock() {
        return readLock;
    }

    @Override
    public Lock writeLock() {
        return writeLock;
    }
    
    public Lock configLock() {
        return configLock;
    }
    
}
