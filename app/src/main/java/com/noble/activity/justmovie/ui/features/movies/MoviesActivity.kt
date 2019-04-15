package com.noble.activity.justmovie.ui.features.movies

import android.content.Intent
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.model.MoviesResponse.Companion.NOW_PLAYING
import com.noble.activity.justmovie.data.model.MoviesResponse.Companion.TOP_RATED
import com.noble.activity.justmovie.data.model.MoviesResponse.Companion.UPCOMING
import com.noble.activity.justmovie.ui.features.about.AboutActivity
import com.noble.activity.justmovie.ui.features.search.SearchActivity
import com.noble.activity.justmovie.ui.utils.DeviceUtils
import kotlinx.android.synthetic.main.activity_main.*

class MoviesActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    companion object {
        private const val DEFAULT_FRAGMENT = R.id.item_playing
        private const val ARG_BOTTOM_BAR_POSITION = "pos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.app_name)

        appBarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent30))

        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent60))
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId =
                DEFAULT_FRAGMENT
        } else {
            bottomNavigationView.selectedItemId = savedInstanceState.getInt(ARG_BOTTOM_BAR_POSITION)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(ARG_BOTTOM_BAR_POSITION, bottomNavigationView.selectedItemId)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_search) {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.item_settings) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_movies, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_playing ->
                supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, MoviesFragment.newInstance(NOW_PLAYING))
                    .commit()
            R.id.item_rated ->
                supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, MoviesFragment.newInstance(TOP_RATED))
                    .commit()
            R.id.item_upcoming ->
                supportFragmentManager
                    .beginTransaction()
                    .replace(container.id, MoviesFragment.newInstance(UPCOMING))
                    .commit()
        }

        return true
    }



}

