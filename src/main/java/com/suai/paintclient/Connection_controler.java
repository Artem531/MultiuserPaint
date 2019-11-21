package com.suai.paintclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Connection_controler implements Initializable {

    @FXML
    private javafx.scene.control.TextField txtName;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainWindow.fxml"));
        MainWindowController con = (MainWindowController)loader.getController();
        this.inputStream = MainWindowController.getIn();
        this.outputStream = MainWindowController.getOut();

    }

    public void join() {

        try {
            String[] require = new String[2];
            require[0] = "j";
            require[1] = txtName.getText();

            outputStream.writeObject(require);

            this.outputStream.writeObject(txtName);
            //MainWindowController.NotReadyToRead = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


