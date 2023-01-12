package com.certified.babybuy.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)
    val recentUIState = ObservableField(UIState.EMPTY)

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        val uid = Firebase.auth.currentUser?.uid
        uid?.let {
            getItems(it)
            getCategories(it)
        }
    }

    private fun getItems(userId: String) {
        viewModelScope.launch {
            val query =
                Firebase.firestore.collection("_items")
                    .whereEqualTo("uid", userId)
                    .orderBy("created", Query.Direction.DESCENDING)
            query.addSnapshotListener { value, error ->
                if (value == null || value.isEmpty || error != null)
                    recentUIState.set(UIState.EMPTY)
                else {
                    recentUIState.set(UIState.HAS_DATA)
                    _items.value = value.toObjects(Item::class.java)
                }
            }
        }
    }

    private fun getCategories(userId: String) {
        viewModelScope.launch {
            val query =
                Firebase.firestore.collection("_categories")
                    .whereEqualTo("uid", userId)
                    .orderBy("created", Query.Direction.DESCENDING)
            query.addSnapshotListener { value, error ->
                if (value == null || value.isEmpty || error != null)
                    uiState.set(UIState.EMPTY)
                else {
                    uiState.set(UIState.HAS_DATA)
                    _categories.value = value.toObjects(Category::class.java)
                }
            }
        }
    }
}