/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.NaturalLanguage.util.Counters;

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
public class CounterUtils {
    public static <E> Collector<E,?,Counter<E>> CounterCollector(){
         return new CounterCollectorImpl<>();
    }
    
    public static <E> Counter<E> count(List<E> inList){
        return inList.parallelStream().collect(CounterCollector());
    }

    private static class CounterCollectorImpl<E> implements Collector<E, Counter<E>, Counter<E>> {
        
        private CounterCollectorImpl(){
            
        }

        @Override
        public Supplier<Counter<E>> supplier() {
            return Counter<E>::new;
        }

        @Override
        public BiConsumer<Counter<E>, E> accumulator() {
            return (Counter<E> coll, E item) -> coll.incrementCount(item, 1);
        }

        @Override
        public BinaryOperator<Counter<E>> combiner() {
            return (Counter<E> coll, Counter<E> other)-> {
                    coll.incrementAll(other);
                    return coll;
            };
        }

        @Override
        public Function<Counter<E>, Counter<E>> finisher() {
            return (Counter<E> coll)->coll;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return java.util.EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
        }
    }
}
