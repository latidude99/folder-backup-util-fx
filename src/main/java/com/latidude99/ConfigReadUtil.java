package com.latidude99;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConfigReadUtil {

    public static final String CONFIG_PATH = "FolderBackupUtilFX.config";
    public static List<String> excluded;
    public static String name = "none";
    public static String pathToCopy = "none";
    public static String pathToMove = "none";



    public static void readConfig()throws IOException{
        LineIterator iterator = null;
        File configFile;
        try {
            excluded = new ArrayList<>();
            configFile = new File(CONFIG_PATH);
            iterator = FileUtils.lineIterator(configFile, "UTF-8");

            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (!line.startsWith("#") && line.contains("=")) {
                    String[] params = line.split("=");

                    if(params.length > 1){
                        String option = params[0].trim();
                        switch(option){
                            case "exclude":
                                excluded.add(params[1].trim());
                                break;
                            case "name":
                                name = params[1].trim();
                            case "path.to.copy":
                                pathToCopy = params[1].trim();
                                break;
                            case "path.to.move":
                                pathToMove = params[1].trim();
                                break;
                        }
                    }
                }
            }
        }catch(IOException e){

        } finally {
            LineIterator.closeQuietly(iterator);
        }
    }

    public static void logConfig(String logName, String defaultName){
        Logger.clearLog(logName);

        if(name != null && !name.equals("none")){
            Logger.info = Logger.info + "ZIP archive name: " + name +
                    "\r\n\r\n";
        } else{
            Logger.info = Logger.info + "ZIP archive name: " + defaultName +
                    "\r\n";
        }
        for(String excl :excluded){
            Logger.info = Logger.info + "Excluded folder or file: " + excl +
                    "\r\n";
        }
        Logger.log(logName);
    }

}
