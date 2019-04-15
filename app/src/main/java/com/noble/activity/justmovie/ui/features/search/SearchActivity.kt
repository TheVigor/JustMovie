package com.noble.activity.justmovie.ui.features.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.noble.activity.justmovie.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_QUERY = "query"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val query = intent.getStringExtra(SEARCH_QUERY) ?: ""

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(container.id, SearchFragment.newInstance(query))
                .commit()
        }
    }
}
