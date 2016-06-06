package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;

/**
 * Created by Rui Oliveira on 05/06/2016.
 */
public class Client {
    private final float UPDATE_TIME = 1/60f;
    float timer;


    private Server server = null;
    private Player player;
    private String socketID;
    private boolean isReady = false;
    private boolean isAdmin = false;

    public Client(Player player, String id){
        Gdx.app.log("Client", "Creating client...");
        this.player = player;
        this.socketID = id;
        Gdx.app.log("Client", "Client ID: "+ socketID);
    }

    public void updateToServer(Socket socket, float dt){
        timer += dt;
        if(timer >= UPDATE_TIME){
            JSONObject data = new JSONObject();
            try{
                //Gdx.app.log("CLIENT", "sending that player " + this.socketID + " is " + this.isReady());
                data.put("isReady", this.isReady);
                socket.emit("playerIsReady", data);
            } catch(JSONException e){
                Gdx.app.log("SOCKET.IO", "Error sending updated data");
            }
        }
    }

    public void pushAnswer(Player player, String answer){

    }

    public String getSocketID() {
        return socketID;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
