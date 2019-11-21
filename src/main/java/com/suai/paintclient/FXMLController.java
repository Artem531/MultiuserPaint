/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suai.paintclient;

import com.suai.multiuserpaint.updatercanvas.UpdaterCanvas;
import java.io.IOException;
import java.net.URL;
//import java.net.http.WebSocket;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

public class FXMLController implements Initializable {

    Listener listener;

    @FXML
    private Canvas canvas;

    String BaseColor = "#ffffff";

    @FXML
    private Slider slider;

    @FXML
    private CheckBox erase;


    @FXML
    private ColorPicker colorPicker;


    static boolean StartWriting = false;

    class Listener extends Thread{

        UpdaterCanvas updaterCanvas;

        Listener(Thread a, UpdaterCanvas updaterCanvas){
            super(a);
            this.updaterCanvas = updaterCanvas;
        }

        public void run(){
            while (true){
                updaterCanvas.updateCanvas();
            }

        }

    }

    public UpdaterCanvas updater;

    public UpdaterCanvas get_UpdaterCanvas(){
        return updater;
    }

    @FXML
    public void presed(MouseEvent event) throws IOException
    {
        //System.out.println("OnMousePressed");
        Double x = event.getX();
        Double y = event.getY();
        String action[] = new String[4];
        action[0] = "OnMousePressed";
        action[1] = x.toString();
        action[2] = y.toString();

        if (erase.isSelected()){
            action[3] = "erase";
        }
        else{
            action[3] = "draw";
        }

        MainWindowController.outputStream.writeObject(action);
        if (StartWriting == false){
            StartWriting = true;
            Listener thread =  new Listener(new Thread(), updater);
            this.listener = thread;
            thread.start();
        }
        //updater.updateCanvas();
    }
    
    @FXML
    public void draged(MouseEvent event) throws IOException
    {
        //System.out.println("OnMouseDragged");
        Double x = event.getX();
        Double y = event.getY();
        Double size = slider.getValue();
        String action[] = new String[6];
        action[0] = "OnMouseDragged";
        action[1] = x.toString();
        action[2] = y.toString();
        action[3] = size.toString();
        action[4] = colorPicker.getValue().toString();

        if (erase.isSelected()){
            action[5] = "erase";
        }
        else{
            action[5] = "draw";
        }

        MainWindowController.outputStream.writeObject(action);
        if (StartWriting == false){
            StartWriting = true;
            Listener thread =  new Listener(new Thread(), updater);
            thread.start();
        }
        //updater.updateCanvas();
    }


    // TODO can we see static in another Thread?
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        updater = new UpdaterCanvas(canvas);

        for(String [] action: SettingsController.getHystory())
            updater.updateCanvas(action);
   }
}
