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

import java.util.HashMap;
import java.util.Map;

import com.github.jacekolszak.aseprite.Color;
import com.github.jacekolszak.aseprite.Palette;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.PaletteChunk.PaletteEntry;

final class PaletteImpl implements Palette {

    private final Colors colors;
    private final int transparentColorIndex;

    PaletteImpl(ASE ase) {
        ASE.Frame frame = ase.frame(1);
        colors = frame.chunks()
                .stream()
                .filter(Chunk::isPalette)
                .map(Chunk::palette)
                .collect(Colors::new, Colors::merge, Colors::merge);
        transparentColorIndex = ase.header().transparentColorPaletteEntryIndex();
    }

    @Override
    public int numberOfColors() {
        return colors.numberOfColors();
    }

    @Override
    public int transparentColorIndex() {
        return transparentColorIndex;
    }

    @Override
    public Color at(int index) {
        return colors.at(index);
    }

    private static class Colors {

        private int numberOfColors = 0;
        private Map<Integer, Color> colorsByIndex = new HashMap<>();

        void merge(Chunk.PaletteChunk chunk) {
            numberOfColors = (int) chunk.totalNumberOfEntries();
            int i = (int) chunk.firstColorIndexToChange();
            for (PaletteEntry entry : chunk.entries()) {
                ColorImpl color = new ColorImpl(entry.red(), entry.green(), entry.blue(), entry.alpha());
                colorsByIndex.put(i++, color);
            }
        }

        void merge(Colors colors) {
            numberOfColors = colors.numberOfColors;
            colorsByIndex.putAll(colors.colorsByIndex);
        }

        int numberOfColors() {
            return numberOfColors;
        }

        Color at(int index) {
            return colorsByIndex.get(index);
        }

    }
}
