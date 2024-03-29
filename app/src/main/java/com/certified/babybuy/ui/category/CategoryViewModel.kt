package com.certified.babybuy.ui.category

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess = _uploadSuccess.asStateFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category = _category.asStateFlow()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    val _updateSuccess = MutableStateFlow(false)
    val updateSuccess = _updateSuccess.asStateFlow()

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            try {
                val db = Firebase.firestore
                val categoriesRef = if (category.id.isBlank()) db.collection("_categories")
                    .document() else db.collection("_categories").document(category.id)
                Log.d("TAG", "updateCategory: Category: ${category.copy(id = categoriesRef.id)}")
                val response = categoriesRef.set(category.copy(id = categoriesRef.id))
                response.await()
                _uploadSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _message.value = "Category added successfully"
                    uiState.set(UIState.SUCCESS)
                } else {
                    _message.value = "Failed to add category"
                    uiState.set(UIState.FAILURE)
                }
            } catch (e: Exception) {
                _message.value = "An error occurred: ${e.localizedMessage}"
                _uploadSuccess.value = false
            }
        }
    }

    fun getCategory(id: String) {
        viewModelScope.launch {
            try {
                val query =
                    Firebase.firestore.collection("_categories").document(id)
                query.addSnapshotListener { value, error ->
                    Log.d("TAG", "getCategory: Value: $value")
                    Log.d("TAG", "getCategory: Error: $error")
                    if (value == null || error != null)
                        _message.value = "An error occurred: ${error?.localizedMessage}"
                    else
                        _category.value = value.toObject(Category::class.java)
                }
            } catch (e: Exception) {
                _message.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    fun getItems(userId: String, categoryId: String) {
        viewModelScope.launch {
            val query = repository.getItems(userId)
            query.addSnapshotListener { value, error ->
                if (value == null || value.isEmpty || error != null) {
                    uiState.set(UIState.EMPTY)
                    _items.value = emptyList()
                } else {
                    uiState.set(UIState.HAS_DATA)
                    _items.value =
                        value.toObjects(Item::class.java).filter { it.categoryId == categoryId }
                }
            }
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            try {
                val response = repository.deleteItem(id)
                response.await()
                _category.value?.let {
                    val map = mapOf("itemCount" to it.itemCount - 1)
                    updateCategory(it.id, map)
                }
                _updateSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _message.value = "Item deleted successfully"
                } else {
                    _message.value = "Error deleting item"
                }
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
                _category.value?.let {
                    val map = mapOf("purchasedCount" to it.purchasedCount + 1)
                    updateCategory(it.id, map)
                }
                _updateSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _message.value = "Item marked as purchased"
                    uiState.set(UIState.SUCCESS)
                } else {
                    _message.value = "Failed to mark item as purchased"
                    uiState.set(UIState.FAILURE)
                }
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