/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suai.paintclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SettingsController implements Initializable {

    public static LinkedList<String[]> history;
    @FXML
    private Button btnCreate;
    
    @FXML
    private TextField txtName;
    
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    
    @FXML
    private void create()
    {
        try
        {
            String[] require = new String[2];
            require[0] = "n";
            require[1] = txtName.getText();
            
            outputStream.writeObject(require);
            
            try
            {

             history = (LinkedList<String[]>) inputStream.readObject();

            } catch (IOException ex) {
                System.out.println("CanvasController" + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("CanvasController" + ex.getMessage());
            }
             catch (Exception ex) {
                System.out.println("CanvasController" + ex.getMessage());
            }

            Stage stage = new Stage();
            FXMLLoader loader  = new FXMLLoader();

            //loader.setLocation(getClass().getResource("Canvas.fxml"));
            loader.setLocation(getClass().getClassLoader().getResource("fxml/FXML.fxml"));

            Parent root = loader.load();

            //отправляем серверу сообщение

            System.out.println("Posle load");
            stage.setTitle("Multiuser Paint");
            stage.setResizable(false); //
            stage.setScene(new Scene(root));
            MainWindowController.stage.close();
            stage.show();
            
        }
        catch(IOException e)
        {
             System.out.println("14888 " + e.getMessage() + "ЫЫЫЫЫЫ");
        }
        
    }
    
    public static LinkedList<String[]>getHystory()
    {
        System.out.println("GET HISTORY");
        return history;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/MainWindow.fxml"));
        MainWindowController con = (MainWindowController)loader.getController();
        this.inputStream = MainWindowController.getIn();
        this.outputStream = MainWindowController.getOut();

    }    
    
}
