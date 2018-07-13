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

import com.github.jacekolszak.aseprite.Frame;

final class FrameImpl implements Frame {

    private final ASE.Frame frame;

    FrameImpl(ASE.Frame frame) {
        this.frame = frame;
    }

    @Override
    public Duration duration() {
        return Duration.ofMillis(frame.duration());
    }
}
