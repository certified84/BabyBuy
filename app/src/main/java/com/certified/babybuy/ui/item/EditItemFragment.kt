package com.certified.babybuy.ui.item

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.certified.babybuy.R
import com.certified.babybuy.databinding.FragmentEditItemBinding
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.ImageResizer
import com.certified.babybuy.util.UIState
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemViewModel by viewModels()
    private val args: EditItemFragmentArgs by navArgs()

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            try {
                val bitmap =
                    uri?.let {
                        MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            it
                        )
                    }
                Log.d("TAG", "onActivityResult: Actual Size: ${bitmap?.byteCount?.div(1048576)} MB")
                val scaledBitmap = bitmap?.let { ImageResizer.reduceBitmapSize(it, 240000) }
                Log.d(
                    "TAG",
                    "onActivityResult: Scaled Size: ${scaledBitmap?.byteCount?.div(1048576)} MB"
                )
                when {
                    bitmap != null && bitmap.byteCount > (1048576 * 50) -> {
                        showSnackbar("Image too large. Max size is 5MB")
                        return@registerForActivityResult
                    }
                    bitmap == null -> return@registerForActivityResult
                    else -> {
                        binding.ivItemImage.load(bitmap) {
                            transformations(RoundedCornersTransformation(20f))
                        }
//                    uploadImage(scaledBitmap)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                try {
                    val bitmap = intent?.extras!!["data"] as Bitmap?
                    Log.d(
                        "TAG",
                        "onActivityResult: Actual Size: ${bitmap?.byteCount?.div(1048576)} MB"
                    )
                    val scaledBitmap = bitmap?.let { ImageResizer.reduceBitmapSize(it, 240000) }
                    Log.d(
                        "TAG",
                        "onActivityResult: Scaled Size: ${scaledBitmap?.byteCount?.div(1048576)} MB"
                    )
                    when {
                        bitmap != null && bitmap.byteCount > (1048576 * 50) -> {
                            showSnackbar("Image too large. Max size is 5MB")
                            return@registerForActivityResult
                        }
                        bitmap == null -> return@registerForActivityResult
                        else -> {
                            binding.ivItemImage.load(bitmap) {
                                transformations(RoundedCornersTransformation(20f))
                            }
//                    uploadImage(scaledBitmap)
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.uiState = viewModel.uiState
        binding.item = args.item

        binding.apply {

            val keyListener = etName.keyListener
            val fadeInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            val fadeOutAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

            if (args.item.name.isBlank())
                viewModel.uiState.set(UIState.EDITING)
            else
                etName.keyListener = null

            btnEdit.setOnClickListener {
                viewModel.uiState.set(UIState.EDITING)
                etName.keyListener = keyListener
                etName.apply {
                    post { etName.setSelection(etName.text.toString().length) }
                    requestFocus()
                }
                btnEdit.startAnimation(fadeOutAnim)
                btnClose.startAnimation(fadeOutAnim)
                btnDone.startAnimation(fadeInAnim)
                tvClickChange.startAnimation(fadeInAnim)
            }
            btnDone.setOnClickListener {
                viewModel.uiState.set(UIState.HAS_DATA)
                etName.keyListener = null
                btnEdit.startAnimation(fadeInAnim)
                btnClose.startAnimation(fadeInAnim)
                btnDone.startAnimation(fadeOutAnim)
                tvClickChange.startAnimation(fadeOutAnim)
            }
            btnClose.setOnClickListener {
                findNavController().navigate(EditItemFragmentDirections.actionEditItemFragmentToHomeFragment())
            }
            ivItemImage.setOnClickListener {
                if (viewModel.uiState.get() == UIState.EDITING)
                    launchChangeItemImageDialog()
            }
        }
    }

    private fun launchChangeItemImageDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val selection = arrayOf(
            "Take picture",
            "Choose from gallery",
        )
        builder.setTitle("Options")
        builder.setSingleChoiceItems(selection, -1) { dialog: DialogInterface, which: Int ->
            when (which) {
                0 -> startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                1 -> getContent.launch("image/*")
            }
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onResume() {
        super.onResume()
        if (args.item.name.isBlank())
            binding.etName.apply {
                post { setSelection(text.toString().length) }
                requestFocus()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}