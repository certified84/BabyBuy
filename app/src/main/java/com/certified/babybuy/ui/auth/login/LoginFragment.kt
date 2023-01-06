package com.certified.babybuy.ui.auth.login

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.certified.babybuy.R
import com.certified.babybuy.databinding.FragmentLoginBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.UIState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

        viewModel.apply {
            message.observe(viewLifecycleOwner) {
                if (it != null) {
                    showSnackbar(it)
                    _message.postValue(null)
                }
            }
            success.observe(viewLifecycleOwner) {
                if (it) {
                    _success.postValue(false)
                    val user = Firebase.auth.currentUser!!
                    if (user.isEmailVerified) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    } else {
                        Firebase.auth.signOut()
                        showSnackbar("Check your email for verification link")
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
                setImageResource(R.drawable.ic_app_icon)
            }
            btnSignup.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
            }
            btnForgotPassword.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPasswordRecoveryFragment())
            }
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isBlank()) {
                    etEmailLayout.error = "Email is required"
                    etEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!checkEmail(email))
                    return@setOnClickListener
                etEmailLayout.error = null

                if (password.isBlank()) {
                    etPasswordLayout.error = "Password is required"
                    etPassword.requestFocus()
                    return@setOnClickListener
                }
                etPasswordLayout.error = null

                with(viewModel) {
                    uiState.set(com.certified.babybuy.util.UIState.LOADING)
                    signInWithEmailAndPassword(email, password)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}