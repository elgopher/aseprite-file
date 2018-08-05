package com.github.jacekolszak.aseprite;

public interface Pixel {

    Color color();

    int x();

    int y();

    // TODO This method is a bit unfortunate and works only for an indexed palette. Maybe it is possible to return some meaningful numbers for RGBA images as well
    int index();

}
