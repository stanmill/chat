/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.gui.ChatScreen;

/**
 *
 * @author stanleypena
 */
public class Chat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChatScreen screen = new ChatScreen();
        screen.initSocket();
        screen.show();
        
    }
    
}
