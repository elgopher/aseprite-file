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

import java.util.ArrayList;
import java.util.List;

import com.github.jacekolszak.aseprite.Frame;
import com.github.jacekolszak.aseprite.Frames;

final class FramesImpl implements Frames {

    private final int count;
    private final List<Frame> frames = new ArrayList<>();

    FramesImpl(ASE ase) {
        ASE.Header header = ase.header();
        count = header.frames();
        for (int i = 1; i <= header.frames(); i++) {
            ASE.Frame frame = ase.frame(i);
            frames.add(new FrameImpl(frame));
        }
    }

    @Override
    public int count() {
        return count;
    }

    @Override
    public Frame frame(int number) {
        return frames.get(number - 1);
    }
}
