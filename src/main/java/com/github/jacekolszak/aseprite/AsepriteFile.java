package com.github.jacekolszak.aseprite;

public interface AsepriteFile {

    void load();

    boolean loaded();

    Sprite sprite();

    long fileSize();

    Frames frames();

    Palette palette();

    Layers layers();

}
