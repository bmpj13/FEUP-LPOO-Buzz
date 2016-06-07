package com.buzzit.gui.state;

public interface GameStrategy {
    void start();
    void render();
    void finish();
    boolean finished();
}
