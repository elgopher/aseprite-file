package com.github.jacekolszak.aseprite.impl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class ASE {

    private static final int FIRST_FRAME_OFFSET = 128;
    private final ByteBuffer buffer;

    public ASE(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    public Header header() {
        return new Header();
    }

    Frame frame(int number) {
        int offset = FIRST_FRAME_OFFSET;
        for (int i = 1; i < number; i++) {
            int frameBytes = (int) new Frame(offset).bytes();
            offset += frameBytes;
        }
        return new Frame(offset);
    }

    /**
     * DWORD: A 32-bit unsigned integer value
     */
    private long dword(int index) {
        return Integer.toUnsignedLong(buffer.getInt(index));
    }

    /**
     * WORD: A 16-bit unsigned integer value
     */
    private int word(int index) {
        return Short.toUnsignedInt(buffer.getShort(index));
    }

    /**
     * BYTE: An 8-bit unsigned integer value
     */
    private int byte_(int index) {
        return Byte.toUnsignedInt(buffer.get(index));
    }

    public class Header {

        public long fileSize() {
            return dword(0);
        }

        int frames() {
            return word(6);
        }

        int width() {
            return word(8);
        }

        int height() {
            return word(10);
        }

        int colorDepth() {
            return word(12);
        }

        long flags() {
            return dword(14);
        }

        int speed() {
            return word(18);
        }

        int transparentColorPaletteEntryIndex() {
            return byte_(28);
        }

        int numberOfColors() {
            return word(32);
        }

        int pixelWidth() {
            return byte_(34);
        }

        int pixelHeight() {
            return byte_(35);
        }

    }

    class Frame {

        private final int offset;

        Frame(int offset) {
            this.offset = offset;
        }

        long bytes() {
            return dword(offset);
        }

        int numberOfChunks() {
            return word(offset + 6);
        }

        int duration() {
            return word(offset + 8);
        }

    }

}
