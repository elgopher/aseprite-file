package com.github.jacekolszak.aseprite;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class AsepriteFileFactory {

    public AsepriteFile asepriteFile(URL url) {
        return new AsepriteFile(() -> {
            try {
                InputStream inputStream = url.openStream();
                return inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException("Problem loading Aseprite from url " + url, e);
            }
        });
    }

    public AsepriteFile asepriteFile(File file) {
        return new AsepriteFile(() -> {
            try {
                Path path = file.toPath();
                return Files.readAllBytes(path);
            } catch (IOException e) {
                throw new RuntimeException("Problem loading Aseprite from file " + file, e);
            }
        });

    }

    public AsepriteFile asepriteFile(byte[] bytes) {
        return new AsepriteFile(() -> bytes);
    }

}
