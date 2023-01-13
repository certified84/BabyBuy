package com.certified.babybuy.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.certified.babybuy.adapters.ItemRecyclerAdapter
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.databinding.FragmentCategoryDetailBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by activityViewModels()
    private val args: CategoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCategoryDetailBinding.inflate(layoutInflater, container, false)
        args.id?.let {
            viewModel.getCategory(it)
            viewModel.getItems(Firebase.auth.currentUser!!.uid, it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.apply {
            btnEdit.setOnClickListener {
                this@CategoryDetailFragment.viewModel.category.value?.let { it1 ->
                    findNavController().navigate(
                        CategoryDetailFragmentDirections.actionCategoryDetailFragmentToEditCategoryFragment(
                            it1, "category"
                        )
                    )
                }
            }
            btnBack.setOnClickListener {
                findNavController().navigate(
                    CategoryDetailFragmentDirections.actionCategoryDetailFragmentToHomeFragment()
                )
            }
            fabAddItem.setOnClickListener {
                findNavController().navigate(
                    CategoryDetailFragmentDirections.actionCategoryDetailFragmentToEditItemFragment(
                        Item(), "category"
                    )
                )
            }
            recyclerViewItems.adapter = ItemRecyclerAdapter("category").apply {
                setOnItemClickedListener(object : ItemRecyclerAdapter.OnItemClickedListener {
                    override fun onItemClick(item: Item) {
                        findNavController().navigate(
                            CategoryDetailFragmentDirections.actionCategoryDetailFragmentToEditItemFragment(
                                item, "category"
                            )
                        )
                    }
                })
            }
            recyclerViewItems.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}