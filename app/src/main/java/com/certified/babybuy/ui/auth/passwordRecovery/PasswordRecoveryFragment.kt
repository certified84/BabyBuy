package com.certified.babybuy.ui.auth.passwordRecovery

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.certified.babybuy.R
import com.certified.babybuy.databinding.FragmentPasswordRecoveryBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordRecoveryFragment : Fragment() {

    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PasswordRecoveryViewModel by viewModels()

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

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.message.collect {
                        it?.let {
                            showSnackbar(it)
                            viewModel._message.value = null
                        }
                    }
                }
                launch {
                    viewModel.success.collect {
                        if (it) {
                            viewModel._success.value = false
                            findNavController().navigate(PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment())
                        }
                    }
                }
            }
        }

        binding.apply {
            indicator.apply {
                setText("Loading...")
                setTypeface(R.font.space_grotesk_medium)
                setProgressIndicatorColor("#001F27")
                setTrackColor("#AFECFF")
                setImageResource(R.drawable.ic_onboarding_icon)
            }
            btnBack.setOnClickListener {
                findNavController().navigate(
                    PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToLoginFragment()
                )
            }
            btnResetPassword.setOnClickListener {
                val email = etEmail.text.toString()

                if (email.isBlank()) {
                    etEmailLayout.error = "Email is required"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!checkEmail(email))
                    return@setOnClickListener
                etEmailLayout.error = null

                with(viewModel) {
                    uiState.set(UIState.LOADING)
                    sendPasswordResetEmail(email)
                }
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailLayout.error = "Enter a valid email"
            binding.etEmail.requestFocus()
            return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (binding.indicator.isVisible)
            binding.indicator.startAnimation()
    }

    override fun onPause() {
        super.onPause()
        binding.indicator.stopAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}