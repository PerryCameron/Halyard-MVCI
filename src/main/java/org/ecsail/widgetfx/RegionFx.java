package org.ecsail.widgetfx;

import javafx.scene.layout.Region;

public class RegionFx {

    public static Region regionHeightOf(double height) {
        Region region = new Region();
        region.setPrefHeight(height);
        return region;
    }

    public static Region regionWidthOf(double width) {
        Region region = new Region();
        region.setPrefHeight(width);
        return region;
    }
}
