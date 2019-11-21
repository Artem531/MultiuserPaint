/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suai.multiuserpaint.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.suai.paintclient.FXMLController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class UserThread extends Thread{
    private Socket socket;
    private String name;
    private ObjectOutputStream outputStream ;
    private ObjectInputStream inputStream;
    private Room room;
    private User user;
    static int id = -1;

    public UserThread(Socket socket) 
    {
        this.id++;
        this.socket = socket;
        System.out.println("Join: "  + socket.getInetAddress().toString());
        Server.LOGGER.info("Join: "  + socket.getInetAddress().toString());

        this.start();
    }
    
    public void run() 
    {
        try
        {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());

            while(true) {

                //получаем ответ о создании или подключении к комнате
                String[] answer = (String[])inputStream.readObject();

                while ((answer[0].compareTo("n") != 0 && answer[0].compareTo("j") != 0) == true) {
                    if (answer[0].compareTo("Update_list") == 0) {
                        String[] cur_rooms = Server.getRoomList().getRooms();
                        if (cur_rooms.length != 0) {
                            try {
                                outputStream.writeObject(cur_rooms);
                                outputStream.flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                                Server.LOGGER.info(e.getMessage());
                            }
                        } else {
                            try {
                                String[] tmp = new String[1];
                                tmp[0] = "empty";
                                outputStream.writeObject(tmp);
                                outputStream.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Server.LOGGER.info(e.getMessage());

                            }
                        }
                    }
                    answer = (String[]) inputStream.readObject();
                }


                if (answer[0].equals("n")) {
                    System.out.println("Client: " + (id - 1) + " creating room with name: " + answer[1]);
                    Server.LOGGER.info("Client: " + (id - 1) + " creating room with name: " + answer[1]);

                    user = new User(socket, outputStream, inputStream);
                    room = new Room(user, answer[1]);
                    Server.getRoomList().add(room);
                    break;

                    }


                if (answer[0].compareTo("j") == 0) {

                    if (Server.getRoomList().getRoom(answer[1]) != null) {
                        String [] mess = new String[1];
                        mess[0] = new Integer(id).toString();
                        outputStream.writeObject(mess[0]);


                        room = Server.getRoomList().getRoom(answer[1]);
                        user = new User(socket, outputStream, inputStream);
                        room.join(user);

                    }
                    else {

                        String [] mess = new String[1];
                        mess[0] = "Room does not exist!";
                        outputStream.writeObject(mess[0]);
                        continue;
                    }

                    break;
                }
            }

            while(true)
            {
                String[] changes = (String[])inputStream.readObject();

                room.addAction(user, changes);
                
            }
	    }
        catch(IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("User:" + user.getSocket().getInetAddress() + " EXEPTION: " + e.getMessage());
            Server.LOGGER.info("User:" + user.getSocket().getInetAddress() + " EXEPTION: " + e.getMessage());
            room.quit(user);

        } catch (ClassNotFoundException ex) 
        {
            
        } 

    }
}
