package com.certified.babybuy.ui.item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.certified.babybuy.R
import com.certified.babybuy.databinding.FragmentEditItemBinding
import com.certified.babybuy.util.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemViewModel by viewModels()
    private val args: EditItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        binding.item = args.item

        binding.apply {

            val keyListener = etName.keyListener
            val fadeInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            val fadeOutAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

            btnEdit.setOnClickListener {
                viewModel.uiState.set(UIState.EDITING)
                etName.keyListener = keyListener
                etName.apply {
                    post { etName.setSelection(etName.text.toString().length) }
                    requestFocus()
                }
                btnEdit.startAnimation(fadeOutAnim)
                btnClose.startAnimation(fadeOutAnim)
                btnDone.startAnimation(fadeInAnim)
                tvClickChange.startAnimation(fadeInAnim)
            }
            btnDone.setOnClickListener {
                viewModel.uiState.set(UIState.HAS_DATA)
                etName.keyListener = null
                btnEdit.startAnimation(fadeInAnim)
                btnClose.startAnimation(fadeInAnim)
                btnDone.startAnimation(fadeOutAnim)
                tvClickChange.startAnimation(fadeOutAnim)
            }
            btnClose.setOnClickListener {
                findNavController().navigate(EditItemFragmentDirections.actionEditItemFragmentToHomeFragment())
            }

            if (viewModel.uiState.get() != UIState.EDITING)
                etName.keyListener = null
            else etName.keyListener = keyListener
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}