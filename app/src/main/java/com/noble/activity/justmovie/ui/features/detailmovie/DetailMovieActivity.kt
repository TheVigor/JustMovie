package com.noble.activity.justmovie.ui.features.detailmovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.TmdbConfig
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.features.detailmovie.appbar.AppBarStateChangeListener
import com.noble.activity.justmovie.ui.features.movies.MoviesFragment.Companion.MOVIE_TYPE
import com.noble.activity.justmovie.ui.utils.DeviceUtils
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.util.*

class DetailMovieActivity : AppCompatActivity() {

    private lateinit var movie: Movie
    private lateinit var fragment: DetailMovieFragment

    private val isSystemStatusBarShown: Boolean
        get() = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        movie = intent.getSerializableExtra(MOVIE_TYPE) as Movie

        fragment = DetailMovieFragment.newInstance(movie)
        supportFragmentManager.beginTransaction().replace(container.id, fragment).commit()

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.transparent40)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {finish()}
        toolbar.title = null

        appBar.addOnOffsetChangedListener(object: AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarState) {
                toolbarTitle.text = if (state === AppBarState.COLLAPSED) movie.title else null
            }
        })


        toolbarTitle.text = movie.title
        if (movie.backdropPath != null) {
            Glide.with(this)
                .load(TmdbConfig.image(movie.backdropPath))
                .thumbnail(0.1F)
                .into(cover)
        }

        collapsingLayout.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary))
        collapsingLayout.setStatusBarScrimColor(ContextCompat.getColor(this, android.R.color.transparent))

        val params = fullToolbar.layoutParams as FrameLayout.LayoutParams
        params.topMargin = DeviceUtils.statusBarHeight(this)

        fullToolbar.setNavigationOnClickListener { onBackPressed() }

        fullImage.setOnClickListener {
            fullToolbar.visibility = if (isSystemStatusBarShown) View.GONE else View.VISIBLE
            showSystemStatusBar(isSystemStatusBarShown.not())
        }

//        cover.setOnLongClickListener {
//            val sessionId = preferences.getString(KEY_SESSION_ID, "") ?: ""
//            if (sessionId.isNotEmpty()) {
//                DeviceUtil.vibrate(this@MovieActivity, 15)
//                val dialog = BackdropDialog.newInstance(String.format(Locale.US, TMDB_IMAGE, "original", movie.backdropPath))
//                dialog.show(supportFragmentManager, "tag")
//                return@setOnLongClickListener true
//            }
//            return@setOnLongClickListener false
//        }

    }

    override fun onBackPressed() {
        if (fragment.imageAnimator != null && !fragment.imageAnimator!!.isLeaving) {
            fragment.imageAnimator?.exit(true)
        } else {
            super.onBackPressed()
        }
    }

    fun showSystemStatusBar(state: Boolean) {
        val flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE
        window.decorView.systemUiVisibility = if (state) 0 else flags
    }

}
