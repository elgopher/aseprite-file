package com.github.jacekolszak.aseprite;

public interface Layer extends Parent {

    String name();

    boolean visible();

    boolean readonly();

    boolean group();

    int opacity();

    BlendMode mode();

    Cell cell(int frame);

}
