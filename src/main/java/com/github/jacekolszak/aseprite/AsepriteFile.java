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
package com.github.jacekolszak.aseprite;

import java.util.function.Supplier;

import com.github.jacekolszak.aseprite.impl.ASE;
import com.github.jacekolszak.aseprite.impl.FramesImpl;
import com.github.jacekolszak.aseprite.impl.PaletteImpl;
import com.github.jacekolszak.aseprite.impl.SpriteImpl;

final class AsepriteFile {

    private final Supplier<byte[]> loader;
    private boolean loaded;
    private ASE ase;

    AsepriteFile(Supplier<byte[]> loader) {
        this.loader = loader;
    }

    public void load() {
        byte[] bytes = loader.get();
        this.ase = new ASE(bytes);
        this.loaded = true;
    }

    public boolean loaded() {
        return loaded;
    }

    public Sprite sprite() {
        return new SpriteImpl(ase.header());
    }

    public long fileSize() {
        return ase.header().fileSize();
    }

    public Frames frames() {
        return new FramesImpl(ase);
    }

    public Palette palette() {
        return new PaletteImpl(ase);
    }

}
