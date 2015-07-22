/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.swing;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.MutableComboBoxModel;

/**
 *
 * @author steven owens
 * @param <T>
 */
public class CollectionListModel<T>
        extends javax.swing.AbstractListModel<T>
        implements java.util.List<T>, MutableComboBoxModel<T> {

    /**
     *
     */
    private static final long serialVersionUID = -3036702579663732244L;
    protected java.util.List<T> mList;
    protected T selectedItem;
    

    public CollectionListModel() {
        mList = new java.util.ArrayList<>();
    }

    public CollectionListModel(java.util.Collection<T> inCollection) {
        this();
        if (inCollection != null){
            mList.addAll(inCollection);
        }
    }

    public CollectionListModel(Class<? extends java.util.List<T>> inClass) throws InstantiationException, IllegalAccessException {
        mList = inClass.newInstance();
    }

    public CollectionListModel(Class<? extends java.util.List<T>> inClass, java.util.Collection<T> inCollection) throws InstantiationException, IllegalAccessException {
        this(inClass);
        if (inCollection != null){
            mList.addAll(inCollection);
        }
    }

    @Override
    public boolean add(T e) {
        boolean added = mList.add(e);
        fireIntervalAdded(this, mList.size() - 1, mList.size() - 1);
        return added;
    }

    @Override
    public void add(int index, T element) {
        mList.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        int start = mList.size();
        boolean added = mList.addAll(c);
        fireIntervalAdded(this, start, mList.size() - 1);
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean added = mList.addAll(index, c);
        fireIntervalAdded(this, index, index + c.size() - 1);
        return added;
    }

    @Override
    public void clear() {
        int end = mList.size() - 1;
        mList.clear();
        fireIntervalRemoved(this, 0, end);
    }

    @Override
    public boolean contains(Object o) {
        return mList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return mList.containsAll(c);
    }

    @Override
    public T get(int index) {
        return mList.get(index);
    }

    @Override
    public int indexOf(Object o) {
        return mList.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return java.util.Collections.unmodifiableList(mList).iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return mList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return java.util.Collections.unmodifiableList(mList).listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return java.util.Collections.unmodifiableList(mList).listIterator(index);
    }

    @Override
    public boolean remove(Object o) {
        int itemNum = mList.indexOf(o);
        boolean removed = mList.remove(o);
        fireIntervalRemoved(this, itemNum, itemNum);
        return removed;
    }

    @Override
    public T remove(int index) {
        T item = mList.remove(index);
        fireIntervalRemoved(this, index, index);
        return item;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;
        for (Object curObject : c){
            int index = mList.indexOf(curObject);
            if (mList.remove(curObject)){
                fireIntervalRemoved(this, index, index);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        List<?> mirrorList = new java.util.ArrayList<>(mList);
        mirrorList.removeAll(c);
        return removeAll(mirrorList);
    }

    @Override
    public T set(int index, T element) {
        T item = mList.set(index, element);
        fireContentsChanged(this, index, index);
        return item;
    }

    @Override
    public int size() {
        return mList.size();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return mList.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return mList.toArray();
    }

    @Override
    public <S> S[] toArray(S[] a) {
        return mList.toArray(a);
    }

    @Override
    public T getElementAt(int arg0) {
        return mList.get(arg0);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addElement(Object obj) {
        this.add((T) obj);
    }

    @Override
    public void removeElement(Object obj) {
        this.remove(obj);
    }

    
    @Override
    @SuppressWarnings("unchecked")
    public void insertElementAt(Object obj, int index) {
        this.add(index, (T) obj);
    }

    @Override
    public void removeElementAt(int index) {
        this.remove(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setSelectedItem(Object anItem) {
        selectedItem = (T)anItem;
    }

    @Override
    public T getSelectedItem() {
        return selectedItem;
    }
}
