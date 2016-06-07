package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.buzzit.Logic.Client;

public class Multiplayer2ndScreen extends SuperScreen {
    ScreenState.ScreenType parentType;

    private Stage stage;
    private Skin skin;
    private BitmapFont font;
    private FreeTypeFontGenerator generator;
    private Pixmap cursorPixmap;
    private Pixmap whitePixmap;
    private Texture cursorTexture;
    private Texture whiteTexture;
    private Texture checkedBoxTexture;
    private Texture uncheckedBoxTexture;
    static private CheckBox readyCheckBox;

    Multiplayer2ndScreen(ScreenState.ScreenType pType) {
        create();
        this.parentType = pType;
    }

    @Override
    protected void create() {
        super.create();

        createSkin();

        /* User name */
        Label ConnectionLabel = new Label("CONNECTED!", skin);

        /* Number of questions per game */
        Label readyLabel = new Label("Are you ready to play?", skin);
        readyCheckBox = new CheckBox("", skin);


        final int smallPad = Gdx.graphics.getHeight()/60;
        final int bigPad = Gdx.graphics.getHeight()/12;

        // Main table
        Table table = new Table();

        table.add(ConnectionLabel).padBottom(smallPad).row();

        table.add(readyLabel).padBottom(smallPad).row();

        table.add(readyCheckBox);

        table.setFillParent(true);

        stage = new Stage();
        stage.addActor(table);
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


        // Checkbox styles
        checkedBoxTexture = new Texture(Gdx.files.internal("settings/checked_checkbox.png"));
        uncheckedBoxTexture = new Texture(Gdx.files.internal("settings/unchecked_checkbox.png"));
        CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.checkboxOn = new SpriteDrawable(new Sprite(checkedBoxTexture));
        checkBoxStyle.checkboxOff = new SpriteDrawable(new Sprite(uncheckedBoxTexture));
        checkBoxStyle.font = skin.getFont("default");
        skin.add("default", checkBoxStyle);


        // Selectbox styles
        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = skin.getFont("default");
        listStyle.fontColorUnselected = Color.CORAL;
        listStyle.fontColorSelected = Color.BLUE;
        listStyle.selection = skin.newDrawable("whiteBackground");

        ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();

        SelectBox.SelectBoxStyle selectBoxStyle = new SelectBox.SelectBoxStyle();
        selectBoxStyle.font = skin.getFont("default");
        selectBoxStyle.fontColor = Color.CORAL;
        selectBoxStyle.listStyle = listStyle;
        selectBoxStyle.scrollStyle = scrollPaneStyle;
        selectBoxStyle.background = skin.newDrawable("whiteBackground");
        skin.add("default", selectBoxStyle);
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

        tryUpdateReady();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
        cursorPixmap.dispose();
        cursorTexture.dispose();
        whitePixmap.dispose();
        whiteTexture.dispose();
        checkedBoxTexture.dispose();
        uncheckedBoxTexture.dispose();
        generator.dispose();
    }

    private void tryUpdateReady() {
        if(isAdmin()){
            //Gdx.app.log("MULTI2SCREEN: ", "is Admin & is " + getAdminClient().isPlaying() + "playing");
            if(!getAdminClient().isPlaying()){
                updateReady();
            }
        } else {
            //Gdx.app.log("MULTI2SCREEN: ", "is NOT Admin & is " + getClient().isPlaying() + "playing");
            if(!getClient().isPlaying()){
                updateReady();
            }
        }
    }

    public static void updateReady() {
        if(isAdmin()){
            if (readyCheckBox.isChecked()) {
                Multiplayer1stScreen.getServer().setAdminReady(true);
            }else {
                Multiplayer1stScreen.getServer().setAdminReady(false);
            }
        } else {
            if (readyCheckBox.isChecked()) {
                Multiplayer1stScreen.getClient().setReady(true);
            }else {
                Multiplayer1stScreen.getClient().setReady(false);
            }
        }

        if(isAdmin()){
            getAdminClient().updateToServer(Multiplayer1stScreen.getSocket(), Gdx.graphics.getDeltaTime());
        } else {
            getClient().updateToServer(Multiplayer1stScreen.getSocket(), Gdx.graphics.getDeltaTime());
        }
    }

    private static boolean isAdmin() {
        return Multiplayer1stScreen.getClient().isAdmin();
    }

    private static Client getAdminClient(){
        return Multiplayer1stScreen.getServer().getAdminClient();
    }

    private static Client getClient(){
        return Multiplayer1stScreen.getClient();
    }

    public static void changeToGameScreen(){
        ScreenState.getInstance().changeState(ScreenState.ScreenType.MULTIPLAYERGAMESCREEN);
    }
}

