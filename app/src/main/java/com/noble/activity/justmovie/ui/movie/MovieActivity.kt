package com.noble.activity.justmovie.ui.movie

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.ui.extensions.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MovieActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener  {

    companion object {
        private const val DEFAULT_FRAGMENT = R.id.item_playing
        private const val ARG_BOTTOM_BAR_POSITION = "pos"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.setTitle(R.string.app_name)

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_playing -> showToast("Playing")
            R.id.item_rated -> showToast("Rated")
            R.id.item_upcoming -> showToast("Upcoming")
        }

        return true
    }



}

