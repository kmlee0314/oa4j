/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etm.api.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord; 

/**
 *
 * @author vogler
 */
public class Debug {    
    
    public static final java.util.logging.Logger out = java.util.logging.Logger.getLogger(Debug.class.getName());
    public static final String fmtDate = "yyyy.MM.dd HH:mm:ss.SSS";

    private static class LogFormatter extends Formatter {
        @Override
        public String format(LogRecord record) {
            SimpleDateFormat fmt = new SimpleDateFormat(Debug.fmtDate);
            String formattedMessage = formatMessage(record);
            return fmt.format(new Date()) + " " + String.format("%-60s %-7s", record.getSourceClassName() + "." + record.getSourceMethodName(), record.getLevel()) + ": " + formattedMessage + "\n";
        }
    }

    public static void setOutput(String filename) throws IOException  {
        FileHandler fileHandler = new FileHandler(filename+".%g.log", 5242880, 5, true);        
        fileHandler.setFormatter(new LogFormatter());
        out.addHandler(fileHandler);
        out.setUseParentHandlers(false);        
        
        File file = new File(filename+".err");
        FileOutputStream fos = new FileOutputStream(file);
        PrintStream ps = new PrintStream(fos);
        System.setErr(ps);       
        System.setOut(ps);
    }    
    
    public static void setLevel(Level level) {
        Debug.out.setLevel(level);
    }    
    
    public static void StackTrace(Level level, Exception ex) {
        String trace = ex.toString()+":\n";
        for (StackTraceElement ste : ex.getStackTrace()) {
                trace += "  " + ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber()+"\n";
        }
        Debug.out.log(level, trace);
    }    
    
    public static void StackTrace(Level level, String msg) {
        String trace = msg+":\n";
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
                trace += "  " + ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber()+"\n";
        }
        Debug.out.log(level, trace);
    }        
    
    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Debug.StackTrace(Level.SEVERE, ex);
        }
    }    
}

//-----------------------------------------------------------------------------------------
