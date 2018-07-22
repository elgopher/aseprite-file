package com.github.jacekolszak.aseprite.impl;

import static com.github.jacekolszak.aseprite.BlendMode.ADDITION;
import static com.github.jacekolszak.aseprite.BlendMode.COLOR;
import static com.github.jacekolszak.aseprite.BlendMode.COLOR_BURN;
import static com.github.jacekolszak.aseprite.BlendMode.COLOR_DODGE;
import static com.github.jacekolszak.aseprite.BlendMode.DARKEN;
import static com.github.jacekolszak.aseprite.BlendMode.DIFFERENCE;
import static com.github.jacekolszak.aseprite.BlendMode.DIVIDE;
import static com.github.jacekolszak.aseprite.BlendMode.EXCLUSION;
import static com.github.jacekolszak.aseprite.BlendMode.HARD_LIGHT;
import static com.github.jacekolszak.aseprite.BlendMode.HUE;
import static com.github.jacekolszak.aseprite.BlendMode.LIGHTEN;
import static com.github.jacekolszak.aseprite.BlendMode.LUMINOSITY;
import static com.github.jacekolszak.aseprite.BlendMode.MULTIPLY;
import static com.github.jacekolszak.aseprite.BlendMode.NORMAL;
import static com.github.jacekolszak.aseprite.BlendMode.OVERLAY;
import static com.github.jacekolszak.aseprite.BlendMode.SATURATION;
import static com.github.jacekolszak.aseprite.BlendMode.SCREEN;
import static com.github.jacekolszak.aseprite.BlendMode.SOFT_LIGHT;
import static com.github.jacekolszak.aseprite.BlendMode.SUBTRACT;

import java.util.List;

import com.github.jacekolszak.aseprite.BlendMode;
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

        private LayerImpl topLayer = new LayerImpl(false, false, false, 0, NORMAL, "main");
        private BlendModes blendModes = new BlendModes();

        void merge(LayerChunk chunk) {
            LayerImpl layer = new LayerImpl(chunk.visible(), !chunk.editable(), chunk.groupLayer(), chunk.opacity(),
                    blendModes.blendMode(chunk.blendMode()), chunk.name());
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

    static class BlendModes {

        private BlendMode[] blendModes = new BlendMode[]{
                NORMAL, MULTIPLY, SCREEN, OVERLAY, DARKEN, LIGHTEN,
                COLOR_DODGE, COLOR_BURN, HARD_LIGHT, SOFT_LIGHT,
                DIFFERENCE, EXCLUSION, HUE, SATURATION, COLOR, LUMINOSITY, ADDITION,
                SUBTRACT, DIVIDE
        };

        BlendMode blendMode(int blendMode) {
            return blendModes[blendMode];
        }
    }

}
