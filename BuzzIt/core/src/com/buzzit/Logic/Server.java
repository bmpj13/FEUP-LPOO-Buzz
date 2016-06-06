package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.buzzit.GUI.screen.Multiplayer1stScreen;
import com.buzzit.GUI.screen.SettingsScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.socket.emitter.Emitter;

/**
 * Created by Rui Oliveira on 05/06/2016.
 */
public class Server {
    private String id;

    private Client adminClient;
    private Match match;
    private HashMap<String, Client> clients;
    private int numPlayers = 0;
    private static boolean isPlaying = false;

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
        }).on("playerOne", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    adminClient = new Client(new Player(SettingsScreen.getName()), playerId);
                    adminClient.setAdmin(true);
                    Multiplayer1stScreen.setClient(adminClient);
                    addClient(adminClient);
                    Gdx.app.log("SERVER", "Player 1 Connect: " + playerId);
                } catch (JSONException e) {
                    Gdx.app.log("SERVER", "Error getting PlayerOneID");
                }
            }
        }).on("playerIsReady", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(!isServerPlaying()) {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        String playerID = data.getString("id");
                        boolean isReady = data.getBoolean("isReady");

                        //Gdx.app.log("SERVER", "Player " + playerID + " asks to be " + isReady);

                        if (clients.size() > 1) {
                            for (HashMap.Entry<String, Client> entry : clients.entrySet()) {
                                //Gdx.app.log("SERVER", "Player " + entry.getKey() + " is ready? " + entry.getValue().isReady());
                                //Gdx.app.log("SERVER", "can it change? " + playerID.equals(entry.getKey()));
                                if (playerID.equals(entry.getKey())) {
                                    entry.getValue().setReady(isReady);
                                }
                            }
                            tryStart();
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("SERVER", "Error sending 'readyness'");
                    }
                }
            }
        });
    }

    public void tryStart(){

        for(HashMap.Entry<String, Client> entry : clients.entrySet()){
            Gdx.app.log("SERVER", "Player " + entry.getKey() + " is ready? " + entry.getValue().isReady());
        }

        if(canStart()){
            for(HashMap.Entry<String, Client> entry : clients.entrySet()){
                entry.getValue().setReady(false);
            }
            Gdx.app.log("SERVER", "Can Start the Game!! :D");
            this.isPlaying = true;
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

    public void removeClient(String clientId){
        clients.remove(clientId);
    }

    public void setAdminReady(boolean ready){
        this.adminClient.setReady(ready);
    }

    public Client getAdminClient() {
        return adminClient;
    }

    public void addPoints(int PlayerID, int points){

    }

    public void pushQuestion(Question question){

    }

    public static boolean isServerPlaying(){
        return isPlaying;
    }
}
