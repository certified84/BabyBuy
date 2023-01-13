package com.certified.babybuy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.babybuy.R
import com.certified.babybuy.adapters.CategoryRecyclerAdapter
import com.certified.babybuy.adapters.ItemRecyclerAdapter
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

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

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        binding.recentUIState = viewModel.recentUIState
        binding.viewModel = viewModel

        binding.apply {
            val params = Constraints.LayoutParams(content.width, content.height)
            params.setMargins(60, 10, 0, 10)
            fab.setOnClickListener { showHide() }
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

            recyclerViewItems.adapter = ItemRecyclerAdapter().apply {
                setOnItemClickedListener(object : ItemRecyclerAdapter.OnItemClickedListener {
                    override fun onItemClick(item: Item) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToEditItemFragment(
                                item, "home"
                            )
                        )
                    }
                })
            }
            recyclerViewItems.layoutManager = LinearLayoutManager(context)

            recyclerViewCategories.adapter = CategoryRecyclerAdapter().apply {
                setOnItemClickedListener(object : CategoryRecyclerAdapter.OnItemClickedListener {
                    override fun onItemClick(category: Category) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToCategoryDetailFragment(
                                category.id
                            )
                        )
                    }
                })
            }
            recyclerViewCategories.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            fabAddCategory.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToEditCategoryFragment(
                        Category(), "home"
                    )
                )
            }

            fabAddItem.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToEditItemFragment(
                        Item(), "home"
                    )
                )
            }
        }
    }

    private fun showHide() {
        val visible = binding.fabAddCategory.isVisible
        val fadeInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        val fadeOutAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        binding.apply {
            if (visible) {
                fabAddCategory.apply {
                    visibility = View.GONE
                    startAnimation(fadeOutAnim)
                }
                fabAddItem.apply {
                    visibility = View.GONE
                    startAnimation(fadeOutAnim)
                }
            } else {
                fabAddCategory.apply {
                    visibility = View.VISIBLE
                    startAnimation(fadeInAnim)
                }
                fabAddItem.apply {
                    visibility = View.VISIBLE
                    startAnimation(fadeInAnim)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}