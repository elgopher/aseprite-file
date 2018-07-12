package com.github.jacekolszak.aseprite;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.File;
import java.net.URL;

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
        URL url = AsepriteFileSpec.class.getResource("/file.aseprite");
        AsepriteFile file = factory.asepriteFile(url);
        file.load();
        Frames frames = file.frames();
        assertThat(frames).isNotNull();
        assertThat(frames.count()).isEqualTo(1);
    }


}
