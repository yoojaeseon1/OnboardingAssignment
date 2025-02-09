package com.example.onboardingassignment.presentation.resgister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.onboardingassignment.R
import com.example.onboardingassignment.data.UserModel
import com.example.onboardingassignment.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    private val binding: FragmentSignUpBinding by lazy{
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    private val viewModel: SignViewModel by activityViewModels()

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

    }

    private fun initListener(){
        binding.btnComplete.setOnClickListener {
            val idPattern = Pattern.compile(getString(R.string.pattern_id))
            val passwordPattern = Pattern.compile(getString(R.string.pattern_pw))
            val emailPattern = android.util.Patterns.EMAIL_ADDRESS

            val id = binding.etId.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordCheck = binding.etPasswordCheck.text.toString()
            val nickname = binding.etNickname.text.toString()
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()

            if(id.isBlank() || !idPattern.matcher(id).matches()) {
                Toast.makeText(requireActivity(), getString(R.string.guide_check_id), Toast.LENGTH_SHORT).show()
            } else if(password.isBlank() || passwordCheck.isBlank() || password != passwordCheck || !passwordPattern.matcher(password).matches()) {
                Toast.makeText(requireActivity(), getString(R.string.guide_check_pw), Toast.LENGTH_SHORT).show()
            } else if(nickname.isBlank()) {
                Toast.makeText(requireActivity(), getString(R.string.guide_check_nickname), Toast.LENGTH_SHORT).show()
            } else if(name.isBlank()) {
                Toast.makeText(requireActivity(), getString(R.string.guide_check_name), Toast.LENGTH_SHORT).show()
            } else if(email.isBlank() || !emailPattern.matcher(email).matches()) {
                Toast.makeText(requireActivity(), getString(R.string.guide_check_email), Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    viewModel.createUser(UserModel(id, password, name, nickname, email))
                }
                Toast.makeText(requireActivity(), getString(R.string.complete_sign_up), Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            }


        }

        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }



    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()

    }
}