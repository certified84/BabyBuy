package com.certified.babybuy.ui.auth.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.User
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val _uiState = MutableStateFlow(UIState.EMPTY)
    val uiState = _uiState.asStateFlow()

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val _success = MutableStateFlow(false)
    val success = _success.asStateFlow()

    val _uploadSuccess = MutableStateFlow<Boolean>(false)
    val uploadSuccess = _uploadSuccess.asStateFlow()

    fun createUserWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.createUserWithEmailAndPassword(email, password)
                response.await()
                _success.value = response.isSuccessful
                Log.d("TAG", "createUserWithEmailAndPassword: ${response.result}")
                if (!response.isSuccessful) {
                    _uiState.value = UIState.FAILURE
                    _message.value = "Registration failed: ${response.exception?.localizedMessage}"
                }
            } catch (e: Exception) {
                _uiState.value = UIState.FAILURE
                _message.value = "Registration failed: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }

    fun signInWithCredential(firebaseCredential: AuthCredential) {
        viewModelScope.launch {
            try {
                val response = repository.signInWithCredential(firebaseCredential)
                response.await()
                _success.value = response.isSuccessful
                Log.d("TAG", "signInWithCredential: ${response.result}")
                if (!response.isSuccessful) {
                    _uiState.value = UIState.FAILURE
                    _message.value = "Registration failed: ${response.exception?.localizedMessage}"
                }
            } catch (e: Exception) {
                _uiState.value = UIState.FAILURE
                _message.value = "Registration failed: ${e.localizedMessage}"
                _success.value = false
            }
        }
    }

    fun uploadDetails(user: User) {
        viewModelScope.launch {
            try {
                val response = repository.uploadDetails(user)
                response.await()
                if (response.isSuccessful) {
                    _uiState.value = UIState.SUCCESS
                    Log.d("TAG", "uploadDetails: Success: ${response.result}")
                } else {
                    _uiState.value = UIState.FAILURE
                    _message.value =
                        "Account creation failed: ${response.exception?.localizedMessage}"
                    Log.d("TAG", "uploadDetails: Failure: ${response.exception?.localizedMessage}")
                }
                _uploadSuccess.value = response.isSuccessful
            } catch (e: Exception) {
                _uiState.value = UIState.FAILURE
                Log.d("TAG", "uploadDetails: Exception: ${e.localizedMessage}")
                _uploadSuccess.value = false
            }
        }
    }
}