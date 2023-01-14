package com.certified.babybuy.ui.category

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.certified.babybuy.R
import com.certified.babybuy.adapters.ItemRecyclerAdapter
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.databinding.FragmentCategoryDetailBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.Extensions.showYesNoDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CategoryViewModel by activityViewModels()
    private val args: CategoryDetailFragmentArgs by navArgs()

    private val itemAdapter by lazy { ItemRecyclerAdapter("category") }

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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.message.collect {
                        it?.let { showSnackbar(it) }
                        viewModel._message.value = null
                    }
                }
                launch {
                    viewModel.deleteSuccess.collect {
                        if (it) {
                            itemAdapter.notifyDataSetChanged()
                            viewModel._deleteSuccess.value = false
                        }
                    }
                }
            }
        }

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
                        Item(), "category", args.id
                    )
                )
            }
            recyclerViewItems.adapter = itemAdapter.apply {
                setOnItemClickedListener(object : ItemRecyclerAdapter.OnItemClickedListener {
                    override fun onItemClick(item: Item) {
                        findNavController().navigate(
                            CategoryDetailFragmentDirections.actionCategoryDetailFragmentToEditItemFragment(
                                item, "category", null
                            )
                        )
                    }
                })
            }
            recyclerViewItems.layoutManager = LinearLayoutManager(context)
        }

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction == ItemTouchHelper.LEFT) {
                    val item = itemAdapter.currentList[viewHolder.absoluteAdapterPosition]
                    showYesNoDialog(
                        "Delete Item",
                        "Are you sure you want to delete ${item.name}?",
                        { viewModel.deleteItem(item.id) })
                    { itemAdapter.notifyDataSetChanged() }
//                    viewModel.deleteItem(itemAdapter.getItemAt(viewHolder.absoluteAdapterPosition).id)
//                    viewModel.deleteItem(itemAdapter.currentList[viewHolder.absoluteAdapterPosition].id)
                } else {
//                    mark as purchased
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_forever_white)
                    .addSwipeLeftBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red
                        )
                    )
                    .addSwipeRightActionIcon(R.drawable.ic_done1)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    .create()
                    .decorate()
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }).attachToRecyclerView(binding.recyclerViewItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}