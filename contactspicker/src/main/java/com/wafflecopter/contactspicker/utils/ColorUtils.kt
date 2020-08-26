package com.wafflecopter.contactspicker.utils

import android.graphics.Color
import java.util.*

object ColorUtils {
    //Hey Stranger!
    private val mColors = arrayOf(
        "039BE5", "0F9D58", "4285F4", "FF5722", "DB4437", "689F38", "009688", "DB4437", "3F51B5",
        "9C27B0", "4E342E", "F50057", "42A5F5", "009688", "9E9D24", "00C853", "BF360C", "37474F"
    )
    @JvmStatic
    val randomMaterialColor: Int
        get() = Color.parseColor("#" + mColors[Random().nextInt(18)])
}