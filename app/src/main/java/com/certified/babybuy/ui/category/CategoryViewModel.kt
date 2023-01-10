package com.certified.babybuy.ui.category

import androidx.lifecycle.ViewModel
import com.certified.babybuy.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

}