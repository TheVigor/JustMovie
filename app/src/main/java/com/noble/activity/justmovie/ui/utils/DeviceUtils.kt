package com.noble.activity.justmovie.ui.utils

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission
import com.noble.activity.justmovie.R

object DeviceUtils {

    fun isTablet(context: Context) = context.resources.getBoolean(R.bool.tablet)

    fun isLandscape(context: Context) =
        context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    fun statusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) context.resources.getDimensionPixelSize(resId) else 0
    }

    fun dp(context: Context, value: Float) =
        Math.ceil((context.resources.displayMetrics.density * value).toDouble()).toInt()

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(context: Context, milliseconds: Int) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT > 26) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    milliseconds.toLong(), VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            // Deprecated in API 26
            vibrator.vibrate(milliseconds.toLong())
        }
    }
}