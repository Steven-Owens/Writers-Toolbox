package com.civprod.writerstoolbox.OpenNLP.training;
import java.awt.event.WindowEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Steven Owens
 */
@SuppressWarnings("serial")
public abstract class EditorWindow<T> extends javax.swing.JFrame {
    protected boolean done;
    private Runnable mRunnable;
    
    protected T createdObject;
    public EditorWindow(){
        this(null);
    }
    
    public EditorWindow(T createdObject){
        this.createdObject = createdObject;
        this.done = false;
        this.mRunnable = null;
        this.addWindowListener(new myWindowListener());
    }
    
    public void runOnClose(Runnable inRunnable){
        this.mRunnable = inRunnable;
    }

    /**
     * @return the createdObject
     */
    public T getCreatedObject() {
        return createdObject;
    }
    
    
    public class myWindowListener implements java.awt.event.WindowListener{

        @Override
        public void windowOpened(WindowEvent e) {}

        @Override
        public void windowClosing(WindowEvent e) {
            done = true;
            if (mRunnable != null){
                mRunnable.run();
            }
        }

        @Override
        public void windowClosed(WindowEvent e) {
            done = true;
        }

        @Override
        public void windowIconified(WindowEvent e) {}

        @Override
        public void windowDeiconified(WindowEvent e) {}

        @Override
        public void windowActivated(WindowEvent e) {}

        @Override
        public void windowDeactivated(WindowEvent e) {}
        
    }
}
