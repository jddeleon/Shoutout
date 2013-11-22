package com.shout_out;

import android.app.Application;


public class LobbyID extends Application {
    private static int lobbyID;

    public static int getLobbyID() {
        return lobbyID;
    }
    
    public static void setLobbyID(int num) {
        lobbyID = num;
    }
}