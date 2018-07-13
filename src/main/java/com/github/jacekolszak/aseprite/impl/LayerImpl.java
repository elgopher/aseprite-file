package com.github.jacekolszak.aseprite.impl;

import com.github.jacekolszak.aseprite.Layer;

class LayerImpl implements Layer {

    private final boolean visible;
    private final String name;

    LayerImpl(boolean visible, String name) {
        this.visible = visible;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean visible() {
        return visible;
    }
}
