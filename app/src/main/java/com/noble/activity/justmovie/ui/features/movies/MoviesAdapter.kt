package com.noble.activity.justmovie.ui.features.movies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.TmdbConfig.TMDB_IMAGE
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.ui.common.DebouncingOnClickListener
import com.noble.activity.justmovie.ui.utils.DeviceUtils
import java.util.*

import kotlinx.android.synthetic.main.item_poster.*
import kotlinx.android.extensions.LayoutContainer

class MoviesAdapter(
    private val listener: Listener
): RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    interface Listener {
        fun onMovieClick(movie: Movie)
    }

    val movies = ArrayList<Movie>()

    fun addMovies(results: List<Movie>) {
        movies.addAll(results)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poster, parent, false)
        val holder = MovieViewHolder(view)

        val landscape = DeviceUtils.isLandscape(parent.context) || DeviceUtils.isTablet(parent.context)
        view.layoutParams.height = if (landscape) (parent.width / 2.5F).toInt() else (parent.width / 2 * 1.5F).toInt()

        return holder
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    inner class MovieViewHolder(override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(movie: Movie) {
            Glide.with(containerView.context)
                .load(String.format(Locale.US, TMDB_IMAGE, "w342", movie.posterPath))
                .thumbnail(0.1F)
                .into(poster)

            containerView.setOnClickListener(object: DebouncingOnClickListener() {
                override fun doClick(v: View) {
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onMovieClick(movies[adapterPosition])
                    }
                }
            })
        }
    }
}