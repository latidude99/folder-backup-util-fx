package com.latidude99;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

public class MainController {
    ZipTask zipTask;

    @FXML
    private Label pathLbl;

    @FXML
    private Button stopBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private ProgressBar progressBar;

    public void initialize() {
        compressFolder();
    }


    private void compressFolder(){

        try {
            zipTask = new ZipTask();
        } catch (IOException e) {
            Logger.log(zipTask.logNameGenerated, e.getStackTrace().toString());
            MessageBox.show("An error occurred during application start. " +
                    "Please see FolderBackupUtil.log file for details.", "Error");
        }

        progressBar.setStyle(" -fx-progress-color: beige;");
        progressBar.progressProperty().bind(zipTask.progressProperty());
        pathLbl.textProperty().bind(zipTask.messageProperty());

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
                    stopBtn.setText("Delete ZIP archive");
                }else{
                    File file = new File(zipTask.archiveNameGenerated);
                    try {
                        FileUtils.forceDelete(file);
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("ZIP archive has been deleted.");
                        Logger.log(zipTask.logNameGenerated, "\r\nZIP archive  " +
                                zipTask.archiveNameGenerated + "  has been deleted.\r\n");
                    } catch (NullPointerException | FileNotFoundException e) {
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("ZIP archive has already been deleted.");
                        Logger.log(zipTask.logNameGenerated, "\n\r" + e.getLocalizedMessage() +
                                "\n\r");
//                        Arrays.asList(e.getStackTrace())
//                                .forEach(line -> Logger.log(zipTask.logNameGenerated,
//                                        "\n\r" + line));
                    } catch (IOException e){
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("\r\nError occurred while deleting ZIP archive\r\n");
                        Logger.log(zipTask.logNameGenerated, "\n\r" + e.getLocalizedMessage() +
                                "\n\r");
//                        Arrays.asList(e.getStackTrace())
//                                .forEach(line -> Logger.log(zipTask.logNameGenerated,
//                                        "\n\r" + line));
                    }
                }

            }
        });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("ZIP archive has been successfully created.");
                        stopBtn.setText("Delete ZIP archive");
                        cancelBtn.setText("Close");
                    }
                });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("Operation has been cancelled.");
                        stopBtn.setText("Delete ZIP archive");
                        cancelBtn.setText("Close");
                    }
                });

        zipTask.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED,
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        pathLbl.textProperty().unbind();
                        pathLbl.setText("Operation failed." +
                                " Check FolderBackupUtil.log for details");
                        Logger.log(zipTask.logNameGenerated,
                                "\n\r" + zipTask.getException().getLocalizedMessage() +
                                "\n\r");
//                        Arrays.asList(zipTask.getException().getStackTrace())
//                                .forEach(line -> Logger.log(zipTask.logNameGenerated,
//                                        "\n\r" + line));
                        stopBtn.setText("Delete ZIP archive");
                        cancelBtn.setText("Close");
                    }
                });

        Thread thread = new Thread(zipTask);
        thread.setDaemon(true);
        thread.start();

    }


}














