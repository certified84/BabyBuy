package com.certified.babybuy.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.babybuy.databinding.FragmentEditCategoryBinding
import com.certified.babybuy.util.Extensions.showYesNoDialog

class EditCategoryFragment : Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by viewModels()
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

//        binding.lifecycleOwner = this
//        binding.uiState = viewModel.uiState
//        binding.viewModel = viewModel
        binding.category = args.category

        binding.apply {
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
                                        args.category
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
                                    args.category
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