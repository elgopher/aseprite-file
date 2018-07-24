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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

final class ASE {

    private static final int FIRST_FRAME_OFFSET = 128;
    private final ByteBuffer buffer;

    ASE(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
    }

    Header header() {
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
     * STRING:
     * WORD: string length (number of bytes)
     * BYTE[length]: characters (in UTF-8) The '\0' character is not included.
     */
    private String string(int index) {
        int length = stringLength(index);
        byte[] dst = new byte[length];
        buffer.position(index + 2); // ???
        buffer.get(dst, 0, length);
        try {
            return new String(dst, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Problem decoding string in ASE file", e);
        }
    }

    private int stringLength(int index) {
        return word(index);
    }

    /**
     * BYTE: An 8-bit unsigned integer value
     */
    private int byte_(int index) {
        return Byte.toUnsignedInt(buffer.get(index));
    }

    class Header {

        long fileSize() {
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

        List<Chunk> chunks() {
            List<Chunk> chunks = new ArrayList<>(numberOfChunks());
            int currentOffset = this.offset + 16;
            for (int i = 0; i < numberOfChunks(); i++) {
                Chunk chunk = new Chunk(currentOffset);
                chunks.add(chunk);
                currentOffset += chunk.size();
            }
            return chunks;
        }

        class Chunk {

            private final int offset;

            Chunk(int offset) {
                this.offset = offset;
            }

            long size() {
                return dword(offset);
            }

            int type() {
                return word(offset + 4);
            }

            boolean isLayer() {
                return type() == 0x2004;
            }

            boolean isCel() {
                return type() == 0x2005;
            }

            boolean isCelExtra() {
                return type() == 0x2006;
            }

            boolean isFrameTags() {
                return type() == 0x2018;
            }

            boolean isPalette() {
                return type() == 0x2019;
            }

            boolean isUserData() {
                return type() == 0x2020;
            }

            boolean isSlice() {
                return type() == 0x2022;
            }

            PaletteChunk palette() {
                return new PaletteChunk(offset + 6);
            }

            LayerChunk layer() {
                return new LayerChunk(offset + 6);
            }

            CelChunk cel() { return new CelChunk(offset + 6); }

            class PaletteChunk {

                private final int offset;

                PaletteChunk(int offset) {
                    this.offset = offset;
                }

                long totalNumberOfEntries() {
                    return dword(offset);
                }

                long firstColorIndexToChange() {
                    return dword(offset + 4);
                }

                long lastColorIndexToChange() {
                    return dword(offset + 8);
                }

                List<PaletteEntry> entries() {
                    List<PaletteEntry> entries = new ArrayList<>();
                    int currentOffset = offset + 20;
                    for (int i = 0; i < totalNumberOfEntries(); i++) {
                        PaletteEntry entry = new PaletteEntry(currentOffset);
                        entries.add(entry);
                        currentOffset += entry.size();
                    }
                    return entries;
                }

                class PaletteEntry {

                    private final int offset;

                    PaletteEntry(int offset) {
                        this.offset = offset;
                    }

                    int flags() {
                        return word(offset);
                    }

                    boolean hasName() {
                        return (flags() & 1) == 1;
                    }

                    int red() {
                        return byte_(offset + 2);
                    }

                    int green() {
                        return byte_(offset + 3);
                    }

                    int blue() {
                        return byte_(offset + 4);
                    }

                    int alpha() {
                        return byte_(offset + 5);
                    }

                    String name() {
                        return string(offset + 6);
                    }

                    int size() {
                        int size = 6;
                        if (hasName()) {
                            return size + 2 + stringLength(offset + 6);
                        } else {
                            return size;
                        }
                    }

                }

            }

            class LayerChunk {
                private final int offset;

                LayerChunk(int offset) {
                    this.offset = offset;
                }

                int flags() {
                    return word(offset);
                }

                boolean visible() {
                    return (flags() & 1) == 1;
                }

                boolean editable() {
                    return (flags() & 2) == 2;
                }

                int type() {
                    return word(offset + 2);
                }

                boolean imageLayer() {
                    return type() == 0;
                }

                boolean groupLayer() {
                    return type() == 1;
                }

                int childLevel() {
                    return word(offset + 4);
                }

                int blendMode() {
                    return word(offset + 10);
                }

                int opacity() {
                    return word(offset + 12);
                }

                String name() {
                    return string(offset + 16);
                }
            }

            class CelChunk {

                private final int offset;

                CelChunk(int offset) {
                    this.offset = offset;
                }

            }
        }
    }

}
