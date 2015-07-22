/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.apps;

import com.civprod.util.concurrent.locks.ReadWriteConfigLock;
import com.civprod.writerstoolbox.NaturalLanguage.util.Wrapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Steven Owens
 */
public class ToolSuite {

    private final Map<Class<? extends Object>, Object> toolMap;
    private final Map<String, Object> namedTools;
    private AtomicBoolean addOnly;
    private AtomicBoolean sealed;
    private ToolSuite mParent;
    private ReadWriteConfigLock myToolMapReadWriteLock;
    private ReadWriteConfigLock myNamedToolsReadWriteLock;
    private ReentrantReadWriteLock myParentLock;

    public ToolSuite(boolean addOnly) {
        toolMap = org.apache.commons.collections4.MapUtils.synchronizedMap(new HashMap<>());
        namedTools = org.apache.commons.collections4.MapUtils.synchronizedMap(new HashMap<>());
        this.addOnly = new AtomicBoolean(addOnly);
        this.sealed = new AtomicBoolean(false);
        mParent = null;
        myToolMapReadWriteLock = new ReadWriteConfigLock();
        myNamedToolsReadWriteLock = new ReadWriteConfigLock();
        myParentLock = new ReentrantReadWriteLock(false);
    }

    public ToolSuite() {
        this(false);
    }

    public ToolSuite(ToolSuite inParent) {
        this(inParent, inParent.isAddOnly());
    }

    public ToolSuite(ToolSuite inParent, boolean addOnly) {
        this(addOnly);
        mParent = inParent;
    }
    
    public boolean addTool(String key, Object inTool){
        boolean added = false;
        myNamedToolsReadWriteLock.writeLock().lock();
        try {
            added = addTool(key,inTool, this.addOnly.get());
        } finally {
                myNamedToolsReadWriteLock.writeLock().unlock();
        }
        return added;
    }
    
    public boolean addTool(String key, Object inTool, boolean addOnly){
        boolean added = false;
        myNamedToolsReadWriteLock.writeLock().lock();
        try {
            added = addTool(key,inTool,this.sealed.get(), addOnly);
        } finally {
                myNamedToolsReadWriteLock.writeLock().unlock();
        }
        return added;
    }

    public boolean addTool(String key, Object inTool, boolean sealed, boolean addOnly) {
        boolean added = false;
        if (!sealed) {
            myNamedToolsReadWriteLock.writeLock().lock();
            try {
                if ((!addOnly) || (!namedTools.containsKey(key))) {
                    namedTools.put(key, inTool);
                }
            } finally {
                myNamedToolsReadWriteLock.writeLock().unlock();
            }
        }
        return added;
    }

    public boolean addTool(Object inTool) {
        boolean added = false;
        myToolMapReadWriteLock.writeLock().lock();
        try {
            added |= addTool(inTool, this.addOnly.get());
        } finally {
            myToolMapReadWriteLock.writeLock().unlock();
        }
        return added;
    }
    
    public boolean addTool(Object inTool, boolean addOnly) {
        boolean added = false;
        myToolMapReadWriteLock.writeLock().lock();
        try {
            added |= addTool(inTool, this.sealed.get(), addOnly);
        } finally {
            myToolMapReadWriteLock.writeLock().unlock();
        }
        return added;
    }

    public boolean addTool(Object inTool, boolean sealed, boolean addOnly) {
        boolean added = false;
        if (!sealed) {
            myToolMapReadWriteLock.writeLock().lock();
            try {
                if (inTool instanceof Wrapper) {
                    added |= addTool(((Wrapper) inTool).getInterObject(),sealed , addOnly);
                }
                added |= addTool(inTool.getClass(), inTool, addOnly);
            } finally {
                myToolMapReadWriteLock.writeLock().unlock();
            }
        }
        return added;
    }

    private boolean addTool(Class<? extends Object> toolClass, Object inTool, boolean addOnly) {
        boolean added = false;
        if (toolClass != null) {
            if ((!addOnly) || (!toolMap.containsKey(toolClass))) {
                toolMap.put(toolClass, inTool);
                added |= toolMap.get(toolClass) == inTool;
                //stop recurshion when you reach Object(i.e. the root of the class tree)
                if (!Object.class.equals(toolClass)) {
                    added |= addTool(toolClass.getSuperclass(), inTool, addOnly);
                    for (Class<?> curClass : toolClass.getInterfaces()) {
                        added |= addTool(curClass, inTool, addOnly);
                    }
                }
            }
        }
        return added;
    }

    public Object getTool(String toolName) {
        final Object rTool;
        if (namedTools.containsKey(toolName)) {
            rTool = namedTools.get(toolName);
        } else {
            if (this.mParent != null) {
                rTool = mParent.getTool(toolName);
            } else {
                rTool = null;
            }
        }
        return rTool;
    }

    public List<Object> getToolsByNames(List<String> toolNames) {
        List<Object> rTools = new java.util.ArrayList<>(toolNames.size());
        final int parentReadCount = this.myParentLock.getReadHoldCount();
        myNamedToolsReadWriteLock.readLock().lock();
        try {
            if (!namedTools.keySet().containsAll(toolNames)) {
                myParentLock.readLock().lock();
            }
            for (String curToolName : toolNames) {
                rTools.add(getTool(curToolName));
            }
        } finally {
            myNamedToolsReadWriteLock.readLock().unlock();
            if (this.myParentLock.getReadHoldCount() > parentReadCount) {
                myParentLock.readLock().unlock();
            }
        }
        return rTools;
    }

    public List<Object> getToolsByNames(String... toolClasses) {
        return getToolsByNames(Arrays.asList(toolClasses));
    }

    public List<Object> getTools(String... toolClasses) {
        return getToolsByNames(Arrays.asList(toolClasses));
    }

    @SuppressWarnings("unchecked")
    public <S> S getTool(Class<S> toolClass) {
        final S rTool;
        if (toolMap.containsKey(toolClass)) {
            rTool = (S) toolMap.get(toolClass);
        } else {
            if (this.mParent != null) {
                rTool = mParent.getTool(toolClass);
            } else {
                rTool = null;
            }
        }
        return rTool;
    }

    public List<Object> getTools(List<Class> toolClasses) {
        List<Object> rTools = new java.util.ArrayList<>(toolClasses.size());
        final int parentReadCount = this.myParentLock.getReadHoldCount();
        myToolMapReadWriteLock.readLock().lock();
        try {
            if (!toolMap.keySet().containsAll(toolClasses)) {
                myParentLock.readLock().lock();
            }
            for (Class curToolClass : toolClasses) {
                rTools.add(getTool(curToolClass));
            }
        } finally {
            myToolMapReadWriteLock.readLock().unlock();
            if (this.myParentLock.getReadHoldCount() > parentReadCount) {
                myParentLock.readLock().unlock();
            }
        }
        return rTools;
    }

    public List<Object> getTools(Class... toolClasses) {
        return getTools(Arrays.asList(toolClasses));
    }

    public List<Object> getTools(List<Class> toolClasses, List<String> toolNames) {
        List<Object> rTools = new java.util.ArrayList<>(toolClasses.size() + toolNames.size());
        final int parentReadCount = this.myParentLock.getReadHoldCount();
        myToolMapReadWriteLock.readLock().lock();
        try {
            myNamedToolsReadWriteLock.readLock().lock();
            try {
                if (!(toolMap.keySet().containsAll(toolClasses) && namedTools.keySet().containsAll(toolNames))) {
                    myParentLock.readLock().lock();
                }
                for (Class curToolClass : toolClasses) {
                    rTools.add(getTool(curToolClass));
                }
                for (String curToolName : toolNames) {
                    rTools.add(getTool(curToolName));
                }
            } finally {
                myNamedToolsReadWriteLock.readLock().unlock();
            }
        } finally {
            myToolMapReadWriteLock.readLock().unlock();
            if (this.myParentLock.getReadHoldCount() > parentReadCount) {
                myParentLock.readLock().unlock();
            }
        }
        return rTools;
    }

    /**
     * @return the addOnly
     */
    public boolean isAddOnly() {
        return addOnly.get();
    }

    /**
     * @param addOnly the addOnly to set
     */
    public void setAddOnly(boolean addOnly) {
        this.myToolMapReadWriteLock.configLock().lock();
        try {
            myNamedToolsReadWriteLock.configLock().lock();
            try {
                this.addOnly.set(addOnly);
            } finally {
                myNamedToolsReadWriteLock.configLock().unlock();
            }
        } finally {
            this.myToolMapReadWriteLock.configLock().unlock();
        }
    }

    /**
     * @param addOnly the addOnly to set
     */
    public void lazySetAddOnly(boolean addOnly) {
        new Thread(() -> setAddOnly(addOnly)).start();
    }

    /**
     * @return the addOnly
     */
    public boolean isSealed() {
        return this.sealed.get();
    }

    public void setSealed(boolean sealed) {
        this.myToolMapReadWriteLock.configLock().lock();
        try {
            myNamedToolsReadWriteLock.configLock().lock();
            try {
                this.sealed.set(sealed);
            } finally {
                myNamedToolsReadWriteLock.configLock().unlock();
            }
        } finally {
            this.myToolMapReadWriteLock.configLock().unlock();
        }
    }

    public void lazySetSealed(boolean sealed) {
        new Thread(() -> setSealed(sealed)).start();
    }

    /**
     * @return the mParent
     */
    public ToolSuite getParent() {
        ToolSuite myParent;
        myParentLock.readLock().lock();
        try {
            myParent = mParent;
        } finally {
            myParentLock.readLock().unlock();
        }
        return myParent;
    }

    /**
     * @param mParent the mParent to set
     */
    public void setParent(ToolSuite mParent) {
        myParentLock.writeLock().lock();
        try {
            this.mParent = mParent;
        } finally {
            myParentLock.writeLock().unlock();
        }
    }
}
