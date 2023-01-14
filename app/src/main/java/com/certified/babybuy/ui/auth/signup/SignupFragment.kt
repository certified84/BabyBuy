package com.certified.babybuy.ui.auth.signup

import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.certified.babybuy.BuildConfig
import com.certified.babybuy.R
import com.certified.babybuy.data.model.User
import com.certified.babybuy.databinding.FragmentSignupBinding
import com.certified.babybuy.util.Extensions.showActionDialog
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.UIState
import com.certified.babybuy.util.verifyPassword
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignupViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private var name: String = ""

    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private var showOneTapUI = true

    private val signup =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
                val idToken = credential.googleIdToken
                when {
                    idToken != null -> {
                        showOneTapUI = false
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        name = credential.displayName ?: ""
                        with(viewModel) {
                            uiState.set(UIState.LOADING)
                            signInWithCredential(firebaseCredential)
                        }
                    }
                    else -> {
                        Log.d("TAG", "No ID token!")
                    }
                }
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Log.d("TAG", "One-tap dialog was closed.")
                        showOneTapUI = false
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Log.d("TAG", "One-tap encountered a network error.")
                    }
                    else -> {
                        Log.d(
                            "TAG", "Couldn't get credential from result." +
                                    " (${e.localizedMessage})"
                        )
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState

        oneTapClient = Identity.getSignInClient(requireActivity())
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

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
                            val currentUser = auth.currentUser!!
                            val user = User(
                                uid = currentUser.uid,
                                name = name,
                                email = currentUser.email.toString(),
                                image = currentUser.photoUrl?.toString()
                            )
                            viewModel.uploadDetails(user)
                        }
                    }
                }
                launch {
                    viewModel.uploadSuccess.collect {
                        if (it) {
                            viewModel._uploadSuccess.value = false
                            auth.apply {
                                val profileChangeRequest =
                                    userProfileChangeRequest { displayName = name }
                                currentUser!!.apply {
                                    updateProfile(profileChangeRequest)
                                    sendEmailVerification()
                                }
                            }
                            showActionDialog(
                                "Success",
                                "Account created successfully. We sent a verification link to ${auth.currentUser?.email}. You can ignore the link if you used Google Sign In.",
                            ) {
                                auth.signOut()
                                findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment())
                            }
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
            btnLogin.setOnClickListener { findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToLoginFragment()) }
            btnSignup.setOnClickListener {
                name = etDisplayName.text.toString()
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (name.isBlank()) {
                    etDisplayNameLayout.error = "Name is required"
                    etDisplayName.requestFocus()
                    return@setOnClickListener
                }
                etDisplayNameLayout.error = null

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

                if (!verifyPassword(password, etPassword, etPasswordLayout))
                    return@setOnClickListener
                etPasswordLayout.error = null

                with(viewModel) {
                    uiState.set(UIState.LOADING)
                    createUserWithEmailAndPassword(email, password)
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
        if (showOneTapUI) {
            oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(requireActivity()) { result ->
                    try {
                        signup.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("TAG", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(requireActivity()) { e ->
                    // No Google Accounts found. Just continue presenting the signed-out UI.
                    e.localizedMessage?.let { Log.d("TAG", it) }
                }
        }
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