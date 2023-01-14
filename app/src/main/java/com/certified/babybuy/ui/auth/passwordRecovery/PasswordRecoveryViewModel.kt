package com.certified.babybuy.ui.auth.passwordRecovery

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class PasswordRecoveryViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.sendPasswordResetEmail(email)
                response.await()
                _success.value = response.isSuccessful
                if (response.isSuccessful) {
                    uiState.set(UIState.SUCCESS)
                    _message.value = "An email reset link has been to sent to $email"
                } else {
                    uiState.set(UIState.FAILURE)
                    _message.value = response.exception?.localizedMessage
                }
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _message.value = "An error occurred: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }
}