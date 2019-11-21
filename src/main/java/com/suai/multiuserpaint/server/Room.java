
package com.suai.multiuserpaint.server;

import com.suai.multiuserpaint.history.History;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room{
    
    private String name;
    private static int id = 0;
    private int idRoom;
    private History history;
    
    private ArrayList<User> users;
    private User admin;
    
    
    public Room(User admin, String name)
    {
        idRoom = ++id;
        history = new History();
        users = new ArrayList();
        this.name = name;
        this.admin = admin;
        join(admin);
    }
 
    // подключившумуся отправляем текущее состояине холста
    public void join(User user)
    {
        users.add(user);
        try {
            System.out.println("К комнате " + idRoom + "присоединился" + user);
            user.getThisObjectOutputStream().writeObject(history.getHistory());
        } catch (IOException ex) {
            System.out.println("Join(): " + id + ex.getMessage());
        }
    }

    public void quit(User user)
    {
        users.remove(user);

        System.out.println("In room: " + idRoom + "disconnected" + user);
        try {
            user.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void broadcast(String [] changes)
    {
        // TODO add broadcast every sec
        User tmp = null;
        int idx = 0;

           // System.out.println("!!!!!!!!!!!!I do broadcast!!!!!!!!!!");
        ArrayList<User> users_tmp = (ArrayList<User>) users.clone();
        for (User user : users_tmp)
        {
            try {
                tmp = user;

                if(changes == null)
                    System.out.println("ИЗМЕНЕНИЯ НУЛЛ ");
                   // System.out.println("!!!!!!!!!!!!I do broadcast for " + idx);

                //user.getThisObjectOutputStream().writeObject(user.getSocket().toString());
                user.getThisObjectOutputStream().writeObject(changes);
                idx++;
            }

            catch (SocketException e) {
                //users.remove(idx);
                System.out.println("Socket ex:broadcast " + e.getMessage());
            }
            catch (IOException e) {

                //users.remove(idx);
                System.out.println("IO ex: broadcast " + e.getMessage());
            }

        }


    }
    
    public String toString()
    {
        return name;
    }
    
    public int getId()
    {
        return id;
    }

    synchronized void addAction(User user, String[] changes) {
        history.addAction(user, changes);
        broadcast(changes);
        
    }

}
