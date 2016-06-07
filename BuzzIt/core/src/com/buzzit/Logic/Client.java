package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.buzzit.GUI.screen.Multiplayer1stScreen;
import com.buzzit.GUI.screen.Multiplayer2ndScreen;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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

    private static boolean playing = false;

    public Client(Player player, String id){
        Gdx.app.log("Client", "Creating client...");
        this.player = player;
        this.socketID = id;
        this.playing = false;
        Gdx.app.log("Client", "Client ID: "+ socketID);

        clientSocketEventListener();
    }

    private void clientSocketEventListener() {
        Multiplayer1stScreen.getSocket().on("gameStart", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                setIsPlaying(true);
                Gdx.app.log("CLIENT: ", "is Playing = " + isPlaying());
                Multiplayer2ndScreen.changeToGameScreen();
            }
        });
    }

    public void updateToServer(Socket socket, float dt){
        timer += dt;
        if(timer >= UPDATE_TIME){
            JSONObject data = new JSONObject();
            try{
                //Gdx.app.log("CLIENT", "Player name: " + this.player.getName());
                data.put("isReady", this.isReady);
                data.put("playerName", this.player.getName());
                socket.emit("playerIsReady", data);
            } catch(JSONException e){
                Gdx.app.log("SOCKET.IO", "Error sending updated data");
            }
        }
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

    public static boolean isPlaying() {
        return playing;
    }

    public void setIsPlaying(boolean playing) {
        this.playing = playing;
    }

    public Player getPlayer() {
        return player;
    }
}
