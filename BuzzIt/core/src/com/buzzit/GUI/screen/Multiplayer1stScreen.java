package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.buzzit.Logic.Client;
import com.buzzit.Logic.Player;
import com.buzzit.Logic.Server;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Multiplayer1stScreen extends SuperScreen {
    ScreenState.ScreenType parentType;

    private static Socket socket;
    private static Player player;
    private static Client client;
    private static Server server;
    private String id;

    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private Pixmap cursorPixmap;
    private Pixmap whitePixmap;
    private Texture cursorTexture;
    private Texture whiteTexture;
    private Texture connectTexture;
    static private TextField serverTextField;

    final int WIDTH = 1080;
    final int HEIGHT = 1920;

    Multiplayer1stScreen(ScreenState.ScreenType pType) {
        create();
        this.parentType = pType;
    }

    @Override
    protected void create() {
        Gdx.input.setCatchBackKey(true);

        super.create();

        createSkin();

        /* Server IP */
        Label nameLabel = new Label("Insert Server IP", skin);
        serverTextField = new TextField("10.0.2.2:8080", skin);
        serverTextField.setBlinkTime(1f);
        serverTextField.setAlignment(Align.center);
        serverTextField.setMaxLength(21);

        final int smallPad = Gdx.graphics.getHeight()/60;
        final int bigPad = Gdx.graphics.getHeight()/12;


        connectTexture = new Texture(Gdx.files.internal("menu/play.png"));
        ImageButton btnConnect = new ImageButton(new SpriteDrawable(new Sprite(connectTexture)));
        btnConnect.getImage().setScaling(Scaling.fit);

        // Main table
        Table table = new Table();

        table.add(nameLabel).padBottom(smallPad).row();

        table.add(serverTextField).width(Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/3).padBottom(bigPad).row();

        table.add(btnConnect).width(300).height(300);
        btnConnect.row();

        table.setFillParent(true);

        stage = new Stage(new FillViewport(WIDTH, HEIGHT));
        stage.addActor(table);


        /*** Listeners ***/
        btnConnect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                connectSocket(serverTextField.getText().toString());
                configSocketConnectionEvents();
            }
        });
    }

    // SOCKET CONNECTION //
    public void connectSocket(String ip){
        try{
            socket = IO.socket("http://" + ip);
        }catch(Exception e){
            Gdx.app.log("EXCEPTION", "no find server");
        }
        socket.connect();
    }

    public void configSocketConnectionEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");
            }
        }).on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + id);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("welcomingStep", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                String nPlayers = null;
                try {
                    nPlayers = data.getString("n");
                    int i = Integer.parseInt(nPlayers);
                    Gdx.app.log("Number of Players", nPlayers);
                    if(i == 1){
                        server = new Server();
                        //client = new Client(server, player, id);

                        ScreenState.getInstance().changeState(ScreenState.ScreenType.MULTIPLAYERSETTINGS);
                    } else {
                        Gdx.app.log("Multiplayer 1st Screen", "Creating client!");
                        client = new Client(new Player(SettingsScreen.getName()), id);

                        ScreenState.getInstance().changeState(ScreenState.ScreenType.MULTIPLAYER2);
                    }
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting number of players");
                }
            }
        });
    }


    private void createSkin() {
        skin = new Skin();


        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/good_times.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;

        font = generator.generateFont(parameter);
        skin.add("default", font);

        cursorPixmap = new Pixmap((int) font.getSpaceWidth()/2, (int) font.getCapHeight(), Pixmap.Format.RGBA8888);
        cursorPixmap.setColor(Color.CORAL);
        cursorPixmap.fill();
        cursorTexture = new Texture(cursorPixmap);
        skin.add("cursorTexture", cursorTexture);

        whitePixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        whitePixmap.setColor(Color.WHITE);
        whitePixmap.fill();
        whiteTexture = new Texture(whitePixmap);
        skin.add("whiteBackground", whiteTexture);


        // Textfield Styles
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = skin.getFont("default");
        textFieldStyle.fontColor = Color.BLACK;
        textFieldStyle.cursor = skin.newDrawable("cursorTexture");
        textFieldStyle.background = skin.newDrawable("whiteBackground");
        skin.add("default", textFieldStyle);

        // Label styles
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.CORAL;
        skin.add("default", labelStyle);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("default");
        labelStyle.fontColor = Color.WHITE;
        skin.add("categoryName", labelStyle);
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            if (parentType != null) {
                ScreenState.getInstance().changeState(parentType);
            }
        }

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        super.hide();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        super.dispose();
        skin.dispose();
        font.dispose();
        stage.dispose();
        connectTexture.dispose();
        cursorPixmap.dispose();
        cursorTexture.dispose();
        whitePixmap.dispose();
        whiteTexture.dispose();
        generator.dispose();
    }

    public static String getServerIP(){
        return serverTextField.getText().toString();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static Client getClient() {
        return client;
    }

    public static void setClient(Client adminClient) {
        client = adminClient;
    }

    public static Server getServer() {
        return server;
    }
}
