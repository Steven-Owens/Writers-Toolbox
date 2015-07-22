/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.civprod.writerstoolbox.apps;

import com.civprod.writerstoolbox.apps.ToolSuite;
import java.util.logging.Logger;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;

/**
 *
 * @author Steven Owens
 */
public class Context {

    private Context mParent;
    private ToolSuite mToolSuite;
    private Configuration mConfiguration;
    private String mLoggerNamePart;
    private final boolean modifiable;
    private boolean useIndependentLogger;
    private final java.util.concurrent.locks.ReadWriteLock ParentLock;
    private final java.util.concurrent.locks.ReadWriteLock ToolSuiteLock;
    private final java.util.concurrent.locks.ReadWriteLock ConfigurationLock;
    private final java.util.concurrent.locks.ReadWriteLock LoggerLock;

    public Context(Context inParent) {
        this(inParent, new ToolSuite(inParent.getToolSuite()), createConfigurationFromParent(inParent), java.util.UUID.randomUUID().toString(), inParent.isModifiable(), false);
    }

    private Context(Context inParent, ToolSuite inToolSuite, Configuration inConfiguration, String inLoggerName, boolean modifiable, boolean useIndependentLogger) {
        mParent = inParent;
        mToolSuite = inToolSuite;
        mConfiguration = inConfiguration;
        mLoggerNamePart = inLoggerName;
        this.modifiable = modifiable;
        this.useIndependentLogger = useIndependentLogger;
        if (modifiable) {
            ParentLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
            ToolSuiteLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
            ConfigurationLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
            LoggerLock = new java.util.concurrent.locks.ReentrantReadWriteLock();
        } else {
            ParentLock = null;
            ToolSuiteLock = null;
            ConfigurationLock = null;
            LoggerLock = null;
        }
    }

    /**
     * @return the mToolSuite
     */
    public ToolSuite getToolSuite() {
        ToolSuite rToolSuite;
        if (modifiable) {
            ToolSuiteLock.readLock().lock();
        }
        try {
            rToolSuite = mToolSuite;
        } finally {
            if (modifiable) {
                ToolSuiteLock.readLock().unlock();
            }
        }
        return rToolSuite;
    }

    /**
     * @param inToolSuite the mToolSuite to set
     */
    public void setToolSuite(ToolSuite inToolSuite) throws UnsupportedOperationException {
        if (!modifiable) {
            throw new UnsupportedOperationException("Can't modify an unmodifiable context");
        }
        ToolSuiteLock.writeLock().lock();
        try {
            this.mToolSuite = inToolSuite;
        } finally {
            ToolSuiteLock.writeLock().unlock();
        }
    }

    /**
     * @return the mConfiguration
     */
    public Configuration getConfiguration() {
        Configuration rConfiguration;
        if (modifiable) {
            ConfigurationLock.readLock().lock();
        }
        try {
            rConfiguration = mConfiguration;
        } finally {
            if (modifiable) {
                ConfigurationLock.readLock().unlock();
            }
        }
        return rConfiguration;
    }

    /**
     * @param inConfiguration the mConfiguration to set
     */
    public void setConfiguration(Configuration inConfiguration) throws UnsupportedOperationException {
        if (!modifiable) {
            throw new UnsupportedOperationException("Can't modify an unmodifiable context");
        }
        ConfigurationLock.writeLock().lock();
        try {
            this.mConfiguration = inConfiguration;
        } finally {
            ConfigurationLock.writeLock().unlock();
        }
    }

    /**
     * @return the modifiable
     */
    public boolean isModifiable() {
        return modifiable;
    }

    /**
     * @return the mParent
     */
    public Context getParent() {
        Context rParent;
        if (modifiable) {
            ParentLock.readLock().lock();
        }
        try {
            rParent = mParent;
        } finally {
            if (modifiable) {
                ParentLock.readLock().unlock();
            }
        }
        return rParent;
    }

    /**
     * @param inParent the mParent to set
     */
    public void setParent(Context inParent) throws UnsupportedOperationException {
        if (!modifiable) {
            throw new UnsupportedOperationException("Can't modify an unmodifiable context");
        }
        ParentLock.writeLock().lock();
        try {
            this.mParent = inParent;
        } finally {
            ParentLock.writeLock().unlock();
        }
    }

    /**
     * @return the mLogger
     */
    public String getLoggerName() {
        String rLoggerName = "";
        if (!this.useIndependentLogger) {
            if (this.mParent != null) {
                rLoggerName = mParent.getLoggerName() + ".";
            } else {
                rLoggerName = Logger.GLOBAL_LOGGER_NAME + ".";
            }
        }
        if (modifiable) {
            LoggerLock.readLock().lock();
        }
        try {
            rLoggerName += mLoggerNamePart;
        } finally {
            if (modifiable) {
                LoggerLock.readLock().unlock();
            }
        }
        return rLoggerName;
    }

    public Logger getLogger() {
        return Logger.getLogger(getLoggerName());
    }

    /**
     * @param inLogger the mLogger to set
     */
    public void setLoggerNamePart(String inLoggerName) {
        if (!modifiable) {
            throw new UnsupportedOperationException("Can't modify an unmodifiable context");
        }
        LoggerLock.writeLock().lock();
        try {
            this.mLoggerNamePart = inLoggerName;
        } finally {
            LoggerLock.writeLock().unlock();
        }
    }

    private static Configuration createConfigurationFromParent(Context inParent) {
        CompositeConfiguration tempCompositeConfiguration = new CompositeConfiguration();
        if (inParent != null) {
            tempCompositeConfiguration.addConfiguration(inParent.getConfiguration());
        }
        return tempCompositeConfiguration;
    }

    /**
     * @return the useIndependentLogger
     */
    public boolean useIndependentLogger() {
        boolean rUseIndependentLogger;
        if (modifiable) {
            LoggerLock.readLock().lock();
        }
        try {
            rUseIndependentLogger = useIndependentLogger;
        } finally {
            if (modifiable) {
                LoggerLock.readLock().unlock();
            }
        }
        return rUseIndependentLogger;
    }

    /**
     * @param useIndependentLogger the useIndependentLogger to set
     */
    public void setUseIndependentLogger(boolean useIndependentLogger) {
        if (!modifiable) {
            throw new UnsupportedOperationException("Can't modify an unmodifiable context");
        }
        LoggerLock.writeLock().lock();
        try {
            this.useIndependentLogger = useIndependentLogger;
        } finally {
            LoggerLock.writeLock().unlock();
        }
    }

    public static class Builder {

        private Context mParent = null;
        private ToolSuite mToolSuite = null;
        private Configuration mConfiguration = null;
        private String mLoggerNamePart = null;
        private boolean modifiable = true;
        private boolean useIndependentLogger = false;

        /**
         * @return the mParent
         */
        public Context getParent() {
            return mParent;
        }

        /**
         * @param mParent the mParent to set
         */
        public Builder setParent(Context mParent) {
            this.mParent = mParent;
            return this;
        }

        /**
         * @return the mToolSuite
         */
        public ToolSuite getToolSuite() {
            return mToolSuite;
        }

        /**
         * @param mToolSuite the mToolSuite to set
         */
        public Builder setToolSuite(ToolSuite mToolSuite) {
            this.mToolSuite = mToolSuite;
            return this;
        }

        /**
         * @return the mConfiguration
         */
        public Configuration getConfiguration() {
            return mConfiguration;
        }

        /**
         * @param mConfiguration the mConfiguration to set
         */
        public Builder setConfiguration(Configuration mConfiguration) {
            this.mConfiguration = mConfiguration;
            return this;
        }

        /**
         * @return the modifiable
         */
        public boolean isModifiable() {
            return modifiable;
        }

        /**
         * @param modifiable the modifiable to set
         */
        public Builder setModifiable(boolean modifiable) {
            this.modifiable = modifiable;
            return this;
        }

        public Context build() {
            if (this.mToolSuite == null) {
                if (this.mParent != null) {
                    ToolSuite toolSuite = mParent.getToolSuite();
                    mToolSuite = new ToolSuite(mParent.getToolSuite());
                    if (toolSuite.isSealed()) {
                        mToolSuite.setSealed(true);
                    }
                } else {
                    mToolSuite = new ToolSuite();
                }
            }
            if (this.mConfiguration == null) {
                mConfiguration = createConfigurationFromParent(mParent);
            }
            if (mLoggerNamePart == null) {
                mLoggerNamePart = "";
                mLoggerNamePart += java.util.UUID.randomUUID().toString();
            }
            return new Context(mParent, mToolSuite, mConfiguration, mLoggerNamePart, modifiable, useIndependentLogger);
        }

        /**
         * @return the mLogger
         */
        public String getLoggerNamePart() {
            return mLoggerNamePart;
        }

        /**
         * @param mLogger the mLogger to set
         */
        public Builder setLoggerNamePart(String inLoggerName) {
            this.mLoggerNamePart = inLoggerName;
            return this;
        }

        public Builder setLoggerName(String inLoggerName) {
            return setUseIndependentLogger(true).setLoggerNamePart(inLoggerName);
        }

        /**
         * @return the useIndependentLogger
         */
        public boolean isUseIndependentLogger() {
            return useIndependentLogger;
        }

        /**
         * @param useIndependentLogger the useIndependentLogger to set
         */
        public Builder setUseIndependentLogger(boolean useIndependentLogger) {
            this.useIndependentLogger = useIndependentLogger;
            return this;
        }
    }
}
