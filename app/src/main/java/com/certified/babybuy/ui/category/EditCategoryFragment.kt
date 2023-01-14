package com.certified.babybuy.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.babybuy.databinding.FragmentEditCategoryBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.Extensions.showYesNoDialog
import com.certified.babybuy.util.UIState
import com.certified.babybuy.util.currentDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditCategoryFragment : Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by activityViewModels()
    private val args: EditCategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.message.collect {
                        it?.let { showSnackbar(it) }
                        viewModel._message.value = null
                    }
                }
                launch {
                    viewModel.uploadSuccess.collect { success ->
                        if (success) {
                            viewModel._uploadSuccess.value = false
                            when (args.from) {
                                "category" ->
                                    findNavController().navigate(
                                        EditCategoryFragmentDirections.actionEditCategoryFragmentToCategoryDetailFragment(
                                            args.category.id
                                        )
                                    )
                                else ->
                                    findNavController().navigate(
                                        EditCategoryFragmentDirections.actionEditCategoryFragmentToHomeFragment()
                                    )
                            }
                        }
                    }
                }
            }
        }

        binding.apply {
            category = args.category
            etTitle.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isNotBlank()) etTitleLayout.error = null
            }
            etDescription.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isNotBlank()) etDescriptionLayout.error = null
            }
            btnClose.setOnClickListener {
                if (etTitle.text.toString() != args.category.title || etDescription.text.toString() != args.category.desc) {
                    showYesNoDialog(
                        "Discard Changes",
                        "Are you sure you want to discard changes?"
                    ) {
                        when (args.from) {
                            "category" ->
                                findNavController().navigate(
                                    EditCategoryFragmentDirections.actionEditCategoryFragmentToCategoryDetailFragment(
                                        args.category.id
                                    )
                                )
                            else ->
                                findNavController().navigate(
                                    EditCategoryFragmentDirections.actionEditCategoryFragmentToHomeFragment()
                                )
                        }
                    }
                } else {
                    when (args.from) {
                        "category" ->
                            findNavController().navigate(
                                EditCategoryFragmentDirections.actionEditCategoryFragmentToCategoryDetailFragment(
                                    args.category.id
                                )
                            )
                        else ->
                            findNavController().navigate(
                                EditCategoryFragmentDirections.actionEditCategoryFragmentToHomeFragment()
                            )
                    }
                }
            }
            fabSave.setOnClickListener {
                val title = etTitle.text.toString()
                val desc = etDescription.text.toString()

                if (title.isBlank()) {
                    etTitleLayout.error = "Title is required"
                    etTitle.requestFocus()
                    return@setOnClickListener
                }
                etTitleLayout.error = null

                with(viewModel) {
                    uiState.set(UIState.LOADING)
                    updateCategory(
                        args.category.copy(
                            title = title,
                            desc = desc,
                            modified = currentDate().timeInMillis
                        )
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.etTitle.apply {
            post { setSelection(text.toString().length) }
            requestFocus()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}