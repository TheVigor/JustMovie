package com.noble.activity.justmovie.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.common.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_movie.*

class MoviesFragment: Fragment(), MoviesAdapter.Listener {
    companion object {
        const val MOVIE_TYPE = "movie_type"

        fun newInstance(type: String): MoviesFragment {
            val args = Bundle()
            args.putString(MOVIE_TYPE, type)

            val fragment = MoviesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var type: String
    private lateinit var adapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MoviesAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spanCount = 2

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, resources.getDimension(R.dimen.movies_list_spacing).toInt()))
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && adapter.itemCount != 0) {
                    //presenter.moviesNext(0, list)
                }
            }
        })

        //emptyView.setOnClickListener { presenter.movies(0, list) }

        //type = arguments?.getString(MOVIE_TYPE) ?: NOW_PLAYING
        //presenter.movies(0, list)
    }

    override fun onMovieClick(movie: Movie) {
        val intent = Intent(requireContext(), MoviesActivity::class.java)
        intent.putExtra(MOVIE_TYPE, movie)
        startActivity(intent)
    }

//    override fun loading(state: Boolean) {
//        progressBar.visibility = if (state) VISIBLE else GONE
//    }
//
//    override fun content(results: List<Movie>) {
//        adapter.addMovies(results)
//    }
//
//    override fun error(code: Int) {
//        emptyView.visibility = VISIBLE
//        emptyView.setMode(code)
//
//        if (BuildUtil.isApiKeyEmpty()) {
//            emptyView.setValue(R.string.error_empty_api_key)
//        }
//    }
//

}