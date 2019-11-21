/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suai.multiuserpaint.history;

import com.suai.multiuserpaint.server.User;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;



//замутить синхонизацию
public class History implements Serializable{

    private LinkedList<String[]> actions = new LinkedList<String[]>();

    public History() {
    
    }
    

    public void addAction(User user, String[] action)
    {
        actions.add(action);
    }
    
    
    public LinkedList<String[]> getHistory()
    {
        return actions;
    }

    //возможно добавть метод который возвратит строчку со всеми действиями
    
            
            
    

}
