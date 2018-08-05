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

import java.util.ArrayList;
import java.util.List;

import com.github.jacekolszak.aseprite.BlendMode;
import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.Layer;
import com.github.jacekolszak.aseprite.Layers;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.CelChunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.LayerChunk;

final class LayersImpl implements Layers {

    private final Layers layers = new Layers();
    private final CelFactory celFactory;

    LayersImpl(ASE ase, CelFactory celFactory) {
        this.celFactory = celFactory;
        for (int i = 1; i <= ase.header().frames(); i++) {
            ASE.Frame frame = ase.frame(i);
            for (Chunk chunk : frame.chunks()) {
                if (chunk.isLayer()) {
                    layers.merge(chunk.layer());
                } else if (chunk.isCel()) {
                    layers.merge(i, chunk.cel());
                }
            }
        }
    }

    @Override
    public List<Layer> children() {
        return layers.children();
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

    private class Layers {

        private LayerImpl topLayer = new LayerImpl(false, false, false, 0, NORMAL, "main");
        private List<LayerImpl> layersByIndex = new ArrayList<>();
        private BlendModes blendModes = new BlendModes();

        void merge(LayerChunk chunk) {
            LayerImpl layer = new LayerImpl(chunk.visible(), !chunk.editable(), chunk.groupLayer(), chunk.opacity(),
                    blendModes.blendMode(chunk.blendMode()), chunk.name());
            topLayer.addChild(layer, chunk.childLevel());
            layersByIndex.add(layer);
        }

        void merge(int frame, CelChunk chunk) {
            if (chunk.isCompressedImage()) {
                Chunk.CompressedImageChunk compressedImageChunk = chunk.compressedImage();
                Cel cel = celFactory.create(chunk.xPosition(), chunk.yPosition(), compressedImageChunk.width(),
                        compressedImageChunk.height(), compressedImageChunk.decompressedData());
                chunk.layerIndex();
                layersByIndex.get(chunk.layerIndex()).addCel(frame, cel);
                topLayer.addCel(frame, cel);
            }
        }

        List<Layer> children() {
            return topLayer.children();
        }

    }

}
