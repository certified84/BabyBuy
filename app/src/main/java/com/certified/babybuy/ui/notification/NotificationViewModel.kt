package com.certified.babybuy.ui.notification

import androidx.lifecycle.ViewModel
import com.certified.babybuy.util.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {

    val _uiState = MutableStateFlow(UIState.EMPTY)
    val uiState = _uiState.asStateFlow()
}