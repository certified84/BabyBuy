package com.certified.babybuy.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import com.certified.babybuy.R
import com.certified.babybuy.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val params = Constraints.LayoutParams(content.width, content.height)
            params.setMargins(60, 10, 0, 10)
            btnDrawer.setOnClickListener {
                btnDrawer.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.rotate
                    )
                )
                btnClose.apply {
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.slide_content
                        )
                    )
                    x = 400f
                    y = 100f
                }
                content.apply {
                    btnDrawer.isEnabled = false
                    startAnimation(
                        AnimationUtils.loadAnimation(
                            requireContext(),
                            R.anim.slide_content
                        )
                    )
                    x = 600f
                    y = 100f
                }
            }
            btnClose.setOnClickListener {
                btnDrawer.startAnimation(
                    AnimationUtils.loadAnimation(
                        requireContext(),
                        R.anim.rotate
                    )
                )
                btnClose.apply {
//                    startAnimation(
//                        AnimationUtils.loadAnimation(
//                            requireContext(),
//                            R.anim.slide_content
//                        )
//                    )
                    x = 0f
                    y = 0f
                }
                content.apply {
                    btnDrawer.isEnabled = true
//                    startAnimation(
//                        AnimationUtils.loadAnimation(
//                            requireContext(),
//                            R.anim.slide_content
//                        )
//                    )
                    x = 0f
                    y = 0f
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}