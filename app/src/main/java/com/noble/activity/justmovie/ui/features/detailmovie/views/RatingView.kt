package com.noble.activity.justmovie.ui.features.detailmovie.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.ui.utils.LayoutHelper
import com.noble.activity.justmovie.ui.utils.ViewUtils

class RatingView: LinearLayoutCompat {

    companion object {
        const val ICON_STAR = 0
        const val ICON_STAR_HALF = 1
        const val ICON_STAR_BORDER = 2
    }

    private val stars = arrayOfNulls<StarView>(5)

    constructor(context: Context): super(context, null) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs, 0) {
        initialize(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        initialize(context)
    }

    private fun initialize(context: Context) {
        orientation = LinearLayoutCompat.HORIZONTAL

        val starCount = 5
        for (i in 0 until starCount) {
            val view = StarView(context)
            view.layoutParams = LayoutHelper.makeLinear(context,20, 20)
            addView(view)
            stars[i] = view
        }
    }

    fun setRating(rating: Float) {
        val myRating = rating / 2

        val a = myRating.toInt()
        val b = (10 * (myRating - a)).toInt()
        val c: Int
        c = if (b >= 5) 5 else 0

        if (c == 0) {
            when (a) {
                0 -> for (star in stars) {
                    star?.setIcon(ICON_STAR_BORDER)
                }
                1 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR_BORDER)
                    stars[2]?.setIcon(ICON_STAR_BORDER)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                2 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR_BORDER)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                3 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                4 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR)
                    stars[3]?.setIcon(ICON_STAR)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                5 -> for (star in stars) {
                    star?.setIcon(ICON_STAR)
                }
            }
        } else {
            when (a) {
                0 -> {
                    stars[0]?.setIcon(ICON_STAR_HALF)
                    stars[1]?.setIcon(ICON_STAR_BORDER)
                    stars[2]?.setIcon(ICON_STAR_BORDER)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                1 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR_HALF)
                    stars[2]?.setIcon(ICON_STAR_BORDER)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                2 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR_HALF)
                    stars[3]?.setIcon(ICON_STAR_BORDER)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                3 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR)
                    stars[3]?.setIcon(ICON_STAR_HALF)
                    stars[4]?.setIcon(ICON_STAR_BORDER)
                }
                4 -> {
                    stars[0]?.setIcon(ICON_STAR)
                    stars[1]?.setIcon(ICON_STAR)
                    stars[2]?.setIcon(ICON_STAR)
                    stars[3]?.setIcon(ICON_STAR)
                    stars[4]?.setIcon(ICON_STAR_HALF)
                }
            }
        }
    }

    private inner class StarView(context: Context): AppCompatImageView(context) {

        private var icon: Drawable? = null

        fun setIcon(style: Int) {
            when (style) {
                ICON_STAR -> icon = ViewUtils.getIcon(context, R.drawable.ic_star, R.color.accent_yellow)
                ICON_STAR_HALF -> icon = ViewUtils.getIcon(context, R.drawable.ic_star_half, R.color.accent_yellow)
                ICON_STAR_BORDER -> icon = ViewUtils.getIcon(context, R.drawable.ic_star_border, R.color.accent_yellow)
            }

            setImageDrawable(icon)
        }
    }
}