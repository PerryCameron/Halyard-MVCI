package org.ecsail.widgetfx;

import javafx.scene.layout.Region;

public class RegionFx {

    public static Region regionOf(double height) {
        Region region = new Region();
        region.setPrefHeight(height);
        return region;
    }
}
