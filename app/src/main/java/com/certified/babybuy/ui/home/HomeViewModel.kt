package com.certified.babybuy.ui.home

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.FirebaseUser
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
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
    val recentUIState = ObservableField(UIState.EMPTY)

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val _updateSuccess = MutableStateFlow(false)
    val updateSuccess = _updateSuccess.asStateFlow()

    init {
        _user.value = Firebase.auth.currentUser
        _user.value?.uid?.let {
            getItems(it)
            getCategories(it)
        }
    }

    private fun getItems(userId: String) {
        viewModelScope.launch {
            val query = repository.getItems(userId)
            query.addSnapshotListener { value, error ->
                if (value == null || value.isEmpty || error != null) {
                    _items.value = emptyList()
                    recentUIState.set(UIState.EMPTY)
                } else {
                    recentUIState.set(UIState.HAS_DATA)
                    _items.value = value.toObjects(Item::class.java).take(10)
                }
            }
        }
    }

    private fun getCategories(userId: String) {
        viewModelScope.launch {
            val query = repository.getCategories(userId)
            query.addSnapshotListener { value, error ->
                Log.d("TAG", "getCategories: Error: $error")
                Log.d("TAG", "getCategories: Value: $value")
                if (value == null || value.isEmpty || error != null) {
                    _categories.value = emptyList()
                    uiState.set(UIState.EMPTY)
                } else {
                    uiState.set(UIState.HAS_DATA)
                    _categories.value = value.toObjects(Category::class.java)
                }
            }
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            try {
                val categoryId = _items.value.find { it.id == id }?.categoryId
                val response = repository.deleteItem(id)
                response.await()
                _categories.value.find { it.id == categoryId }?.let {
                    val map = mapOf("itemCount" to it.itemCount - 1)
                    updateCategory(it.id, map)
                }
                _updateSuccess.value = response.isSuccessful
                _message.value = if (response.isSuccessful) "Item deleted successfully"
                else "Error deleting item"
            } catch (e: Exception) {
                _updateSuccess.value = false
                _message.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    fun updateItem(id: String, purchased: Boolean) {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val itemsRef = db.collection("_items").document(id)
                val response = itemsRef.update("purchased", purchased)
                response.await()
                val categoryId = _items.value.find { it.id == id }?.categoryId
                _categories.value.find { it.id == categoryId }?.let {
                    val map = mapOf("purchasedCount" to it.purchasedCount + 1)
                    updateCategory(it.id, map)
                }
                _updateSuccess.value = response.isSuccessful
                _message.value = if (response.isSuccessful) "Item marked as purchased"
                else "Failed to mark item as purchased"
            } catch (e: Exception) {
                _message.value = "An error occurred: ${e.localizedMessage}"
                _updateSuccess.value = false
            }
        }
    }

    private fun updateCategory(id: String, map: Map<String, Any>) {
        viewModelScope.launch {
            try {
                val categoryRef = Firebase.firestore.collection("_categories").document(id)
                categoryRef.update(map).await()
            } catch (e: Exception) {
                _message.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}