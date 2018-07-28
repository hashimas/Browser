/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stetis.browser;

import java.lang.Thread.State;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.Color;
import java.awt.SystemColor;
import javafx.scene.effect.Effect;

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
    @FXML
    private ProgressIndicator page_progress;

    @FXML
    private AnchorPane homepage;

    @FXML
    private AnchorPane parentAnchor;
    @FXML
    private GridPane parentGrid;

    @FXML
    private HBox trans;

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
            page_progress.setVisible(true);
            initBrowser();
        });

        parentAnchor.setBackground(Background.EMPTY);
        parentGrid.setBackground(Background.EMPTY);
       
    }

    void initBrowser() {

        webEngine = webView.getEngine();
        webEngine.load("https://google.com");

        //address.textProperty().bind(webEngine.locationProperty());
        // updating progress bar using binding
        //page_progress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                location.setText(webEngine.getTitle());
                //page_progress.setVisible(true);
            }
            if (newValue == Worker.State.SUCCEEDED) {
                // hide progress bar then page is ready
                page_progress.setVisible(false);
            }

        });

    }

    @FXML
    void loadPage(ActionEvent event) {
        String addressText = "http://".concat(address.getText());
        // String securedAddressText = "https://".concat(address.getText());
        String googleSearch = "https://www.google.com.ng/search?q=" + address.getText() + "&oq=ghello&aqs=chrome..69i57j0l5.5840j0j4&sourceid=chrome&ie=UTF-8";
        if (address.getText().startsWith("http://")) {
            webEngine.load(address.getText());
            address.setText(webEngine.getLocation());

        } else if (address.getText().startsWith("https://")) {
            webEngine.load(address.getText());
            address.setText(webEngine.getLocation());
        } else if (address.getText().startsWith("www")) {
            webEngine.load(addressText);
            address.setText(webEngine.getLocation());
        } else {
            webEngine.load(googleSearch);
            address.setText(webEngine.getLocation());
        }

    }
    //<editor-fold defaultstate="collapsed" desc="Maximize Window Button Action Perform">

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

    @FXML
    void backward(ActionEvent event) {

    }

    @FXML
    void forward(ActionEvent event) {

    }

    @FXML
    void reload(ActionEvent event) {

    }
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
