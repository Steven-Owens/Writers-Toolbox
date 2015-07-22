/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 *
 * @author Steven Owens
 * @param <T>
 */
public class ChainListWrapper<T> implements List<T> {
    protected final List<T> interList;
    
    public static <T> List<T> createList(Collection<T> inList){
        java.util.function.Supplier<List<T>> SupplierFun = ()-> {
            return org.apache.commons.collections4.list.SetUniqueList.setUniqueList(new ArrayList<T>());
        };
        List<T> rList = inList
                .parallelStream()
                .collect(SupplierFun
                , (List<T> coll, T curItem) -> {
            if (curItem instanceof ChainListWrapper){
                coll.addAll(createList((ChainListWrapper)curItem));
            } else {
                coll.add(curItem);
            }
        }, List<T>::addAll);
        return rList;
    }
    
    public ChainListWrapper(List<T> inList){
        interList = createList(inList);
    }
    
    public ChainListWrapper(T... inList){
        this(java.util.Arrays.asList(inList));
    }
    
    @Override
    public int size() {
        return interList.size();
    }

    @Override
    public boolean isEmpty() {
        return interList.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return interList.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return interList.iterator();
    }

    @Override
    public Object[] toArray() {
        return interList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return interList.toArray(a);
    }

    @Override
    public boolean add(T e) {
        return interList.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return interList.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return interList.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return interList.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return interList.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return interList.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return interList.retainAll(c);
    }

    @Override
    public void clear() {
        interList.clear();
    }

    @Override
    public T get(int index) {
        return interList.get(index);
    }

    @Override
    public T set(int index, T element) {
        return interList.set(index, element);
    }

    @Override
    public void add(int index, T element) {
        interList.add(index, element);
    }

    @Override
    public T remove(int index) {
        return interList.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return interList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return interList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return interList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return interList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return interList.subList(fromIndex, toIndex);
    }
    
    @Override
    public void sort(Comparator<? super T> c){
        interList.sort(c);
    }
    
    @Override
    public Spliterator<T> spliterator(){
        return interList.spliterator();
    }
    
    @Override
    public Stream<T> stream(){
       return interList.stream();
    }
    
    @Override
    public Stream<T> parallelStream(){
        return interList.parallelStream();
    }
    
    @Override
    public boolean removeIf(Predicate<? super T> filter){
        return interList.removeIf(filter);
    }
    
    @Override
    public void forEach(Consumer<? super T> action){
        interList.forEach(action);
    }
    
}
