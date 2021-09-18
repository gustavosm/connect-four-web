package com.deviget.domain.utils;

import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class ColorConverter {

    public static int[] buildRGBArray(Color color) {
        int[] rgbArray = new int[4];
        rgbArray[0] = color.getRed();
        rgbArray[1] = color.getGreen();
        rgbArray[2] = color.getBlue();
        rgbArray[3] = color.getAlpha();
        return rgbArray;
    }

}
