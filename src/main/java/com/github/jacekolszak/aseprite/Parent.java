package com.github.jacekolszak.aseprite;

import java.util.List;

public interface Parent {

    List<Layer> children();

    default Layer withName(String name) {
        return children().stream()
                .filter(layer -> layer.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find layer with name '" + name + "'"));
    }

}
