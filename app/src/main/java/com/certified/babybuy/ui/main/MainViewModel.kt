package com.certified.babybuy.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.util.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class MainViewModel : ViewModel() {

    open val _uiState = MutableStateFlow(UIState.EMPTY)
    open val uiState = _uiState.asStateFlow()

//    init {
//        viewModelScope.launch {
//            repeat(10) {
//                delay(2000L)
//                _uiState.value = UIState.LOADING
//                delay(10000L)
//                _uiState.value = UIState.SUCCESS
//            }
//        }
//    }
}