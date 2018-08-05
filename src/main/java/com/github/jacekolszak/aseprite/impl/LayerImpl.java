/*
 * Copyright 2018 Aseprite File Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.jacekolszak.aseprite.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.jacekolszak.aseprite.BlendMode;
import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.Layer;

final class LayerImpl implements Layer {

    private final boolean visible;
    private final boolean readonly;
    private final boolean group;
    private final String name;
    private final List<LayerImpl> children;
    private final Map<Integer, Cel> cels = new HashMap();
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

    void addCel(int frame, Cel cel) {
        this.cels.put(frame, cel);
    }

    @Override
    public List<Layer> children() {
        return Collections.unmodifiableList(children);
    }

    List<LayerImpl> _children() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public Cel cel(int frame) {
        return cels.get(frame);
    }

}
