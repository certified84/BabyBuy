package com.certified.babybuy.ui.item

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ItemViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val _uiState = MutableStateFlow(UIState.EDITING)
    val uiState = _uiState.asStateFlow()

    val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

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
                    _uiState.value = UIState.EMPTY
                else {
                    _uiState.value = UIState.HAS_DATA
                    _categories.value = value.toObjects(Category::class.java)
                }
            }
        }
    }

    fun updateItem(item: Item, uri: Uri?) {
        viewModelScope.launch {
            try {
                val isNew = item.id.isBlank()
                val db = Firebase.firestore
                val itemsRef = if (isNew) db.collection("_items")
                    .document() else db.collection("_items").document(item.id)

                if (uri != null) {
                    val path = "itemImages/${Firebase.auth.currentUser!!.uid}/${itemsRef.id}.jpg"
                    val imageRef = Firebase.storage.reference.child(path)
                    imageRef.putFile(uri).await()
                    val downloadUrl = imageRef.downloadUrl.await()
                    item.image = downloadUrl.toString()
                }

                val category = _categories.value.find { it.id == item.categoryId }
                if (isNew) {
                    category?.let {
                        it.itemCount += 1
                        if (item.purchased)
                            it.purchasedCount += 1
                    }
                }

                val response = itemsRef.set(item.copy(id = itemsRef.id))
                response.await()
                category?.let {
//                    if (item.purchased)
//                        it.purchasedCount += 1
//                    else it.purchasedCount -= 1
                    val map =
                        mapOf<String, Any>(
                            "itemCount" to it.itemCount,
                            "purchasedCount" to it.purchasedCount
                        )
                    item.categoryId?.let { it1 -> updateCategory(it1, map) }
                }
                _uploadSuccess.value = response.isSuccessful
                if (response.isSuccessful) {
                    _message.value =
                        if (isNew) "Item added successfully" else "Item updated successfully"
                    _uiState.value = UIState.SUCCESS
                } else {
                    _message.value =
                        if (isNew) "Failed to add item" else "Failed to update item"
                    _uiState.value = UIState.FAILURE
                }
            } catch (e: Exception) {
                _message.value = "An error occurred: ${e.localizedMessage}"
                _uploadSuccess.value = false
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