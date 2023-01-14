package com.certified.babybuy.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.certified.babybuy.R
import com.certified.babybuy.adapters.CategoryRecyclerAdapter
import com.certified.babybuy.adapters.ItemRecyclerAdapter
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.databinding.FragmentHomeBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.Extensions.showYesNoDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private val itemAdapter by lazy { ItemRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        binding.recentUIState = viewModel.recentUIState
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
                    viewModel.updateSuccess.collect {
                        if (it) {
                            itemAdapter.notifyDataSetChanged()
                            viewModel._updateSuccess.value = false
                        }
                    }
                }
            }
        }

        binding.apply {
            tvName.text =
                "Hey,\n${auth.currentUser?.displayName?.substringBefore(" ")} \uD83D\uDC4B"

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

            recyclerViewItems.adapter = itemAdapter.apply {
                setOnItemClickedListener(object : ItemRecyclerAdapter.OnItemClickedListener {
                    override fun onItemClick(item: Item) {
                        findNavController().navigate(
                            HomeFragmentDirections.actionHomeFragmentToEditItemFragment(
                                item, "home", null
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
                        Item(), "home", null
                    )
                )
            }
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
                val item = itemAdapter.currentList[viewHolder.absoluteAdapterPosition]
                if (direction == ItemTouchHelper.LEFT) {
                    showYesNoDialog(
                        "Delete Item",
                        "Are you sure you want to delete ${item.name}?",
                        { viewModel.deleteItem(item.id) })
                    { itemAdapter.notifyDataSetChanged() }
                } else {
                    if (item.purchased) {
                        showSnackbar("Item already marked as purchased")
                        itemAdapter.notifyDataSetChanged()
                        return
                    }
                    showYesNoDialog(
                        "Mark item as purchased",
                        "Are you sure you want to mark ${item.name} as purchased?",
                        { viewModel.updateItem(item.id, true) })
                    { itemAdapter.notifyDataSetChanged() }
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
                    .addSwipeRightActionIcon(R.drawable.ic_done2)
                    .addSwipeRightBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green
                        )
                    )
                    .setActionIconTint(Color.WHITE)
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