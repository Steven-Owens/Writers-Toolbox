/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util;

/**
 *
 * @author Steven Owens
 */
public interface PossiblyThreadSafe {

    public default boolean isThreadSafe() {
        boolean threadSafe = isThreadSafeClassOrInterface(this);
        return threadSafe;
    }

    public static boolean isThreadSafeClassOrInterface(Object inObject) {
        boolean threadSafe;
        if (inObject instanceof ThreadSafe) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.BlockingQueue) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.ConcurrentMap) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.locks.Lock) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.locks.ReadWriteLock) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.locks.Condition) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.CompletionService) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.ConcurrentLinkedDeque) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.Executor) {
            threadSafe = true;
        } else if (inObject instanceof ThreadLocal) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.locks.AbstractOwnableSynchronizer) {
            threadSafe = true;
        } else if (inObject instanceof org.apache.commons.collections4.Unmodifiable) {
            threadSafe = true;
        } else if (inObject instanceof org.apache.commons.lang3.concurrent.ConcurrentInitializer) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.ConcurrentLinkedQueue) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.ConcurrentSkipListSet) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.CopyOnWriteArrayList) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.CopyOnWriteArraySet) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.CountDownLatch) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.CyclicBarrier) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.Exchanger) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.Phaser) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.Semaphore) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.ThreadLocalRandom) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicBoolean) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicInteger) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicIntegerArray) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicIntegerFieldUpdater) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicLong) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicLongArray) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicLongFieldUpdater) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicMarkableReference) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicReference) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicReferenceArray) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicReferenceFieldUpdater) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.AtomicStampedReference) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.DoubleAccumulator) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.DoubleAdder) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.LongAccumulator) {
            threadSafe = true;
        } else if (inObject instanceof java.util.concurrent.atomic.LongAdder) {
            threadSafe = true;
            } else if (inObject instanceof org.apache.commons.lang3.concurrent.TimedSemaphore) {
            threadSafe = true;
        } else {
            threadSafe = false;
        }
        return threadSafe;
    }
}
