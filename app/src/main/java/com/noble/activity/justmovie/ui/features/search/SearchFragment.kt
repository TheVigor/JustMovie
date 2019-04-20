package com.noble.activity.justmovie.ui.features.search

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.TextUtils
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.noble.activity.justmovie.R
import com.noble.activity.justmovie.data.model.Movie
import com.noble.activity.justmovie.data.remote.TmdbClient
import com.noble.activity.justmovie.ui.common.GridSpacingItemDecoration
import com.noble.activity.justmovie.ui.errors.EmptyViewMode
import com.noble.activity.justmovie.ui.features.detailmovie.DetailMovieActivity
import com.noble.activity.justmovie.ui.features.movies.MoviesActivity
import com.noble.activity.justmovie.ui.features.movies.MoviesAdapter
import com.noble.activity.justmovie.ui.features.movies.MoviesFragment.Companion.MOVIE_TYPE
import com.noble.activity.justmovie.ui.features.search.SearchActivity.Companion.SEARCH_QUERY
import com.noble.activity.justmovie.ui.utils.DeviceUtils
import com.noble.activity.justmovie.ui.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*


class SearchFragment : Fragment(), SearchContract.ViewContract, MoviesAdapter.Listener {

    companion object {
        private const val KEY_MENU_ICON = "icon"

        private const val SPEECH_REQUEST_CODE = 101
        private const val MENU_ITEM_INDEX = 0
        private const val ITEM_CLR = 1
        private const val ITEM_MIC = 2

        fun newInstance(query: String): SearchFragment {
            val args  = Bundle()
            args.putString(SEARCH_QUERY, query)

            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var iconActionMode = ITEM_MIC

    private var query = ""
    private var actionMenu: Menu? = null

    private lateinit var adapter: MoviesAdapter
    private lateinit var searchViewModel: SearchViewModel

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (results != null && !results.isEmpty()) {
                    val textResults = results[0]
                    if (!TextUtils.isEmpty(textResults)) {
                        searchEditText.setText(textResults)
                        val text = searchEditText.text ?: ""
                        searchEditText.setSelection(text.length)
                        changeActionIcon()
                        searchViewModel.search(textResults)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
        toolbar.navigationIcon = ViewUtils.getIcon(requireContext(), R.drawable.ic_arrow_back, R.color.iconInactiveColor)
        toolbar.setNavigationOnClickListener { requireActivity().finish() }
        toolbar.inflateMenu(R.menu.menu_search)
        toolbar.setOnMenuItemClickListener {
            if (iconActionMode == ITEM_CLR) {
                if (searchEditText.text != null) {
                    searchEditText.text?.clear()
                }
                changeActionIcon()
                showKeyboard(searchEditText)
            } else {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.speak_now)
                startActivityForResult(intent, SPEECH_REQUEST_CODE)
            }
            return@setOnMenuItemClickListener true
        }
        actionMenu = toolbar.menu

        val spanCount = resources.getInteger(R.integer.movies_span_layout_count)

        adapter = MoviesAdapter(this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
        recyclerView.addItemDecoration(GridSpacingItemDecoration(spanCount, DeviceUtils.dp(requireContext(), 3F)))
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    searchViewModel.nextResults()
                }
            }
        })

        progressBar.visibility = GONE

        emptyView.setMode(EmptyViewMode.MODE_NO_RESULTS)

        searchEditText.addTextChangedListener(object: TextChanger {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changeActionIcon()
                if (s.toString().trim().length >= 2) {
                    searchViewModel.search(s.toString().trim { it <= ' ' })
                }
            }
        })
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchViewModel.search(v.text.toString().trim())
                hideKeyboard(searchEditText)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchEditText.setSelection(searchEditText.text.toString().length)
        ViewUtils.clearCursorDrawable(searchEditText)

        if (savedInstanceState != null) {
            iconActionMode = savedInstanceState.getInt(KEY_MENU_ICON)
        }

        changeActionIcon()

        // Start search with query argument.
        query = arguments?.getString(SEARCH_QUERY) ?: ""
        if (query != "") {
            searchEditText.setText(query)
            searchEditText.setSelection(searchEditText.text.toString().length)
            searchViewModel.search(query)
        }


        searchViewModel = ViewModelProviders.of(this, SearchViewModelFactory(
            SearchRepository.getInstance(TmdbClient.getInstance())))
            .get(SearchViewModel::class.java)

        searchViewModel.loadingState.observe(viewLifecycleOwner,
            Observer { state ->
                state?.let {
                    loading(state)
                }
            })

        searchViewModel.errorState.observe(viewLifecycleOwner,
            Observer { code ->
                code?.let {
                    error(code)
                }
            })

        searchViewModel.contentState.observe(viewLifecycleOwner,
            Observer { content ->
                content?.let {
                    content(content)
                }
            })

        searchViewModel.searchStartState.observe(viewLifecycleOwner,
            Observer { searchStart() })

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(KEY_MENU_ICON, iconActionMode)
        super.onSaveInstanceState(outState)
    }

    override fun searchStart() {
        adapter.movies.clear()
        adapter.notifyDataSetChanged()
        emptyView.visibility = GONE
    }

    override fun loading(state: Boolean) {
        progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun content(results: List<Movie>) {
        adapter.addMovies(results)
        //hideKeyboard(searchEditText)
    }

    override fun error(code: Int) {
        emptyView.visibility = VISIBLE
        emptyView.setMode(code)

//        if (BuildUtil.isApiKeyEmpty()) {
//            emptyView.setValue(R.string.error_empty_api_key)
//        }
    }

    override fun onMovieClick(movie: Movie) {
        val intent = Intent(requireContext(), DetailMovieActivity::class.java)
        intent.putExtra(MOVIE_TYPE, movie)
        startActivity(intent)
    }

    private fun changeActionIcon() {
        if (actionMenu != null) {
            val searchEmpty = searchEditText.text.toString().trim().isEmpty()
            iconActionMode = if (searchEmpty) ITEM_MIC else ITEM_CLR
            actionMenu?.getItem(MENU_ITEM_INDEX)?.setIcon(if (searchEmpty) R.drawable.ic_voice else R.drawable.ic_clear)
        }
    }

    private fun showKeyboard(view: View) {
        val imm = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
