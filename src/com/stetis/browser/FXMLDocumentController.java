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
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory.Entry;

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

    @FXML
    private ImageView reloadicon;

    @FXML
    private ImageView next;

    @FXML
    private ImageView previous;

    @FXML
    private TextField stetis_search_field;

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

        stetis_search_field.setOnMouseEntered((MouseEvent event) -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(-0.1);
            homepage.setEffect(colorAdjust);
        });
        stetis_search_field.setOnMouseExited((MouseEvent event) -> {
            ColorAdjust colorAdjust = new ColorAdjust();
            homepage.setEffect(null);
        });

    }

    void initBrowser() {

        webEngine = webView.getEngine();
        address.setText("http://www.stetis.com");

        //address.textProperty().bind(webEngine.locationProperty());
        // updating progress bar using binding
        //page_progress.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
        webEngine.getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) -> {
            if (Worker.State.SUCCEEDED.equals(newValue)) {
                location.setText(webEngine.getTitle());
                homepage.setVisible(false);
                webView.setVisible(true);
                page_progress.setVisible(false);
            }
        });

    }

    void load(String add) {
        //String one = "C:\Users\HASHIM\Desktop\SAMS Proposal\instructions.html";
        //String two = "file:///C:/Users/HASHIM/Desktop/SAMS%20Proposal/instructions.html";

        String filePath = "file:\\\\".concat(add);

        String addressText = "http://".concat(add);
        // String securedAddressText = "https://".concat(address.getText());
        String googleSearch = "https://www.google.com.ng/search?q=" + add + "&oq=" + add + "&aqs=stetisweb..69i57j0l5.5840j0j4&sourceid=stetisweb&ie=UTF-8";
        if (add.startsWith("http://")) {
            webEngine.load(add);
            address.setText(webEngine.getLocation());

        } else if (add.startsWith("https://")) {
            webEngine.load(add);
            address.setText(webEngine.getLocation());
        } else if (add.startsWith("www")) {
            webEngine.load(addressText);
            address.setText(webEngine.getLocation());
        } else if (add.startsWith("C:") | add.startsWith("D:") | add.startsWith("E:") | add.startsWith("F:") | add.startsWith("G:")) {
            webEngine.load(filePath);
        } else {
            webEngine.load(googleSearch);
            address.setText(webEngine.getLocation());
        }
    }

    @FXML
    void loadPage(ActionEvent event) {
        load(address.getText());

    }

//    Since the current visited page is listed in the Entries at the lastPosition (sizeHistory-1),
//     you will need to access the one index before (sizeHistory-2) in order to get the last visited page
    @FXML
    void backward(MouseEvent event) {
        int sizeHistory = webEngine.getHistory().getEntries().size();
        if (sizeHistory > 1) {
            Entry entry = webEngine.getHistory().getEntries().get(sizeHistory - 2);
            load(entry.getUrl());
        }

    }

    @FXML
    void forward(MouseEvent event) {

        int sizeHistory = webEngine.getHistory().getEntries().size();
        if (sizeHistory > 1) {
            Entry entry = webEngine.getHistory().getEntries().get(sizeHistory + 2);
            load(entry.getUrl());
        }
    }

    @FXML
    void reloadPage(MouseEvent event) {
        int sizeHistory = webEngine.getHistory().getEntries().size();
        if (sizeHistory > 1) {
            Entry entry = webEngine.getHistory().getEntries().get(sizeHistory - 1);
            load(entry.getUrl());
        }

    }

    @FXML
    void showHomePage(MouseEvent event) {
        webView.setVisible(false);
        homepage.setVisible(true);
        address.setText("http://www.stetis.com");
        stetis_search_field.clear();

    }

    @FXML
    void stetisSearchAction(ActionEvent event) {
        load(stetis_search_field.getText());
        //stetis_search_field.setVisible(false);
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

//<editor-fold defaultstate="collapsed" desc="Close window button Action perform">
    @FXML
    private void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
//</editor-fold>

}
