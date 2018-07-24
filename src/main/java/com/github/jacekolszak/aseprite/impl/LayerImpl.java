package com.github.jacekolszak.aseprite.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.jacekolszak.aseprite.BlendMode;
import com.github.jacekolszak.aseprite.Cell;
import com.github.jacekolszak.aseprite.Layer;

class LayerImpl implements Layer {

    private final boolean visible;
    private final boolean readonly;
    private final boolean group;
    private final String name;
    private final List<LayerImpl> children;
    private final int opacity;
    private final BlendMode mode;

    LayerImpl(boolean visible, boolean readonly, boolean group, int opacity, BlendMode mode, String name) {
        this.visible = visible;
        this.readonly = readonly;
        this.group = group;
        this.name = name;
        this.children = new ArrayList<>();
        this.opacity = opacity;
        this.mode = mode;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean visible() {
        return visible;
    }

    @Override
    public boolean readonly() {
        return readonly;
    }

    @Override
    public boolean group() {
        return group;
    }

    @Override
    public int opacity() {
        return opacity;
    }

    @Override
    public BlendMode mode() {
        return mode;
    }

    void addChild(LayerImpl layer, int level) {
        if (level == 0) {
            this.children.add(layer);
        } else {
            LayerImpl lastChild = this.children.get(children.size() - 1);
            lastChild.addChild(layer, level - 1);
        }
    }

    @Override
    public List<Layer> children() {
        return Collections.unmodifiableList(children);
    }

    List<LayerImpl> _children() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Cell cell(int frame) {
        return null;
    }
}
