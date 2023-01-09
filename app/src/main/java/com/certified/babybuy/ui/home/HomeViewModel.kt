package com.certified.babybuy.ui.home

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Emoji
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.repository.Repository
import com.certified.babybuy.util.UIState
import com.certified.babybuy.util.currentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val uiState = ObservableField(UIState.EMPTY)

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items = _items.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories = _categories.asStateFlow()

    init {
        getItems()
        getCategories()
    }

    private fun getItems() {
        viewModelScope.launch {
            _items.value = listOf(
                Item(
                    id = "1",
                    title = "Jansport Bag",
                    reminder = currentDate().timeInMillis,
                    category =
                    Category(
                        "1",
                        "Bag",
                        "List of bags for my kids",
                        "#1234AF",
                        40,
                        12,
                        Emoji("\uD83C\uDF92")
                    )
                ),
                Item(
                    id = "2",
                    title = "Air Jordan 4",
                    category =
                    Category(
                        "2",
                        "Shoes",
                        "List of shoes for my kids",
                        "#86AF12",
                        20,
                        9,
                        Emoji("\uD83D\uDC5F")
                    )
                ),
                Item(
                    id = "3",
                    title = "Off-white Hoodie",
                    reminder = currentDate().timeInMillis,
                    category =
                    Category(
                        "3",
                        "Dress",
                        "List of dress for my kids",
                        "#AF1283",
                        40,
                        28,
                        Emoji("\uD83D\uDC57")
                    )
                ),
            )
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categories.value = listOf(
                Category(
                    "1",
                    "Bag",
                    "List of bags for my kids",
                    "#1234AF",
                    40,
                    12,
                    Emoji("\uD83C\uDF92")
                ),
                Category(
                    "3",
                    "Dress",
                    "List of dress for my kids",
                    "#AF1283",
                    40,
                    28,
                    Emoji("\uD83D\uDC57")
                ),
                Category(
                    "2",
                    "Shoes",
                    "List of shoes for my kids",
                    "#86AF12",
                    20,
                    9,
                    Emoji("\uD83D\uDC5F")
                ),
            )
        }
    }
}