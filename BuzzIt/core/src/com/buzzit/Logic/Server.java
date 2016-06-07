package com.buzzit.Logic;

import com.badlogic.gdx.Gdx;
import com.buzzit.GUI.screen.Multiplayer1stScreen;
import com.buzzit.GUI.screen.Multiplayer2ndScreen;
import com.buzzit.GUI.screen.MultiplayerSettingsScreen;
import com.buzzit.GUI.screen.SettingsScreen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

//import java.util.Map;
//sorry nao deu, ele nao reconheceu o client

/**
 * Created by Rui Oliveira on 05/06/2016.
 */
public class Server {
    private Socket socket;
    private String id;
    private Client adminClient;

    private HashMap<String, Client> clients;
    private int numPlayers = 0;
    private static boolean isPlaying = false;

    private Match match;
    private enum GameState { RUNNING, WAITING }
    private GameState gameState;

    public Server(){
        this.socket = Multiplayer1stScreen.getSocket();
        Gdx.app.log("Server", "Creating server...");
        clients = new HashMap<>();
        serverSocketEventListener();
    }

    public void serverSocketEventListener(){
        socket.on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                numPlayers++;
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    addClient(new Client(new Player(SettingsScreen.getName()), playerId));
                    Gdx.app.log("SERVER", "New Player Connect: " + playerId);
                } catch (JSONException e) {
                    Gdx.app.log("SERVER", "Error getting new PlayerID");
                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                numPlayers--;
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
                numPlayers++;
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
                        String name = data.optString("name");

                        //Gdx.app.log("SERVER", "Player " + playerID + " asks to be " + isReady);

                        if (clients.size() > 1) {
                            for (HashMap.Entry<String, Client> entry : clients.entrySet()) {
                                //Gdx.app.log("SERVER", "Player " + entry.getKey() + " is ready? " + entry.getValue().isReady());
                                //Gdx.app.log("SERVER", "can it change? " + playerID.equals(entry.getKey()));
                                if (playerID.equals(entry.getKey())) {
                                    entry.getValue().setReady(isReady);
                                    entry.getValue().getPlayer().setName(name);
                                }
                            }
                            tryStart();
                        }
                    } catch (JSONException e) {
                        Gdx.app.log("SERVER", "Error getting 'readyness'");
                    }
                }
            }
        });
    }

    public void tryStart(){
        /*
        for(HashMap.Entry<String, Client> entry : clients.entrySet()){
            Gdx.app.log("SERVER", "Player " + entry.getKey() + " is ready? " + entry.getValue().isReady());
        }
        */
        if(canStart()){
            isPlaying = true;

            for(HashMap.Entry<String, Client> entry : clients.entrySet()){
                entry.getValue().setReady(false);
            }

            //Gdx.app.log("SERVER", "Can Start the Game!! :D");

            adminClient.setIsPlaying(true);
            startGame();
        }
    }

    public boolean canStart(){
        if(numPlayers <= 1) {
            Gdx.app.log("SERVER", "Not enough players!");
            return false;
        }

        for(HashMap.Entry<String, Client> entry : clients.entrySet()){
            if(!entry.getValue().isReady()) {
                //Gdx.app.log("SERVER", "Both **NOT** ready!");
                return false;
            }
        }
        //Gdx.app.log("SERVER", "Both ready!");
        return true;
    }

    private void startGame() {
        JSONObject data = new JSONObject();
        try{
            data.put("playing", "playing");
            socket.emit("gameStart", data);
        } catch(JSONException e){
            Gdx.app.log("SOCKET.IO", "Error sending updated data");
        }

        match = new Match(MultiplayerSettingsScreen.getNumQuestions(), MultiplayerSettingsScreen.getCategories(), MultiplayerSettingsScreen.getDifficulty(), adminClient.getPlayer());
        Multiplayer2ndScreen.changeToGameScreen();

        Gdx.app.log("SERVER", "Question: " + match.getCurrentQuestion().getQuestion());
        sendQuestion(match.getCurrentQuestion());
        /*
        int i = 0;
        gameState = gameState.RUNNING;
        while(match.getQuestionIndex() != match.getTotalQuestions()){
            if(gameState == GameState.RUNNING) {
                i++;
                Gdx.app.log("SERVER", "Question " + i + ": " + match.getCurrentQuestion().getQuestion());
                sendQuestion(match.getCurrentQuestion());
                gameState = gameState.WAITING;
            } else {
                match.nextQuestion();
                gameState = gameState.RUNNING;
            }
        }*/
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

    private void sendQuestion(Question question){
        ArrayList<String> wrong = new ArrayList<>();
        String[] options = question.generateOptions();
        String[] wrongOptions = new String[options.length];
        int j = 0;
        for(int i=0; i < options.length; i++){
            if(!options[i].equals(question.getCorrect())){
                wrongOptions[j] = options[i];
                wrong.add(options[i]);
                j++;
            }
        }

        String correctOption = question.getCorrect();
        String wrongOption1 = wrongOptions[0];
        String wrongOption2 = wrongOptions[1];
        String wrongOption3 = wrongOptions[2];

        JSONObject data = new JSONObject();
        try{
            data.put("questionString", question.getQuestion());
            data.put("correctOption", correctOption);
            data.put("wrongOption1", wrongOption1);
            data.put("wrongOption2", wrongOption2);
            data.put("wrongOption3", wrongOption3);
            data.put("difficulty", question.getDifficulty().toString());
            data.put("category", question.getCategory().toString());
            socket.emit("sendQuestion", data);
            Gdx.app.log("SERVER", "Sent question");
        } catch(JSONException e){
            Gdx.app.log("SOCKET.IO", "Error sending updated data");
        }


    }

    public void setAdminReady(boolean ready){
        this.adminClient.setReady(ready);
    }

    public Client getAdminClient() {
        return adminClient;
    }

    public static boolean isServerPlaying(){
        return isPlaying;
    }
}
