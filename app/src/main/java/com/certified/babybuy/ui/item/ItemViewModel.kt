package com.certified.babybuy.ui.item

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EDITING)

    val _itemResponse = MutableStateFlow("")
    val itemResponse = _itemResponse.asStateFlow()

    val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess = _uploadSuccess.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        val uid = Firebase.auth.currentUser?.uid
        uid?.let {
            getCategories(it)
        }
    }

    private fun getCategories(userId: String) {
        viewModelScope.launch {
            val query = repository.getCategories(userId)
            query.addSnapshotListener { value, error ->
                Log.d("TAG", "getCategories: Error: $error")
                Log.d("TAG", "getCategories: Value: $value")
                if (value == null || value.isEmpty || error != null)
                    uiState.set(UIState.EMPTY)
                else {
                    uiState.set(UIState.HAS_DATA)
                    _categories.value = value.toObjects(Category::class.java)
                }
            }
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val itemsRef = if (item.id.isBlank()) db.collection("_items")
                    .document() else db.collection("_items").document(item.id)
                Log.d("TAG", "updateItem: Item: ${item.copy(id = itemsRef.id)}")
                val response = itemsRef.set(item.copy(id = itemsRef.id))
                response.await()
                _uploadSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _itemResponse.value = "Item added successfully"
                    uiState.set(UIState.SUCCESS)
                } else {
                    _itemResponse.value = "Failed to add item"
                    uiState.set(UIState.FAILURE)
                }
            } catch (e: Exception) {
                _itemResponse.value = "An error occurred: ${e.localizedMessage}"
                _uploadSuccess.value = false
            }
        }
    }
}