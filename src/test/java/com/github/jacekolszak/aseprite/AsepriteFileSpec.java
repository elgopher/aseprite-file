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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import org.junit.jupiter.api.Test;

final class AsepriteFileSpec {

    private final AsepriteFileFactory factory = new AsepriteFileFactory();

    @Test
    void create_aseprite_file_from_url() {
        URL url = AsepriteFileSpec.class.getResource("/file.aseprite");
        AsepriteFile file = factory.asepriteFile(url);
        assertThat(file.loaded()).isFalse();
    }

    @Test
    void create_aseprite_from_file() {
        AsepriteFile file = factory.asepriteFile(new File("src/test/resources/file.aseprite"));
        assertThat(file.loaded()).isFalse();
    }

    @Test
    void create_aseprite_from_bytes() {
        AsepriteFile file = factory.asepriteFile(new byte[]  {});
        assertThat(file.loaded()).isFalse();
    }

    @Test
    void should_load_url() {
        URL url = AsepriteFileSpec.class.getResource("/file.aseprite");
        AsepriteFile file = factory.asepriteFile(url);
        file.load();
        assertThat(file.loaded()).isTrue();
    }

    @Test
    void should_load_file() {
        AsepriteFile file = factory.asepriteFile(new File("src/test/resources/file.aseprite"));
        file.load();
        assertThat(file.loaded()).isTrue();
    }

    @Test
    void should_return_file_size() {
        AsepriteFile file = factory.asepriteFile(new File("src/test/resources/file.aseprite"));
        file.load();
        assertThat(file.fileSize()).isEqualTo(585);
    }

    @Test
    void should_return_sprite() {
        URL url = AsepriteFileSpec.class.getResource("/file.aseprite");
        AsepriteFile file = factory.asepriteFile(url);
        file.load();
        Sprite sprite = file.sprite();
        assertThat(sprite).isNotNull();
        assertThat(sprite.colorMode()).isEqualTo(ColorMode.INDEXED);
        Size size = sprite.size();
        assertThat(size).isNotNull();
        assertThat(size.width()).isEqualTo(11);
        assertThat(size.height()).isEqualTo(14);
    }

    @Test
    void should_return_frames() {
        AsepriteFile file = asepriteFile();
        Frames frames = file.frames();
        assertThat(frames).isNotNull();
        assertThat(frames.count()).isEqualTo(1);
    }

    @Test
    void should_return_first_frame() {
        AsepriteFile file = asepriteFile();
        Frames frames = file.frames();
        Frame frame = frames.frame(1);
        assertThat(frame).isNotNull();
        assertThat(frame.duration()).isEqualTo(Duration.ofMillis(100));
    }

    private AsepriteFile asepriteFile() {
        URL url = AsepriteFileSpec.class.getResource("/file.aseprite");
        AsepriteFile file = factory.asepriteFile(url);
        file.load();
        return file;
    }

    @Test
    void should_return_palette() {
        AsepriteFile file = asepriteFile();
        Palette palette = file.palette();
        assertThat(palette).isNotNull();
        assertThat(palette.numberOfColors()).isEqualTo(32);
        assertThat(palette.transparentColorIndex()).isEqualTo(0);
    }

    @Test
    void should_return_palette_colors() {
        AsepriteFile file = asepriteFile();
        Palette palette = file.palette();

        Color color0 = palette.at(0);
        assertThat(color0).isNotNull();
        assertThat(color0.red()).isEqualTo(0);
        assertThat(color0.green()).isEqualTo(0);
        assertThat(color0.blue()).isEqualTo(0);
        assertThat(color0.alpha()).isEqualTo(255);

        Color color31 = palette.at(31);
        assertThat(color31).isNotNull();
        assertThat(color31.red()).isEqualTo(138);
        assertThat(color31.green()).isEqualTo(111);
        assertThat(color31.blue()).isEqualTo(48);
        assertThat(color31.alpha()).isEqualTo(174);
    }



}
