package com.suai.multiuserpaint.updatercanvas;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import com.suai.paintclient.MainWindowController;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;



public class UpdaterCanvas implements  Serializable{


    class Drawer extends Thread{

        String name;
        UpdaterCanvas updaterCanvas;

        Drawer(String name, UpdaterCanvas updaterCanvas){
            this.name = name;
            this.updaterCanvas = updaterCanvas;
        }

        public void run(){
            while (true){
                String [] request = null;
                try {
                    String name = (String) MainWindowController.getIn().readObject();
                    request = (String []) MainWindowController.getIn().readObject();

                    if (this.name.equals(name)){
                        updaterCanvas.updateCanvas(request);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    transient HashMap<String, Drawer> ListOfDrawes = new HashMap<String, Drawer>();

    transient Canvas canvas;

    transient GraphicsContext graphicsContext;
      //координаты для рисования

    private double xOld;
    private double yOld;
    
    public UpdaterCanvas(Canvas canvas)
    {
        this.canvas = canvas;
        this.graphicsContext = canvas.getGraphicsContext2D();
        
    }
    
    public UpdaterCanvas(GraphicsContext contex)
    {

        this.graphicsContext = contex;
        this.canvas = this.graphicsContext.getCanvas();
    }



    public void updateCanvas()
    {
        try {
            String [] message = (String [])MainWindowController.getIn().readObject();
            this.updateCanvas(message);
            System.out.println(message);

        } catch (IOException e) {
            //updateCanvas();
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            //updateCanvas();
            e.printStackTrace();
        }

    }

    public synchronized void updateCanvas(String [] changes)
    {

        if (changes[0].compareTo("OnMousePressed") == 0){
            OnMousePressed(changes);
        }
        else if (changes[0].compareTo("OnMouseDragged") == 0){
            OnMouseDragged(changes);
        }
    }

    //формат OnMousePressed x, y
    public void OnMousePressed(String[] action)
    {

        xOld = Double.parseDouble(action[1]);
        yOld = Double.parseDouble(action[2]);
        
    }

    //формат OnMousePressed x, y, size, color 
    public void OnMouseDragged(String[] action) {

        double x = Double.parseDouble(action[1]);
        double y = Double.parseDouble(action[2]);
        double size = Double.parseDouble(action[3]);
        String color = action[4];

        if (action[5].compareTo( "erase" )  == 0) {

            graphicsContext.setLineWidth(size);
            graphicsContext.setStroke(Color.web("#ffffff"));
            graphicsContext.strokeLine(x, y, x, y);

            return;
        }

        graphicsContext.setLineWidth(size);
        graphicsContext.setStroke(Color.web(color));
        graphicsContext.strokeLine(x, y, x, y);

    }

}
