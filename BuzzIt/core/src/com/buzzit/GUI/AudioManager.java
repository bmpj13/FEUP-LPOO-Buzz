package com.buzzit.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

/**
 * Created by User on 07/06/2016.
 */
public class AudioManager {
    private static AudioManager ourInstance = new AudioManager();

    private HashMap<String, Sound> sounds;
    private HashMap<String, Music> musics;

    public static AudioManager getInstance() {
        return ourInstance;
    }

    private AudioManager() {
        sounds = new HashMap<>();
        musics = new HashMap<>();
        sounds.put("correct", Gdx.audio.newSound(Gdx.files.internal("sounds/correctAnswer.mp3")));
        sounds.put("wrong", Gdx.audio.newSound(Gdx.files.internal("sounds/wrongAnswer.mp3")));
        sounds.put("clock", Gdx.audio.newSound(Gdx.files.internal("sounds/clock.mp3")));
        musics.put("background", Gdx.audio.newMusic(Gdx.files.internal("sounds/background2.mp3")));

        musics.get("background").setLooping(true);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        for(Sound sound: sounds.values()){
            sound.dispose();
        }
        for(Music music: musics.values()){
            music.dispose();
        }
    }

    public Sound getSound(String name) {
        return sounds.get(name);
    }

    public Music getMusic(String name) {
        return musics.get(name);
    }
}
