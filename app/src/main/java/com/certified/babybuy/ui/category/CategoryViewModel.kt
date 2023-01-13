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

    val _categoryResponse = MutableStateFlow("")
    val categoryResponse = _categoryResponse.asStateFlow()

    val _uploadSuccess = MutableStateFlow(false)
    val uploadSuccess = _uploadSuccess.asStateFlow()

    private val _category = MutableStateFlow<Category?>(null)
    val category = _category.asStateFlow()

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

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
                    _categoryResponse.value = "Category added successfully"
                    uiState.set(UIState.SUCCESS)
                } else {
                    _categoryResponse.value = "Failed to add category"
                    uiState.set(UIState.FAILURE)
                }
            } catch (e: Exception) {
                _categoryResponse.value = "An error occurred: ${e.localizedMessage}"
                _uploadSuccess.value = false
            }
        }
    }

    fun getCategory(id: String) {
        viewModelScope.launch {
            val query =
                Firebase.firestore.collection("_categories").document(id)
            query.addSnapshotListener { value, error ->
                Log.d("TAG", "getCategory: Value: $value")
                Log.d("TAG", "getCategory: Error: $error")
                if (value == null || error != null)
                    _categoryResponse.value = "An error occurred: ${error?.localizedMessage}"
                else
                    _category.value = value.toObject(Category::class.java)
            }
        }
    }

    fun getItems(userId: String, categoryId: String) {
        viewModelScope.launch {
            val query = repository.getItems(userId)
            query.addSnapshotListener { value, error ->
                if (value == null || value.isEmpty || error != null)
                    uiState.set(UIState.EMPTY)
                else {
                    uiState.set(UIState.HAS_DATA)
                    val allItems = value.toObjects(Item::class.java)
                    _items.value = allItems.filter { it.categoryId == categoryId }
                }
            }
        }
    }
}