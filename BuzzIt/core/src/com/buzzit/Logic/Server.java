package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.buzzit.GUI.screen.Multiplayer1stScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;

/**
 * Created by Rui Oliveira on 05/06/2016.
 */
public class Server {
    private String id;

    private Match match;
    private HashMap<String, Client> clients;
    private int numPlayers = 0;

    public Server(){
        Gdx.app.log("Server", "Creating server...");
        clients = new HashMap<>();
        serverSocketEventListener();
    }

    public void serverSocketEventListener(){
        Multiplayer1stScreen.getSocket().on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    addClient(new Client(new Player("new player"), playerId));
                    Gdx.app.log("SERVER", "New Player Connect: " + playerId);
                } catch (JSONException e) {
                    Gdx.app.log("SERVER", "Error getting new PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");
                    removeClient(id);
                    Gdx.app.log("SERVER", "Player disconnected: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SERVER", "Error disconnecting Player");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try{
                    for(int i= 0; i<objects.length(); i++){
                        Client newClient = new Client(new Player("newPlayer"),objects.getJSONObject(i).getString("id"));
                        addClient(newClient);
                        Gdx.app.log("SERVER", "Added Player " + objects.getJSONObject(i).getString("id"));
                    }
                }catch(JSONException e){
                    Gdx.app.log("SERVER", "Error adding Player");
                }
            }
        }).on("playerIsReady", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerID = data.getString("id");
                    boolean isReady = data.getBoolean("isReady");

                    Gdx.app.log("SERVER", "Player " + playerID + ": " + isReady );

                    if(clients.size()>1) {
                        for (HashMap.Entry<String, Client> entry : clients.entrySet()) {
                            String asd = String.valueOf(entry.getKey());
                            Gdx.app.log("SERVER", asd);
                            if (playerID == entry.getKey()) {
                                entry.getValue().setReady(isReady);
                            }
                        }
                        tryStart();
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SERVER", "Error sending 'readyness'");
                }
            }
        });
    }

    public void tryStart(){
        if(canStart()){
            for(HashMap.Entry<String, Client> entry : clients.entrySet()){
                entry.getValue().setReady(false);
            }
            Gdx.app.log("SERVER", "Can Start the Game!! :D");
        }
    }

    public boolean canStart(){
        for(HashMap.Entry<String, Client> entry : clients.entrySet()){
            if(!entry.getValue().isReady()) {
                Gdx.app.log("SERVER", "Both **NOT** ready!");
                return false;
            }
        }
        Gdx.app.log("SERVER", "Both ready!");
        return true;
    }

    public void addClient(Client client){
        Gdx.app.log("Server", "Adding Client...");
        clients.put(client.getSocketID(), client);
        for(HashMap.Entry<String, Client> entry : clients.entrySet()){
            Gdx.app.log("Player is on:", entry.getValue().getSocketID());
        }
    }

    public void removeClient(String id){

    }

    public void addPoints(int PlayerID, int points){

    }

    public void pushQuestion(Question question){

    }
}
