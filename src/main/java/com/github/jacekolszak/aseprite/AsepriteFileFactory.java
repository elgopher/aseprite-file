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
