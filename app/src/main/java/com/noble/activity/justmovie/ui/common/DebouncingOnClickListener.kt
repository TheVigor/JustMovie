package com.noble.activity.justmovie.ui.common

import android.view.View

abstract class DebouncingOnClickListener: View.OnClickListener {

    companion object {
        var enabled = true
        val ENABLE_AGAIN = { enabled = true }
    }

    override fun onClick(view: View) {
        if (enabled) {
            enabled = false
            view.post(ENABLE_AGAIN)
            doClick(view)
        }
    }

    abstract fun doClick(v: View)
}