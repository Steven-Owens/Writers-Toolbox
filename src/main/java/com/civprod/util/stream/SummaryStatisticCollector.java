/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util.stream;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 *
 * @author Steven Owens
 */
public class SummaryStatisticCollector implements Collector<Double,SummaryStatistics,SummaryStatistics> {
    
    public static final SummaryStatisticCollector instance = new SummaryStatisticCollector();

    @Override
    public Supplier<SummaryStatistics> supplier() {
        return SummaryStatistics::new;
    }

    @Override
    public BiConsumer<SummaryStatistics, Double> accumulator() {
        return SummaryStatistics::addValue;
    }

    @Override
    public BinaryOperator<SummaryStatistics> combiner() {
        return (SummaryStatistics coll, SummaryStatistics other) -> {
            SummaryStatistics.copy(other, coll);
            return coll;
        };
    }

    @Override
    public Function<SummaryStatistics, SummaryStatistics> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return java.util.EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
    
}
