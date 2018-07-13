package com.github.jacekolszak.aseprite.impl;

import java.util.HashMap;
import java.util.Map;

import com.github.jacekolszak.aseprite.Color;
import com.github.jacekolszak.aseprite.Palette;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk;
import com.github.jacekolszak.aseprite.impl.ASE.Frame.Chunk.PaletteChunk.PaletteEntry;

public final class PaletteImpl implements Palette {

    private final Colors colors;

    public PaletteImpl(ASE ase) {
        ASE.Frame frame = ase.frame(1); // TODO all those palette chunks are in the first frame only?
        this.colors = frame.chunks()
                .stream()
                .filter(Chunk::isPalette)
                .map(Chunk::palette)
                .collect(Colors::new, Colors::merge, Colors::merge);
    }

    @Override
    public int numberOfColors() {
        return colors.numberOfColors();
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
