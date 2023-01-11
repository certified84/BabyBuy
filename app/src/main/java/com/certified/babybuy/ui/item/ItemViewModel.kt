package com.certified.babybuy.ui.item

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.HAS_DATA)
}