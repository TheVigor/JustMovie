package com.noble.activity.justmovie.ui.utils

import android.content.Context
import android.widget.LinearLayout

object LayoutHelper {

    private fun getSize(context: Context, size: Float): Int =
            if (size < 0) size.toInt() else DeviceUtils.dp(context, size)

    fun makeLinear(context: Context, width: Int, height: Int) =
            LinearLayout.LayoutParams(getSize(context, width.toFloat()), getSize(context, height.toFloat()))
}