package com.wafflecopter.multicontactpicker;

import android.graphics.Color;

import java.util.Random;

public class ColorUtils {

    //Hey Stranger!

    private static String[] mColors = {
            "039BE5","0F9D58","4285F4","FF5722","DB4437","689F38","009688","DB4437","3F51B5",
            "9C27B0","4E342E","F50057","42A5F5","009688","9E9D24","00C853","BF360C","37474F"
    };

    public static int getRandomMaterialColor(){
        return Color.parseColor("#" + mColors[new Random().nextInt(18)]);
    }

}
