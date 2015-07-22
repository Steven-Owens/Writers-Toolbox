/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author Steven Owens
 */
public class FlatListCollector<T> implements Collector<List<T>,List<T>,List<T>> {
    
    public static final FlatListCollector<String> FlatListCollectorString = new FlatListCollector<String>();

    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList<T>::new;
    }

    @Override
    public BiConsumer<List<T>, List<T>> accumulator() {
        return (List<T> coll, List<T> other)->{
            coll.addAll(other);
        };
    }

    @Override
    public BinaryOperator<List<T>> combiner() {
        return (List<T> coll, List<T> other)->{
            coll.addAll(other);
            return coll;
        };
    }

    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return java.util.EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
    
}
