/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.pronominalAnaphoraResolution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import opennlp.tools.parser.Parse;

/**
 *
 * @author Steven Owens
 */
public class ChainPronounPredicate implements PronounPredicate, Collection<Predicate<Parse>> {
    private List<Predicate<Parse>> allPredicates;
    private boolean parallel;
    
    public ChainPronounPredicate(){
        allPredicates = new ArrayList<>();
    }
    
    public ChainPronounPredicate(boolean parallel, List<Predicate<Parse>> inPredicates){
        this();
        allPredicates.addAll(inPredicates);
        this.parallel = parallel;
    }
    
    public ChainPronounPredicate(List<Predicate<Parse>> inPredicates){
        this(true,inPredicates);
    }
    
    public ChainPronounPredicate(boolean parallel, Predicate<Parse>... inPredicates){
        this(parallel, java.util.Arrays.asList(inPredicates));
    }
    
    public ChainPronounPredicate(Predicate<Parse>... inPredicates){
        this(true,inPredicates);
    }
    
    private Stream<Predicate<Parse>> getSteam(){
        if (this.parallel){
            return allPredicates.parallelStream();
        } else {
            return allPredicates.stream();
        }
    }

    @Override
    public boolean test(Parse t) {
        return getSteam().allMatch((Predicate<Parse> curPredicate) -> curPredicate.test(t));
    }

    @Override
    public void setPronoun(Parse PronounToken) {
        getSteam()
                .filter((Predicate<Parse> curPredicate) -> curPredicate instanceof PronounPredicate)
                .map((Predicate<Parse> curPredicate) -> (PronounPredicate)curPredicate)
                .forEach((PronounPredicate curPronounPredicate) -> curPronounPredicate.setPronoun(PronounToken));
    }

    @Override
    public int size() {
        return allPredicates.size();
    }

    @Override
    public boolean isEmpty() {
        return allPredicates.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return allPredicates.contains(o);
    }

    @Override
    public Iterator<Predicate<Parse>> iterator() {
        return allPredicates.iterator();
    }

    @Override
    public Object[] toArray() {
        return allPredicates.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return allPredicates.toArray(a);
    }

    @Override
    public boolean add(Predicate<Parse> e) {
        return allPredicates.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return allPredicates.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return allPredicates.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Predicate<Parse>> c) {
        return allPredicates.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return allPredicates.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return allPredicates.retainAll(c);
    }

    @Override
    public void clear() {
        allPredicates.clear();
    }

    
}
