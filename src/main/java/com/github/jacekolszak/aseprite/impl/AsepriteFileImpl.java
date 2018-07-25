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

import java.util.function.Supplier;

import com.github.jacekolszak.aseprite.AsepriteFile;
import com.github.jacekolszak.aseprite.Frames;
import com.github.jacekolszak.aseprite.Layers;
import com.github.jacekolszak.aseprite.Palette;
import com.github.jacekolszak.aseprite.Sprite;

public final class AsepriteFileImpl implements AsepriteFile {

    private final Supplier<byte[]> loader;

    private boolean loaded;
    private Layers layers;
    private Palette palette;
    private Frames frames;
    private Sprite sprite;
    private long fileSize;

    public AsepriteFileImpl(Supplier<byte[]> loader) {
        this.loader = loader;
    }

    @Override
    public void load() {
        ASE ase = new ASE(loader.get());
        layers = new LayersImpl(ase);
        palette = new PaletteImpl(ase);
        frames = new FramesImpl(ase);
        sprite = new SpriteImpl(ase);
        fileSize = ase.header().fileSize();
        loaded = true;
    }

    @Override
    public boolean loaded() {
        return loaded;
    }

    @Override
    public Sprite sprite() {
        return sprite;
    }

    @Override
    public long fileSize() {
        return fileSize;
    }

    @Override
    public Frames frames() {
        return frames;
    }

    @Override
    public Palette palette() {
        return palette;
    }

    @Override
    public Layers layers() {
        return layers;
    }

}
