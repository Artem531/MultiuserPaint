package com.suai.paintclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.suai.multiuserpaint.server.UserThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindowController implements Initializable {
    
    private Socket socket;
    
    private  InetAddress IP;
    
    private int port = 12347;
    
    public static ObjectOutputStream outputStream ;

    public static ObjectInputStream inputStream ;

    public static Stage stage;
    
    @FXML
    private  ListView<String> list_of_view;

    @FXML
    private Button btnCreate;

    @FXML
    void createRoom(ActionEvent actionEvent)
    {
        try
        {

            Stage stageS = new Stage();
            FXMLLoader loader  = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("fxml/Settings.fxml"));
            Parent root = loader.load();
            
            stageS.setTitle("Настройки комнаты");
            stageS.setResizable(false); //
            stageS.setScene(new Scene(root));
            stageS.initModality(Modality.WINDOW_MODAL); 
            //actionEvent.
            stageS.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stageS.show();
        }
        catch(IOException e)
        {
             System.out.println("MainWindowController.createRoom() " + e.getMessage());
        }
    }


    void show_join(){

        FXMLLoader loader1  = new FXMLLoader();

//            loader.setLocation(getClass().getResource("/fxml/Canvas.fxml"));
        loader1.setLocation(getClass().getClassLoader().getResource("fxml/Connection_settings.fxml"));

        Parent root1 = null;
        try {
            root1 = loader1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stages = new Stage();
        stages.setTitle("Multiuser Paint");
        stages.setResizable(false); //
        stages.setScene(new Scene(root1));
        MainWindowController.stage.close();
        stages.show();

    }

    @FXML
    private void join()
    {
       try
        {
            String[] require = new String[2];
            require[0] = "j";
            // TODO get room from string[1]
            Scanner scanner = new Scanner(System.in);

            require[1] = scanner.nextLine();

            //show_join();

//            try {
//                require = (String[]) inputStream.readObject();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }

            outputStream.writeObject(require);
            String anwser = "";
            try {
                anwser = (String) inputStream.readObject();

                if (anwser.compareTo("Room does not exist!") == 0){
                    System.out.println(anwser);
                    return;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try
            {
//                SettingsController.history

             // TODO add first call history exception
             SettingsController.history = (LinkedList<String[]>) inputStream.readObject();

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

//            loader.setLocation(getClass().getResource("/fxml/Canvas.fxml"));
            loader.setLocation(getClass().getClassLoader().getResource("fxml/FXML.fxml"));
            
            Parent root = loader.load();


            stage.setTitle("User" + anwser);
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
            
    public static ObjectInputStream getIn()
    {
        return inputStream;
    }
    public static ObjectOutputStream getOut()
    {
        return outputStream;
    }

    
    public void initialize(URL url, ResourceBundle rb) {
        
        connect();
       // updateListRooms();
        //получение и вывод списка комнат
    }

    @FXML
    private void UpdateList(){

        String [] rooms;
        String [] mess = new String[1];
        mess[0] = "Update_list";
        try {
            outputStream.writeObject(mess);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            rooms = (String[]) inputStream.readObject();
            ObservableList<String> items = FXCollections.observableArrayList (rooms);
            list_of_view.setItems(items);
        } catch (IOException e) {
            rooms = new String[1];
            rooms[0] = "empty";
            ObservableList<String> items = FXCollections.observableArrayList (rooms);
            list_of_view.setItems(items);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            rooms = new String[1];
            rooms[0] = "empty";
            ObservableList<String> items = FXCollections.observableArrayList (rooms);
            list_of_view.setItems(items);
            e.printStackTrace();
        }
    }

    private boolean connect()
    {
        try {
            String Addr = "";
            System.out.println("Write Server inet address");
            Scanner scanner = new Scanner(System.in);
            Addr = scanner.nextLine();
            IP = InetAddress.getByName(Addr);
            socket = new Socket(IP, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            return true;
        } catch (UnknownHostException ex) {
            System.out.println("MainWindowController.connect() " + ex.getMessage());
            return false;
        } catch (IOException ex) {
            return false;
        }
    }
    
    private void updateListRooms()
    {
        try 
        {
            String[] rooms = (String[])inputStream.readObject();
            //сделать цикл и запустить его в потоке, для постоянного обновления комнат
        } catch (IOException ex) 
        {
            System.out.println("MainWindowController.udateListRooms() " + ex.getMessage());
        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("MainWindowController.udateListRooms() " + ex.getMessage());
        }
        
        
    }
}
