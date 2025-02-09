package com.example.onboardingassignment.presentation.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.onboardingassignment.R
import com.example.onboardingassignment.data.PostModel
import com.example.onboardingassignment.databinding.FragmentPostCreateBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostCreateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostCreateFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val binding: FragmentPostCreateBinding by lazy {
        FragmentPostCreateBinding.inflate(layoutInflater)
    }

    private val viewModel: PostViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
        binding.btnPostComplete.setOnClickListener {
            
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()
            
            if(title.isEmpty()) {
                Toast.makeText(requireActivity(), "제목을 입력하세요", Toast.LENGTH_SHORT).show()
            } else if (content.isEmpty()){
                Toast.makeText(requireActivity(), "내용을 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    viewModel.createPost(PostModel(title, content))
                }
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

        binding.btnPostCancel.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }





    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostCreateFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostCreateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}