package com.buzzit.GUI.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
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
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.buzzit.Logic.Category;
import com.buzzit.Logic.Difficulty;

import java.util.ArrayList;

public class SettingsScreen implements Screen {
    ScreenState.ScreenType parentType;

    final int FINAL_MAX_SIZE = 20;
    final int MAX_DIGIT_QUESTIONS = 2;

    /* Disposables */
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
    private Texture categoriesBackgroundTexture;

    /* Variables acessed by gameplay */
    static private ArrayList<CheckBox> checkBoxes;
    static private TextField nameTextField;
    static private TextField numQuestionsTextField;
    static private SelectBox<Difficulty> difficultySelectBox;

    SettingsScreen(ScreenState.ScreenType pType) {
        create();
        this.parentType = pType;
    }


    protected void create() {
        Gdx.input.setCatchBackKey(true);

        createSkin();

        Preferences prefs = Gdx.app.getPreferences("settings");             // load saved preferences

        /* User's nickname */
        Label nameLabel = new Label("Nickname", skin);
        nameTextField = new TextField(prefs.getString("name", "player"), skin);
        nameTextField.setBlinkTime(1f);
        nameTextField.setAlignment(Align.center);
        nameTextField.setMaxLength(FINAL_MAX_SIZE);

        /* Number of questions per game */
        Label numQuestionsLabel = new Label("Number of Questions", skin);
        numQuestionsTextField = new TextField(prefs.getString("number_questions", "10"), skin);
        numQuestionsTextField.setBlinkTime(1f);
        numQuestionsTextField.setAlignment(Align.center);
        numQuestionsTextField.setMaxLength(MAX_DIGIT_QUESTIONS);
        numQuestionsTextField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        //TODO falta limitar o numero

        final int smallPad = Gdx.graphics.getHeight()/60;
        final int bigPad = Gdx.graphics.getHeight()/12;

        /* Categories wanted */
        checkBoxes = new ArrayList<>();
        Label categoriesLabel = new Label("Categories", skin);


        Table categoriesTable = new Table(skin);
        categoriesBackgroundTexture = new Texture(Gdx.files.internal("settings/categories_background.png"));
        NinePatch patch = new NinePatch(categoriesBackgroundTexture, 3, 3, 3, 3);
        categoriesTable.setBackground(new NinePatchDrawable(patch));


        for (int i = 0; i < Category.values().length; i++) {
            Category category = Category.values()[i];
            CheckBox checkBox = new CheckBox(category.getName(), skin);
            checkBox.getLabelCell().padLeft(smallPad);
            checkBox.setChecked(prefs.getBoolean("checkbox" + i, true));
            checkBoxes.add(checkBox);

            categoriesTable.add(checkBox).pad(smallPad, smallPad, smallPad/2, smallPad).align(Align.left).padBottom(10);
            categoriesTable.row();
        }

        categoriesTable.padBottom(smallPad);

        ScrollPane.ScrollPaneStyle paneStyle = new ScrollPane.ScrollPaneStyle();
        ScrollPane scrollPane = new ScrollPane(categoriesTable, paneStyle);
        scrollPane.layout();

        /* Difficulty */
        Label difficultyLabel = new Label("Difficulty", skin);
        difficultySelectBox = new SelectBox<>(skin);
        difficultySelectBox.setItems(Difficulty.values());
        String selectedName = prefs.getString("difficulty", Difficulty.EASY.toString());
        difficultySelectBox.setSelected(Difficulty.convert(selectedName));

        // Main table
        Table table = new Table();

        table.add(nameLabel).padBottom(smallPad).row();

        table.add(nameTextField).width(Gdx.graphics.getWidth()/2).padBottom(bigPad).row();

        table.add(numQuestionsLabel).padBottom(smallPad).row();
        table.add(numQuestionsTextField).padBottom(bigPad).row();

        table.add(categoriesLabel).padBottom(smallPad).row();
        table.add(scrollPane).height(bigPad*4).padBottom(bigPad).row();

        table.add(difficultyLabel).padBottom(smallPad).row();
        table.add(difficultySelectBox).row();

        table.setFillParent(true);

        stage = new Stage();
        stage.addActor(table);
    }

    private void createSkin() {
        skin = new Skin();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/good_times.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = Gdx.graphics.getWidth()/25;


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
        listStyle.background = skin.newDrawable("whiteBackground");

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
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
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
    }

    @Override
    public void pause() {
        saveSettings();
    }

    @Override
    public void resume() {
    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {
        saveSettings();
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        skin.dispose();
        font.dispose();
        stage.dispose();
        cursorPixmap.dispose();
        cursorTexture.dispose();
        whitePixmap.dispose();
        whiteTexture.dispose();
        checkedBoxTexture.dispose();
        uncheckedBoxTexture.dispose();
        categoriesBackgroundTexture.dispose();
        generator.dispose();
    }

    /**
     * Gets ArrayList containing all categories chosen
     * @return ArrayList of Category enum
     */
    public static ArrayList<Category> getCategories(){
        ArrayList<Category> c = new ArrayList<>();
        for(CheckBox box: checkBoxes){
            if(box.isChecked())
                c.add(Category.getCategory(box.getText().toString()));
        }
        if(c.isEmpty()){
            for(Category cat: Category.values()){
                c.add(cat);
            }
        }
        return c;
    }

    /**
     * Gets defined number of questions
     * @return int from text field
     */
    public static int getNumQuestions(){
        return Integer.parseInt(numQuestionsTextField.getText());
    }

    /**
     * Gets name of the player
     * @return String name
     */
    public static String getName(){
        return nameTextField.getText();
    }

    /**
     * Gets difficulty selected
     * @return Difficulty enum
     */
    public static Difficulty getDifficulty(){
        return difficultySelectBox.getSelected();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * Stores settings
     */
    private void saveSettings() {
        Preferences prefs = Gdx.app.getPreferences("settings");
        prefs.putString("name", nameTextField.getText());
        prefs.putString("number_questions", numQuestionsTextField.getText());
        prefs.putString("difficulty", difficultySelectBox.getSelected().toString());

        for (int i = 0; i < checkBoxes.size(); i++) {
            prefs.putBoolean("checkbox" + i, checkBoxes.get(i).isChecked());
        }

        prefs.flush();
    }
}
