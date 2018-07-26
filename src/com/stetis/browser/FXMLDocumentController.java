/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stetis.browser;

import java.lang.Thread.State;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author HASHIM
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Button minimize;

    @FXML
    private Button maximize;

    @FXML
    private Button close;

    @FXML
    private TextField address;
    @FXML
    private WebView webView;

    @FXML
    private Text location;

    Stage stage = null;
    Rectangle2D rec2;
    Double w, h;
    WebEngine webEngine;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        rec2 = Screen.getPrimary().getVisualBounds();
        w = 0.1;
        h = 0.1;

        Platform.runLater(() -> {
            stage = (Stage) maximize.getScene().getWindow();
            stage.setMaximized(true);
            stage.setHeight(rec2.getHeight());
            maximize.getStyleClass().add("decoration-button-restore");

        });

        webEngine = webView.getEngine();
        webEngine.load("https://google.com");

        address.textProperty().bind(webEngine.locationProperty());

        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                location.setText(webEngine.getTitle());
                System.out.println("Agent = " + webEngine.getUserAgent());
            }
        });

    }
    //<editor-fold defaultstate="collapsed" desc="Maximize Window Button Action Perform">

    @FXML
    void loadPage(ActionEvent event) {
        Task task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                webEngine.load(address.getText());
                System.out.println("" + address.getText());
                return null;
            }
        };

    }

    @FXML
    private void maximized(ActionEvent event) {
        stage = (Stage) maximize.getScene().getWindow();
        if (stage.isMaximized()) {
            if (w == rec2.getWidth() && h == rec2.getHeight()) {
                stage.setMaximized(false);
                stage.setHeight(600);
                stage.setWidth(800);
                stage.centerOnScreen();
                maximize.getStyleClass().remove("decoration-button-restore");
            } else {
                stage.setMaximized(false);
                maximize.getStyleClass().remove("decoration-button-restore");
            }

        } else {
            stage.setMaximized(true);
            stage.setHeight(rec2.getHeight());
            maximize.getStyleClass().add("decoration-button-restore");
        }
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Minimise">

    @FXML
    private void minimize(ActionEvent event) {
        stage = (Stage) minimize.getScene().getWindow();
        if (stage.isMaximized()) {
            w = rec2.getWidth();
            h = rec2.getHeight();
            stage.setMaximized(false);
            stage.setHeight(h);
            stage.setWidth(w);
            stage.centerOnScreen();
            Platform.runLater(() -> {
                stage.setIconified(true);
            });
        } else {
            stage.setIconified(true);
        }
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Resize Window">

    @FXML
    private void resize(ActionEvent event) {
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Close window button Action perform">
    @FXML
    private void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
//</editor-fold>

}
