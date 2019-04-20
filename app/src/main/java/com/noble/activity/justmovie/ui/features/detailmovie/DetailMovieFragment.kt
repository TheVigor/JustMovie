package com.noble.activity.justmovie.ui.features.detailmovie

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alexvasilkov.gestures.Settings
import com.alexvasilkov.gestures.transition.GestureTransitions
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator
import com.alexvasilkov.gestures.views.GestureImageView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.TmdbConfig.IMDB_MOVIE
import com.noble.activity.justmovie.data.TmdbConfig.TMDB_IMAGE
import com.noble.activity.justmovie.data.TmdbConfig.TMDB_MOVIE
import com.noble.activity.justmovie.data.model.Country
import com.noble.activity.justmovie.data.model.Genre
import com.noble.activity.justmovie.data.model.Mark
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.data.remote.TmdbClient
import com.noble.activity.justmovie.ui.common.Browser
import com.noble.activity.justmovie.ui.utils.DateUtils
import com.noble.activity.justmovie.ui.utils.SpannableUtils
import com.noble.activity.justmovie.ui.utils.ViewUtils
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.fragment_detail_movie.*
import kotlinx.android.synthetic.main.item_crew.*
import kotlinx.android.synthetic.main.item_genre.*
import java.util.*

class DetailMovieFragment: Fragment(), DetailMovieContract.ViewContract {

    companion object {
        private const val EXTRA_MOVIE = "movie"

        internal fun newInstance(movie: Movie): DetailMovieFragment {
            val args = Bundle()
            args.putSerializable(EXTRA_MOVIE, movie)

            val fragment = DetailMovieFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var sessionId: String = ""

    private var favorite: Boolean = false
    private var watchlist: Boolean = false
    private var actionMenu: Menu? = null
    private var menuShare: MenuItem? = null
    private var menuTmdb: MenuItem? = null
    private var menuImdb: MenuItem? = null
    private var menuHomepage: MenuItem? = null

    private var imdbId: String? = null

    private var homepage: String? = null

    private var posterPath: String = ""

    private lateinit var movie: Movie
    private lateinit var crewAdapter: CrewAdapter
    private lateinit var genresAdapter: GenresAdapter

    var imageAnimator: ViewsTransitionAnimator<*>? = null
    private lateinit var fullBackground: View
    private lateinit var fullToolbar: Toolbar
    private lateinit var fullImage: GestureImageView

    private lateinit var detailMovieViewModel: DetailMovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        actionMenu = menu
        menuShare = menu.add(R.string.share).setIcon(R.drawable.ic_anim_share).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuTmdb = menu.add(R.string.view_on_tmdb).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item === menuShare -> {
                val icon = actionMenu!!.getItem(0).icon
                if (icon is Animatable) {
                    (icon as Animatable).start()
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, String.format(Locale.US, TMDB_MOVIE, movie.id))
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)))
            }
            item === menuTmdb ->
                Browser.openUrl(requireContext(), String.format(Locale.US, TMDB_MOVIE, movie.id))
            item === menuImdb ->
                Browser.openUrl(requireContext(), String.format(Locale.US, IMDB_MOVIE, imdbId))
            item === menuHomepage -> Browser.openUrl(requireContext(), homepage!!)
        }

        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fullBackground = (requireActivity() as DetailMovieActivity).fullBackground
        fullToolbar = (requireActivity() as DetailMovieActivity).fullToolbar
        fullImage = (requireActivity() as DetailMovieActivity).fullImage
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_detail_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailMovieViewModel = ViewModelProviders.of(this, DetailMovieViewModelFactory(
            DetailMovieRepository.getInstance(TmdbClient.getInstance())))
            .get(DetailMovieViewModel::class.java)

        detailMovieViewModel.errorState.observe(viewLifecycleOwner,
            Observer { state ->
                state?.let {
                    setConnectionError()
                }
            })

        detailMovieViewModel.movieState.observe(viewLifecycleOwner,
            Observer {  movie ->
                movie?.let {
                    setURLs(it.imdbId, it.homepage)
                    movie(it)
                }

            })



        runtimeIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_clock, R.color.iconActiveColor))
        runtimeText.setText(R.string.loading)

        langIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_earth, R.color.iconActiveColor))
        langText.setText(R.string.loading)

        taglineText.setText(R.string.loading_tagline)
        //taglineText.customSelectionActionModeCallback = SelectableActionMode(taglineText)

        favoritesBtn.visibility = View.GONE
        favoritesBtn.visibility = View.GONE

        genresAdapter = GenresAdapter()
        genresList.adapter = genresAdapter
        genresList.layoutManager = ChipsLayoutManager.newBuilder(requireContext()).setOrientation(ChipsLayoutManager.HORIZONTAL).build()

        crewAdapter = CrewAdapter()
        crewList.adapter = crewAdapter
        crewList.layoutManager = LinearLayoutManager(requireContext())

        favoritesBtn.setOnClickListener {
            //presenter.markFavorite(sessionId, preferences.getInt(KEY_ACCOUNT_ID, 0), movie.id, !favorite)
        }

        watchlistBtn.setOnClickListener {
            //presenter.addWatchlist(sessionId, preferences.getInt(KEY_ACCOUNT_ID, 0), movie.id, !watchlist)
        }

        poster.setOnClickListener {
            imageAnimator = GestureTransitions.from<Any>(poster).into(fullImage)
            imageAnimator?.addPositionUpdateListener { position, isLeaving ->
                fullBackground.visibility = if (position == 0F) View.GONE else View.VISIBLE
                fullBackground.alpha = position

                fullToolbar.visibility = if (position == 0F) View.GONE else View.VISIBLE
                fullToolbar.alpha = position

                fullImage.visibility = if (position == 0F && isLeaving) View.GONE else View.VISIBLE

                Glide.with(requireContext())
                    .load(String.format(Locale.US, TMDB_IMAGE, "original", posterPath))
                    .thumbnail(0.1F)
                    .into(fullImage)

                if (position == 0F && isLeaving) {
                    (requireActivity() as DetailMovieActivity).showSystemStatusBar(true)
                }
            }
            fullImage.controller.settings
                .setGravity(Gravity.CENTER)
                .setZoomEnabled(true)
                .setAnimationsDuration(300L)
                .setDoubleTapEnabled(true)
                .setRotationEnabled(false)
                .setFitMethod(Settings.Fit.INSIDE)
                .setPanEnabled(true)
                .setRestrictRotation(false)
                .setOverscrollDistance(requireContext(), 32F, 32F)
                .setOverzoomFactor(Settings.OVERZOOM_FACTOR).isFillViewport = true
            imageAnimator?.enterSingle(true)
        }

        trailersText.setOnClickListener {
//            val intent = Intent(requireContext(), ContainerActivity::class.java)
//            intent.putExtra(FRAGMENT_NAME, TRAILERS)
//            intent.putExtra(EXTRA_MOVIE, movie)
//            startActivity(intent)
        }

        reviewsText.setOnClickListener {
//            val intent = Intent(requireContext(), ContainerActivity::class.java)
//            intent.putExtra(FRAGMENT_NAME, REVIEWS)
//            intent.putExtra(EXTRA_MOVIE, movie)
//            startActivity(intent)
        }

        keywordsText.setOnClickListener {
//            val intent = Intent(requireContext(), ContainerActivity::class.java)
//            intent.putExtra(FRAGMENT_NAME, KEYWORDS)
//            intent.putExtra(EXTRA_MOVIE, movie)
//            startActivity(intent)
        }

        similarText.setOnClickListener {
//            val intent = Intent(requireContext(), ContainerActivity::class.java)
//            intent.putExtra(FRAGMENT_NAME, SIMILAR)
//            intent.putExtra(EXTRA_MOVIE, movie)
//            startActivity(intent)
        }

        recommendationsText.setOnClickListener {
//            val intent = Intent(requireContext(), ContainerActivity::class.java)
//            intent.putExtra(FRAGMENT_NAME, RECOMMENDATIONS)
//            intent.putExtra(EXTRA_MOVIE, movie)
//            startActivity(intent)
        }

        movie = arguments?.getSerializable(EXTRA_MOVIE) as Movie
        if (movie != null) {
            movieExtra(movie)
            detailMovieViewModel.getDetails(sessionId, movie.id)
        }
    }

    private fun movieExtra(movie: Movie) {
        posterPath = movie.posterPath
        poster.visibility = View.VISIBLE
        Glide.with(requireContext())
            .load(String.format(Locale.US, TMDB_IMAGE, "w342", posterPath))
            .thumbnail(0.1F)
            .into(poster)

        titleText.text = movie.title
        //titleText.customSelectionActionModeCallback = SelectableActionMode(titleText)

        overviewText.text = if (TextUtils.isEmpty(movie.overview)) getString(R.string.no_overview) else movie.overview
        //overviewText.customSelectionActionModeCallback = SelectableActionMode(overviewText)

        ratingView.setRating(movie.voteAverage)
        ratingText.text = movie.voteAverage.toString()

        voteCountText.text = movie.voteCount.toString()

        if (movie.releaseDate == null) {
            //parent.removeView(dateIcon)
            //parent.removeView(dateText)
            infoLayout.removeView(dateLayout) // old layout
        } else {
            dateIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_calendar, R.color.iconActiveColor))
            dateText.text = DateUtils.formatReleaseDate(movie.releaseDate)
        }

        val list = ArrayList<Genre>()
        for (id in movie.genreIds) {
            list.add(Genre.getGenreById(id))
        }
        genresAdapter.setGenres(list)
    }

    override fun movie(movie: Movie) {
        if (movie.runtime == 0) {
            //parent.removeView(runtimeIcon)
            //parent.removeView(runtimeText)
            infoLayout.removeView(runtimeLayout) // old layout
        } else {
            runtimeText.text = getString(R.string.runtime, DateUtils.formatRuntime(movie.runtime), movie.runtime)
        }

        if (movie.countries.isEmpty()) {
            //parent.removeView(langIcon)
            //parent.removeView(langText)
            infoLayout.removeView(langLayout) // old layout
        } else {
            langText.text = formatCountries(movie.countries)
        }

        if (TextUtils.isEmpty(movie.tagline)) {
            //parent.removeView(taglineText)
            titleLayout.removeView(taglineText) // old layout
        } else {
            taglineText.text = movie.tagline
        }
    }

    private fun formatCountries(countries: List<Country>): String {
        if (countries.isEmpty()) {
            return ""
        }

        val text = StringBuilder()
        for (country in countries) {
            text.append(country.name).append(", ")
        }

        //text.delete(text.toString().length - 2, text.toString().length)
        text.delete(text.indexOf(","), text.length)
        return text.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun setURLs(imdbId: String, homepage: String) {
        this.imdbId = imdbId
        this.homepage = homepage

        if (!TextUtils.isEmpty(imdbId)) {
            menuImdb = actionMenu?.add(R.string.view_on_imdb)?.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
        }

        if (!TextUtils.isEmpty(homepage)) {
            menuHomepage = actionMenu?.add(R.string.view_homepage)?.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER)
        }
    }

    override fun setStates(fave: Boolean, watch: Boolean) {
        favorite = fave
        favoritesBtn.visibility = View.VISIBLE

        if (fave) {
            favoritesIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_heart, R.color.accent_blue))
            favoritesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
        } else {
            favoritesIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_heart_outline, R.color.textColorPrimary))
            favoritesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorPrimary))
        }

        watchlist = watch
        watchlistBtn.visibility = View.VISIBLE

        if (watch) {
            watchlistIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_bookmark, R.color.accent_blue))
            watchlistText.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
        } else {
            watchlistIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_bookmark_outline, R.color.textColorPrimary))
            watchlistText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorPrimary))
        }
    }

    override fun onFavoriteChanged(mark: Mark) {
        when (mark.statusCode) {
            Mark.ADDED -> {
                favoritesIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_heart, R.color.accent_blue))
                favoritesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
                favorite = true
            }
            Mark.DELETED -> {
                favoritesIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_heart_outline, R.color.textColorPrimary))
                favoritesText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorPrimary))
                favorite = false
            }
        }
    }

    override fun onWatchListChanged(mark: Mark) {
        when (mark.statusCode) {
            Mark.ADDED -> {
                watchlistIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_bookmark, R.color.accent_blue))
                watchlistText.setTextColor(ContextCompat.getColor(requireContext(), R.color.accent_blue))
                watchlist = true
            }
            Mark.DELETED -> {
                watchlistIcon.setImageDrawable(ViewUtils.getIcon(requireContext(), R.drawable.ic_bookmark_outline, R.color.textColorPrimary))
                watchlistText.setTextColor(ContextCompat.getColor(requireContext(), R.color.textColorPrimary))
                watchlist = false
            }
        }
    }

    override fun setCredits(casts: String, directors: String, writers: String, producers: String) {
        val crewList = ArrayList<Credits>()
        crewList.add(Credits(getString(R.string.starring), getString(R.string.starring, casts)))
        crewList.add(Credits(getString(R.string.directed), getString(R.string.directed, directors)))
        crewList.add(Credits(getString(R.string.written), getString(R.string.written, writers)))
        crewList.add(Credits(getString(R.string.produced), getString(R.string.produced, producers)))
        crewAdapter.setCrew(crewList)
    }

    override fun setConnectionError() {
        Snackbar.make(parent, R.string.error_no_connection, Snackbar.LENGTH_SHORT).show()
    }

    override fun showComplete(movie: Movie) {}

    data class Credits(val category: String, val list: String)

    private inner class CrewAdapter: RecyclerView.Adapter<CrewAdapter.CrewViewHolder>() {

        private val crew = ArrayList<Credits>()

        fun setCrew(result: List<Credits>) {
            crew.addAll(result)
            notifyItemRangeInserted(crew.size + 1, result.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_crew, parent, false)
            return CrewViewHolder(view)
        }

        override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
            holder.bind(crew[position])
        }

        override fun getItemCount() = crew.size

        inner class CrewViewHolder(override val containerView: View):
            RecyclerView.ViewHolder(containerView), LayoutContainer {

            fun bind(crew: Credits) {
                crewText.text = SpannableUtils.boldAndColoredText(crew.category, crew.list)
            }
        }
    }

    private inner class GenresAdapter: RecyclerView.Adapter<GenresAdapter.GenresViewHolder>() {

        private val genres = ArrayList<Genre>()

        fun setGenres(results: List<Genre>) {
            genres.addAll(results)
            notifyItemRangeInserted(genres.size + 1, results.size)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
            return GenresViewHolder(view)
        }

        override fun onBindViewHolder(holder: GenresViewHolder, position: Int) {
            holder.bind(genres[position])
        }

        override fun getItemCount() = genres.size

        inner class GenresViewHolder(override val containerView: View):
            RecyclerView.ViewHolder(containerView), LayoutContainer {

            fun bind(genre: Genre) {
                chipName.text = genre.name
            }
        }
    }
}