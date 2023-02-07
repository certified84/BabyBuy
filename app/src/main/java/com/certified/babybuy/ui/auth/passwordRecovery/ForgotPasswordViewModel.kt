package com.certified.babybuy.ui.auth.passwordRecovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val _uiState = MutableStateFlow(UIState.EMPTY)
    val uiState = _uiState.asStateFlow()

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    fun sendPasswordResetEmail(email: String) {
        _uiState.value = UIState.LOADING
        viewModelScope.launch {
            try {
                val response = repository.sendPasswordResetEmail(email)
                response.await()
                _success.value = response.isSuccessful
                if (response.isSuccessful) {
                    _uiState.value = UIState.SUCCESS
                    _message.value = "An email reset link has been to sent to $email"
                } else {
                    _uiState.value = UIState.FAILURE
                    _message.value = response.exception?.localizedMessage
                }
            } catch (e: Exception) {
                _uiState.value = UIState.FAILURE
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }
}