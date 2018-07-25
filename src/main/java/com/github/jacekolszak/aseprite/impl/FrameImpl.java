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

import java.time.Duration;

import com.github.jacekolszak.aseprite.Cel;
import com.github.jacekolszak.aseprite.Frame;
import com.github.jacekolszak.aseprite.Layer;

public final class FrameImpl implements Frame {

    private final Duration duration;

    FrameImpl(ASE.Frame frame) {
        this.duration = Duration.ofMillis(frame.duration());
        // TODO iterate over cel chunks and collect all the information
    }

    @Override
    public Duration duration() {
        return duration;
    }

    @Override
    public Cel cel(Layer layer) {
        return null;
    }

}
