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
import java.util.List;

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
        AsepriteFile file = factory.asepriteFile(new byte[]{});
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

    private AsepriteFile asepriteFile(String name) {
        URL url = AsepriteFileSpec.class.getResource(name);
        AsepriteFile file = factory.asepriteFile(url);
        file.load();
        return file;
    }

    private AsepriteFile asepriteFile() {
        return asepriteFile("/file.aseprite");
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


    @Test
    void should_return_layers_on_top_level() {
        AsepriteFile file = asepriteFile("/layers.aseprite");
        Layers layers = file.layers();
        assertThat(layers).isNotNull();
        List<Layer> children = layers.children();
        Layer bottomLayer = children.get(0);
        assertThat(bottomLayer.name()).isEqualTo("bottom-layer");
        assertThat(bottomLayer.visible()).isEqualTo(true);
        assertThat(bottomLayer.readonly()).isEqualTo(true);
        Layer topLevelGroup = children.get(1);
        assertThat(topLevelGroup.name()).isEqualTo("top-level-group");
        assertThat(topLevelGroup.visible()).isEqualTo(true);
        assertThat(topLevelGroup.readonly()).isEqualTo(false);
        assertThat(topLevelGroup.group()).isEqualTo(true);
        assertThat(topLevelGroup.children().size()).isEqualTo(1);
        Layer nestedGroup = topLevelGroup.children().get(0);
        assertThat(nestedGroup.name()).isEqualTo("nested-group");
        assertThat(nestedGroup.visible()).isEqualTo(true);
        assertThat(nestedGroup.readonly()).isEqualTo(false);
        assertThat(nestedGroup.group()).isEqualTo(true);
        assertThat(nestedGroup.children().size()).isEqualTo(2);
        Layer layer2inGroup = nestedGroup.children().get(0);
        assertThat(layer2inGroup.name()).isEqualTo("layer-2-in-group");
        assertThat(layer2inGroup.visible()).isEqualTo(true);
        assertThat(layer2inGroup.readonly()).isEqualTo(false);
        assertThat(layer2inGroup.group()).isEqualTo(false);
        Layer layer1inGroup = nestedGroup.children().get(1);
        assertThat(layer1inGroup.name()).isEqualTo("layer-1-in-group");
        assertThat(layer1inGroup.visible()).isEqualTo(true);
        assertThat(layer1inGroup.readonly()).isEqualTo(false);
        assertThat(layer1inGroup.group()).isEqualTo(false);
        Layer hiddenLayer = children.get(2);
        assertThat(hiddenLayer.name()).isEqualTo("hidden-layer");
        assertThat(hiddenLayer.visible()).isEqualTo(false);
        assertThat(hiddenLayer.readonly()).isEqualTo(false);
        Layer topLayer = children.get(3);
        assertThat(topLayer.name()).isEqualTo("top-layer");
        assertThat(topLayer.visible()).isEqualTo(true);
        assertThat(topLayer.readonly()).isEqualTo(false);
    }


}
