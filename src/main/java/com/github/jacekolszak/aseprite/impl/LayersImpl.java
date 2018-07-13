package com.github.jacekolszak.aseprite.impl;

import java.util.List;

import com.github.jacekolszak.aseprite.Layer;
import com.github.jacekolszak.aseprite.Layers;
import com.github.jacekolszak.aseprite.impl.ASE.Frame;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.LayerChunk;

class LayersImpl implements Layers {

    private final Layers layers;

    LayersImpl(ASE ase) {
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

        private LayerImpl topLayer = new LayerImpl(false, false, false, "main");

        void merge(LayerChunk chunk) {
            LayerImpl layer = new LayerImpl(chunk.visible(), !chunk.editable(), chunk.groupLayer(), chunk.name());
            topLayer.addChild(layer, chunk.childLevel());
        }

        void merge(Layers layers) {
            topLayer._children()
                    .forEach(layer -> layers.topLayer.addChild(layer, 0));
        }

        List<Layer> children() {
            return topLayer.children();
        }

    }

}
