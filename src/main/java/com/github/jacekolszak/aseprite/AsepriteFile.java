package com.github.jacekolszak.aseprite;

import java.util.function.Supplier;

import com.github.jacekolszak.aseprite.impl.ASE;
import com.github.jacekolszak.aseprite.impl.FramesImpl;
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
}
