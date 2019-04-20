package com.noble.activity.justmovie.ui.features.detailmovie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class DetailMovieViewModelFactory(private val repository: DetailMovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailMovieViewModel(repository) as T
    }
}