package com.noble.activity.justmovie.ui.common

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.local.SharedPrefs.KEY_BROWSER

object Browser {

    private const val REQUEST_CODE = 100

    fun openUrl(context: Context, url: String) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val inApp = preferences.getBoolean(KEY_BROWSER, true)

        if (inApp) {
            openInAppUrl(context, url)
        } else {
            openBrowserUrl(context, url)
        }
    }

    private fun openInAppUrl(context: Context, url: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)

        val pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val shareIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_share)

        val builder = CustomTabsIntent.Builder()
        builder.addDefaultShareMenuItem()
        builder.setShowTitle(true)
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        builder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
        builder.setActionButton(shareIcon, context.getString(R.string.share_link), pendingIntent, true)

        val intent = builder.build()
        intent.launchUrl(context, url.toUri())
    }

    private fun openBrowserUrl(context: Context, url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
    }
}