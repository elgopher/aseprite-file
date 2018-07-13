package com.github.jacekolszak.aseprite.impl;

import java.util.ArrayList;
import java.util.List;

import com.github.jacekolszak.aseprite.Layer;
import com.github.jacekolszak.aseprite.Layers;
import com.github.jacekolszak.aseprite.impl.ASE.Frame;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.LayerChunk;

public class LayersImpl implements Layers {

    private final Layers layers;

    public LayersImpl(ASE ase) {
        Frame frame = ase.frame(1);// TODO Only first frame contains layer chunks?
        layers = frame.chunks()
                .stream()
                .filter(Chunk::isLayer)
                .map(Chunk::layer)
                .collect(Layers::new, Layers::merge, Layers::merge);
    }

    @Override
    public List<Layer> children() {
        return layers.children();
    }

    static class Layers {

        private List<Layer> children = new ArrayList<>();

        void merge(LayerChunk chunk) {
            children.add(new LayerImpl(chunk.visible(), chunk.name()));
        }

        void merge(Layers layers) {
            children.addAll(layers.children);
        }

        List<Layer> children() {
            return children;
        }

    }

}
