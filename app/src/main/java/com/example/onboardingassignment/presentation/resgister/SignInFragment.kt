package com.example.onboardingassignment.presentation.resgister

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.onboardingassignment.R
import com.example.onboardingassignment.data.SignInData
import com.example.onboardingassignment.data.UserModel
import com.example.onboardingassignment.databinding.FragmentSignInBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch

class SignInFragment : Fragment() {

    private val binding: FragmentSignInBinding by lazy{
        FragmentSignInBinding.inflate(layoutInflater)
    }

    private val viewModel: SignViewModel by activityViewModels()
    private lateinit var googleUser: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserveModel()

    }

    private fun initListener() {
        binding.btnSignIn.setOnClickListener {
            val id = binding.etId.text.toString()
            val password = binding.etPassword.text.toString()

            if (id.isBlank())
                Toast.makeText(requireActivity(), getString(R.string.guide_input_id), Toast.LENGTH_SHORT).show()
            else if (password.isBlank())
                Toast.makeText(requireActivity(), getString(R.string.guide_input_pw), Toast.LENGTH_SHORT).show()
            else {
                lifecycleScope.launch {
                    viewModel.selectUser(id)
                }
            }
        }

        binding.btnSignUp.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.signLayout, SignUpFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.ivGoogleLogin.setOnClickListener {
            val credentialManager = CredentialManager.create(requireActivity())
            lifecycleScope.launch {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(getString(R.string.server_client_id))
                    .build()
                val credentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                runCatching {
                    credentialManager.getCredential(
                        request = credentialRequest,
                        context = requireActivity()
                    )
                }.onSuccess {
                    val credential = it.credential
                    when(credential) {
                        is CustomCredential -> {
                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                                try {
                                    val googleIdTokenCredential = GoogleIdTokenCredential
                                        .createFrom(credential.data)
                                    googleUser = UserModel(
                                        id = googleIdTokenCredential.idToken,
                                        name = googleIdTokenCredential.displayName.toString(),
                                        email = googleIdTokenCredential.id,
                                        api = "google"
                                    )

                                    lifecycleScope.launch {
                                        viewModel.selectUserByGoogle(googleIdTokenCredential.idToken)
                                    }


                                } catch (e: GoogleIdTokenParsingException) {
                                    Log.e("SignInFragment", "invalid google id token response")
                                }
                            }
                        }
                    }
                }.onFailure {
                    Log.e("SignInFragment", "fail google sign in")
                }
            }
        }
    }

    private fun initObserveModel(){
        viewModel.signInUser.observe(viewLifecycleOwner) { signedInUser ->

            val password = binding.etPassword.text.toString()
            if(viewModel.signInUser.value?.password == password) {
                SignInData.signInInUser = signedInUser
                Toast.makeText(requireActivity(), getString(R.string.complete_sign_in), Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.signLayout, HomeFragment.newInstance())
                    .commit()

            }
            else
                Toast.makeText(requireActivity(), getString(R.string.guide_input_id_pw), Toast.LENGTH_SHORT).show()

        }

        viewModel.signInUserByGoogle.observe(viewLifecycleOwner) { signedInUser ->
            if(signedInUser.id.isEmpty()) {
                lifecycleScope.launch {
                    viewModel.createUser(googleUser)
                }
            }
            SignInData.signInInUser = googleUser

            Toast.makeText(requireActivity(), getString(R.string.complete_sign_in), Toast.LENGTH_SHORT).show()
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.signLayout, HomeFragment.newInstance())
                .commit()
        }
    }

}