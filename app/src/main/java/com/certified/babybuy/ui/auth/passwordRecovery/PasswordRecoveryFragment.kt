package com.certified.babybuy.ui.auth.passwordRecovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.certified.babybuy.databinding.FragmentPasswordRecoveryBinding

class PasswordRecoveryFragment : Fragment() {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().navigate(
                    PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}