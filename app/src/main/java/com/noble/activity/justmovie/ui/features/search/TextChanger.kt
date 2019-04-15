package com.noble.activity.justmovie.ui.features.search

import android.text.Editable
import android.text.TextWatcher

interface TextChanger: TextWatcher {

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}