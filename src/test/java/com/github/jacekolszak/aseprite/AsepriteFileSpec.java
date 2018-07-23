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

import static com.github.jacekolszak.aseprite.BlendMode.COLOR_BURN;
import static com.github.jacekolszak.aseprite.BlendMode.DIFFERENCE;
import static com.github.jacekolszak.aseprite.BlendMode.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(color0)
                .matches(c -> c.red() == 0 && c.green() == 0 && c.blue() == 0 && c.alpha() == 255);

        Color color31 = palette.at(31);
        assertThat(color31)
                .matches(c -> c.red() == 138 && c.green() == 111 && c.blue() == 48 && c.alpha() == 174);
    }

    @Test
    void should_return_layers_from_bottom_to_top() {
        AsepriteFile file = asepriteFile("/layers.aseprite");
        Layers layers = file.layers();
        List<Layer> children = layers.children();
        assertThat(children)
                .extracting(Layer::name)
                .containsExactly("bottom-layer", "top-level-group", "hidden-layer", "top-layer");
        assertThat(children)
                .extracting(Layer::visible)
                .containsExactly(true, true, false, true);
        assertThat(children)
                .extracting(Layer::group)
                .containsExactly(false, true, false, false);
        assertThat(children)
                .extracting(Layer::readonly)
                .containsExactly(true, false, false, false);
        assertThat(children)
                .extracting(Layer::opacity)
                .containsExactly(255, 0, 255, 167);
        assertThat(children)
                .extracting(Layer::mode)
                .containsExactly(COLOR_BURN, NORMAL, NORMAL, DIFFERENCE);
    }

    @Test
    void should_return_nested_layers() {
        AsepriteFile file = asepriteFile("/layers.aseprite");

        Layer topLevelGroup = file.layers().children().get(1);
        assertThat(topLevelGroup.children().size()).isEqualTo(1);

        Layer nestedGroup = topLevelGroup.children().get(0);
        assertThat(nestedGroup.name()).isEqualTo("nested-group");
        assertThat(nestedGroup.visible()).isEqualTo(true);
        assertThat(nestedGroup.readonly()).isEqualTo(false);
        assertThat(nestedGroup.group()).isEqualTo(true);

        assertThat(nestedGroup.children())
                .extracting(Layer::name)
                .containsExactly("layer-2-in-group", "layer-1-in-group");
        assertThat(nestedGroup.children())
                .extracting(Layer::visible)
                .containsOnly(true);
        assertThat(nestedGroup.children())
                .extracting(Layer::group)
                .containsOnly(false);
        assertThat(nestedGroup.children())
                .extracting(Layer::readonly)
                .containsOnly(false);
        assertThat(nestedGroup.children())
                .extracting(Layer::opacity)
                .containsOnly(255);
        assertThat(nestedGroup.children())
                .extracting(Layer::mode)
                .containsOnly(NORMAL);
    }

    @Test
    void should_return_layer_with_name() {
        AsepriteFile file = asepriteFile("/layers.aseprite");
        Layer bottomLayer = file.layers().withName("bottom-layer");
        assertThat(bottomLayer.name()).isEqualTo("bottom-layer");
    }

    @Test
    void should_return_layer_with_name_from_another_layer() {
        AsepriteFile file = asepriteFile("/layers.aseprite");
        Layer layer = file.layers()
                .withName("top-level-group")
                .withName("nested-group");
        assertThat(layer.name()).isEqualTo("nested-group");
    }

}
