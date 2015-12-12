package com.ruffneck.cloudnote.utils;

import android.graphics.Color;

public class ColorsUtils {

    public static int getReverseColor(int color) {

        int red = (color >> 16) & 0xff;
        int green = (color >> 8) & 0xff;
        int blue = color & 0xff;

        return Color.rgb(255 - red, 255 - green, 255 - blue);

    }

}
