package com.latidude99;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;

public class MainController {
    ZipTask zipTask;

    @FXML
    private TextArea infoArea;

    @FXML
    private Button stopBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private TextArea logArea;


    public void initialize() {
        logArea.setVisible(false);
        compressFolder();
    }

    private void compressFolder(){

        try {
            zipTask = new ZipTask();
        } catch (IOException e) {
            Arrays.asList(e.getStackTrace()).forEach(line -> Logger.log(
                    zipTask.logNameGenerated,"\n\r" + line));
            infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
            infoArea.setText("An error occurred during application start.       " +
                    "Please see FolderBackupUtil.log file for details.");
        }
        if (zipTask != null && progressBar != null){
            progressBar.progressProperty().bind(zipTask.progressProperty());
            infoArea.textProperty().bind(zipTask.messageProperty());


        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                zipTask.cancel();
                Stage stage = (Stage) cancelBtn.getScene().getWindow();
                stage.close();
            }
        });

        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(zipTask.isRunning()){
                    zipTask.cancel();
                    stopBtn.setText("Delete archive");
                }else{
                    deleteArchiveFile();
                }

            }
        });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        infoArea.textProperty().unbind();
                        infoArea.setStyle("-fx-text-fill: #66ff66; -fx-font: 14 verdana;");
                        infoArea.setText("\r\n  ZIP archive has been successfully created. " +
                                "  << Click here to see details >>");
                        cancelBtn.setText("Close");
                        cancelBtn.requestFocus();
                        cancelBtn.setOnKeyPressed(new EventHandler<KeyEvent>() {
                            @Override
                            public void handle(KeyEvent event) {
                                if (event.getCode().equals(KeyCode.ENTER)) {
                                    zipTask.cancel();
                                    Stage stage = (Stage) cancelBtn.getScene().getWindow();
                                    stage.close();
                                }
                            }
                        });
                        loadLog(zipTask.logNameGenerated);
                        if(ConfigReadUtil.pathToCopy != null
                                && !"none".equals(ConfigReadUtil.pathToCopy)){
                           copyArchive(zipTask.archiveNameGenerated);
                        }else if(ConfigReadUtil.pathToMove != null
                                && !"none".equals(ConfigReadUtil.pathToMove)){
                            moveArchive(zipTask.archiveNameGenerated);
                        }
                        stopBtn.setText("Delete log file");
                        stopBtn.setDisable(false);
                        deleteLogFile();
                        setupLogArea();
                    }
                });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        infoArea.textProperty().unbind();
                        infoArea.setStyle("-fx-text-fill: #e6b800; -fx-font: 14 verdana;");
                        infoArea.setText("\r\nOperation has been cancelled.");
                        stopBtn.setText("Delete archive file");
                        cancelBtn.setText("Close");
                    }
                });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        infoArea.textProperty().unbind();
                        infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
                        infoArea.setText("Operation failed. Check " + zipTask.logNameGenerated +
                        " for details");
                        Logger.log(zipTask.logNameGenerated,
                                "\n\r" + zipTask.getException().getLocalizedMessage() +
                                "\n\r");
                        stopBtn.setText("Delete archive file");
                        cancelBtn.setText("Close");
                    }
                });
        }
        Thread thread = new Thread(zipTask);
        thread.setDaemon(true);
        thread.start();


    }
    //  --------------------------------------------------------------------------------------------

    private boolean copyArchive(String archivePath){
        File archiveCreated = new File(archivePath);
        String destinationPath = ConfigReadUtil.pathToCopy + File.separator + ConfigReadUtil.name;
        File archiveCopied = new File(destinationPath);
        try {
            FileUtils.copyFile(archiveCreated, archiveCopied);
            infoArea.setStyle("-fx-text-fill: #66ff66; -fx-font: 12 verdana;");
            infoArea.setText("The archive file has been created and copied to: \r\n" +
                             destinationPath + "\r\n<< Click here to see details >>");
            stopBtn.setText("Delete original archive");
        } catch (IOException e) {
            infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
            infoArea.setText(
                    "An error occurred while copying the archive to: \r\n" + destinationPath);
            return false;
        }
        return true;
    }

    private boolean moveArchive(String archivePath){
        File archiveCreated = new File(archivePath);
        String destinationPath = ConfigReadUtil.pathToMove + File.separator + archiveCreated.getName();
        File archiveCopied = new File(destinationPath);
        try {
            FileUtils.copyFile(archiveCreated, archiveCopied);
            if(archiveCopied.length() == archiveCreated.length()){
                FileUtils.forceDelete(new File(zipTask.archiveNameGenerated));
                infoArea.setStyle("-fx-text-fill: #66ff66; -fx-font: 12 verdana;");
                infoArea.setText("The archive file has been created and moved to: \r\n" +
                destinationPath + "\r\n<< Click here to see details >>");
                stopBtn.setDisable(true);
            }else{
                infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
                infoArea.setText(
                "An error occurred while moving the archive to: \r\n" + destinationPath);
            }
        } catch (IOException e) {
            infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
            infoArea.setText(
                    "An error occurred while copying the archive to: \r\n" + destinationPath);
            return false;
        }
        return true;
    }


    private void deleteArchiveFile(){
        File file = new File(zipTask.archiveNameGenerated);
        try {
            FileUtils.forceDelete(file);
            infoArea.textProperty().unbind();
            infoArea.setStyle("-fx-text-fill: #66ff66; -fx-font: 14 verdana;");
            infoArea.setText("\r\nThe original archive file has been deleted.");
            stopBtn.setDisable(true);
            Logger.log(zipTask.logNameGenerated, "\r\nThe original archive file " +
                    zipTask.archiveNameGenerated + "  has been deleted.\r\n");
        } catch (NullPointerException | FileNotFoundException e) {
            infoArea.textProperty().unbind();
            infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
            infoArea.setText("\r\nThe original archive file has already been deleted.");
            Logger.log(zipTask.logNameGenerated, "\n\r" + e.getLocalizedMessage() +
                    "\n\r");
        } catch (IOException e){
            infoArea.textProperty().unbind();
            infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 12 verdana;");
            infoArea.setText("An error occurred, please try again in 10 seconds or see " +
                    "the log file:\r\n" + zipTask.logNameGenerated);
            Logger.log(zipTask.logNameGenerated, "\n\r" + e.getLocalizedMessage() +
                    "\n\r");
        }
        cancelBtn.requestFocus();
    }

    private void deleteLogFile(){
        stopBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                File file = new File(zipTask.logNameGenerated);
                try {
                    FileUtils.forceDelete(file);
                    infoArea.textProperty().unbind();
                    infoArea.setStyle("-fx-text-fill: #66ff66; -fx-font: 14 verdana;");
                    infoArea.setText("\r\nLog file has been deleted.");
                    stopBtn.setDisable(true);
                } catch (NullPointerException | FileNotFoundException e) {
                    infoArea.textProperty().unbind();
                    infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 14 verdana;");
                    infoArea.setText("\r\nLog file has already been deleted.");
                } catch (IOException e){
                    infoArea.setStyle("-fx-text-fill: #ff5c33; -fx-font: 14 verdana;");
                    infoArea.textProperty().unbind();
                    infoArea.setText("\r\nAn error occurred, please try again.");
                }
                cancelBtn.requestFocus();
            }
        });
    }

    private void setupLogArea(){
        infoArea.setOnMouseClicked(event -> {
            if(!logArea.isVisible()){
                logArea.setVisible(true);
                infoArea.setText("\r\nThe log file content. " +
                        "  << Click again to close log window >>");
            }else{
                logArea.setVisible(false);
                infoArea.setText("\r\n  ZIP archive has been successfully created. Click " +
                        "here " +
                        "to see details.");
            }

        });
    }

    private void loadLog(String logPath){
        File logFile = new File(logPath);
        try {
            String logContent = FileUtils.readFileToString(logFile, Charset.forName("UTF-8"));
            logArea.setText(logContent);
        } catch (IOException e) {
            MessageBox.show("An error occurred when reading log file:\r\n " +
                    e.getLocalizedMessage(), "Error");
        }
    }


}








