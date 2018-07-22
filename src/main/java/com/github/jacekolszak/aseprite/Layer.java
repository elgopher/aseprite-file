package com.github.jacekolszak.aseprite;

import java.util.List;

public interface Layer {

    String name();

    boolean visible();

    boolean readonly();

    boolean group();

    List<Layer> children();

    int opacity();

    BlendMode mode();

}
