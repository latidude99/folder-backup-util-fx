package com.latidude99;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTask extends Task <Void>{
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm");
    LocalDateTime localDateTime;
    private List <String> fileList;
    private List<String> excluded;
    private String archiveNameDefined;
    public String archiveNameGenerated;
    public String logNameGenerated;
    String sourceFolder;

    public ZipTask() throws IOException {
        fileList = new ArrayList <>();
        ConfigReadUtil.readConfig();
        excluded = ConfigReadUtil.excluded;
        archiveNameDefined = ConfigReadUtil.name;
    }


    @Override
    protected Void call() throws Exception {
        archiveNameGenerated = getBackupPathAndName(archiveNameDefined);
        logNameGenerated = archiveNameGenerated.substring(0, archiveNameGenerated.length() -3) +
                "log";
        ConfigReadUtil.logConfig(logNameGenerated, archiveNameGenerated);

        sourceFolder = getSourceFolder();

        generateFileList(excluded, sourceFolder, new File(sourceFolder));
        zipIt(sourceFolder, archiveNameGenerated);

        return null;
    }


    public void zipIt(String sourceFolder, String zipFile) {
        long numberOfFiles = fileList.size();
        long archived = 0;

        byte[] buffer = new byte[1024];
        String source = new File(sourceFolder).getName();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            FileInputStream in = null;

            for (String file: this.fileList) {
                if(this.isCancelled()){
                    this.updateMessage("Operation has been canceled.");
                    return;
                }
                Logger.log(logNameGenerated, "\r\nadding file : " + file);
                ZipEntry zipEntry = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(zipEntry);
                try {
                    in = new FileInputStream(sourceFolder + File.separator + file);
                    int len;
                    while ((len = in .read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                } finally {
                    in.close();
                }
            archived += 1;
            this.updateProgress(archived, numberOfFiles);
            this.updateMessage("added: " + file);
            Logger.log(logNameGenerated, " ....OK");
            }

            zos.closeEntry();
            Logger.log(logNameGenerated, "\r\n\r\n" +
                    "Folder  " + sourceFolder + "  has been successfully compressed\r\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void generateFileList(List<String> excluded, String sourceFolder, File node) {
//      List<String> argsList = argsToList(args);
        int count= 0;

        // adds file only
        if (node.isFile()) {
            count = 0;
            for (String name : excluded) {
                if (node.getName().equals(name))
                    count++;
            }
            if(count == 0)
                fileList.add(generateZipEntry(sourceFolder, node.toString()));
        }

        if (node.isDirectory()) {
            count = 0;
            for (String name : excluded) {
                if (node.getName().equals(name))
                    count++;
            }
            if(count == 0){
                String[] subNode = node.list();
                for (String filename: subNode) {
                    generateFileList(excluded, sourceFolder, new File(node, filename));
                }
            }
        }
    }

    private String generateZipEntry(String sourceFolder, String file) {
        return file.substring(sourceFolder.length() + 1, file.length());
    }

//    private List<String> argsToList(String[] args){
//        List<String> argsList;
//        if(args.length >0)
//            argsList = Arrays.asList(args);
//        else
//            argsList = new ArrayList<>();
//
//        return argsList;
//    }

    public String getSourceFolder(){
        File tmpFile = new File("tmp2.txt");
        String sourceFolder = tmpFile.getAbsolutePath();
        sourceFolder = sourceFolder.substring(0, sourceFolder.lastIndexOf("\\"));
        tmpFile.delete();
        return sourceFolder;
    }

    public String getBackupPathAndName(String customName){
        String pathAndName;
        File tmpFile = new File("tmp1.txt");
        String path = tmpFile.getAbsolutePath();
        path = path.substring(0, path.lastIndexOf("\\"));
        String folder = path.substring(path.lastIndexOf("\\"));
        localDateTime = LocalDateTime.now();
        String time = localDateTime.format(formatter);
        if(customName != null && !customName.equals("")){
            pathAndName = path + File.separator + customName;
        }else{
            pathAndName = path +
                    File.separator +
                    folder +
                    "_backup_" +
                    time +
                    ".zip";
        }
        tmpFile.delete();
        return pathAndName;
    }



//
//    public boolean moveBackupFile(String backupFileName, File file){
//        File backupFile = new File(backupFileName);
//
//        if(backupFile.renameTo
//                (new File("E:\\___BACKUP\\backup.zip"))) {
//            file.delete();
//            System.out.println("File moved successfully");
//            return true;
//        } else {
//            System.out.println("Failed to move the file");
//            return false;
//        }
//    }
}
























