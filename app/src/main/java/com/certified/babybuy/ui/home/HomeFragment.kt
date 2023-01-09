package com.certified.babybuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.certified.babybuy.R
import com.certified.babybuy.adapters.CategoryViewPagerAdapter
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Emoji
import com.certified.babybuy.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var categories: List<Category>

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

        setUpSliderItem()
        setUpViewPager()
    }

    private fun setUpSliderItem() {
        categories = arrayListOf(
            Category("Bag", "List of bags for my kids", "#1234AF", 40, 12, Emoji("\uD83C\uDF92")),
            Category("Dress", "List of dress for my kids", "#AF1283", 40, 28, Emoji("\uD83D\uDC57")),
            Category("Shoes", "List of shoes for my kids", "#86AF12", 20, 9, Emoji("\uD83D\uDC5F")),
        )
    }

    private fun setUpViewPager() {
        binding.apply {
            viewPager.adapter = CategoryViewPagerAdapter(categories)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    indicator.selection = position
                    if (position == categories.size - 1) {
                        indicator.count = categories.size
                        indicator.selection = position
                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}