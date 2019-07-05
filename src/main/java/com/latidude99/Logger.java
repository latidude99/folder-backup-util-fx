package com.latidude99;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Logger {

    public static final String LOG_PATH = "FolderBackupUtilFX.log";
    public static String info = "";

    public static void log(String logName){
        File logFile;
        if(logName != null && !"".equals(logName)){
            logFile = new File(logName);
        }else{
            logFile = new File(LOG_PATH);
        }
        try{
            FileUtils.writeStringToFile(logFile, info, StandardCharsets.UTF_8, true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void log(String logName, String info){
        File logFile;
        if(logName != null && !"".equals(logName)){
            logFile = new File(logName);
        }else{
            logFile = new File(LOG_PATH);
        }
        try{
            FileUtils.writeStringToFile(logFile, info, StandardCharsets.UTF_8, true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void clearLog(String logName){
        File logFile;
        if(logName != null && !"".equals(logName)){
            logFile = new File(logName);
        }else{
            logFile = new File(LOG_PATH);
        }
        try {
            FileUtils.writeStringToFile(logFile, "", StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

















