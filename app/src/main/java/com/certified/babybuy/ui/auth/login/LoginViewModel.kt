package com.certified.babybuy.ui.auth.login

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    val _message = MutableLiveData<String?>()
    val message: LiveData<String?> get() = _message

    private var _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> get() = _success

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.signInWithEmailAndPassword(email, password)
                response.await()
                _success.value = response.isSuccessful
                if (response.isSuccessful) {
                    Log.d("TAG", "signInWithEmailAndPassword: Success: Welcome $email")
                    uiState.set(UIState.SUCCESS)
                } else {
                    _message.value = response.exception?.message
                    Log.d(
                        "TAG",
                        "signInWithEmailAndPassword: Failure: Welcome ${response.exception?.message}"
                    )
                    uiState.set(UIState.FAILURE)
                }
            } catch (e: Exception) {
                uiState.set(UIState.FAILURE)
                _success.value = false
                _message.value = "Authentication failed: ${e.localizedMessage}"
                Log.d("TAG", "signInWithEmailAndPassword: Exception: ${e.localizedMessage}")
            }
        }
    }
}