package com.certified.babybuy.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.certified.babybuy.databinding.FragmentNotificationBinding
import com.certified.babybuy.util.UIState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: NotificationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect {
                        binding.uiState = it
                    }
                }
            }
        }

        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(
                    NotificationFragmentDirections.actionNotificationFragmentToHomeFragment()
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}