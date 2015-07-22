/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util.concurrent.locks;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 *
 * @author Steven Owens
 */
public class CompositeLock implements Lock {

    private final List<Lock> interLocks;

    public CompositeLock(List<Lock> inLocks) {
        interLocks = org.apache.commons.collections4.ListUtils.unmodifiableList(new java.util.ArrayList<>(inLocks));
    }

    public CompositeLock(Lock... inLocks) {
        this(java.util.Arrays.asList(inLocks));
    }

    @Override
    public void lock() {
        for (Lock curLock : interLocks) {
            curLock.lock();
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        for (Lock curLock : interLocks) {
            curLock.lockInterruptibly();
        }
    }

    @Override
    public boolean tryLock() {
        int currentLockNumber = 0;
        boolean locked = true;
        try {
            while (locked && (currentLockNumber < interLocks.size())) {
                if (interLocks.get(currentLockNumber).tryLock()) {
                    currentLockNumber++;
                } else {
                    locked = false;
                }
            }
        } catch (Exception ex) {
            locked = false;
            throw (ex);
        } finally {
            if (!locked) {
                while (currentLockNumber >= 1) {
                    currentLockNumber--;
                    interLocks.get(currentLockNumber).unlock();
                }
            }
        }
        return locked;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeInNanos = unit.convert(time, TimeUnit.NANOSECONDS);
        long startTime = System.nanoTime();
        int currentLockNumber = 0;
        boolean locked = true;
        try {
            while (locked && (currentLockNumber < interLocks.size())) {
                long currentWaitTime = timeInNanos - (System.nanoTime()-startTime);
                if (interLocks.get(currentLockNumber).tryLock(currentWaitTime,TimeUnit.NANOSECONDS)) {
                    currentLockNumber++;
                } else {
                    locked = false;
                }
            }
        } catch (Exception ex) {
            locked = false;
            throw (ex);
        } finally {
            if (!locked) {
                while (currentLockNumber >= 1) {
                    currentLockNumber--;
                    interLocks.get(currentLockNumber).unlock();
                }
            }
        }
        return locked;
    }

    @Override
    public void unlock() {
        for (Lock curLock : interLocks) {
            curLock.unlock();
        }
    }

    @Override
    public Condition newCondition() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
