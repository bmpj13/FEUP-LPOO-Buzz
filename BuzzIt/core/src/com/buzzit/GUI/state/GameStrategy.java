package com.buzzit.GUI.state;

public interface GameStrategy {
    void start();
    void render();
    void finish();
    boolean finished();
}
