package com.certified.babybuy.ui.item

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.certified.babybuy.R
import com.certified.babybuy.data.model.Category
import com.certified.babybuy.data.model.Contact
import com.certified.babybuy.data.model.Item
import com.certified.babybuy.data.model.Location
import com.certified.babybuy.databinding.FragmentEditItemBinding
import com.certified.babybuy.util.Extensions.showActionDialog
import com.certified.babybuy.util.Extensions.showSnackbar
import com.certified.babybuy.util.UIState
import com.certified.babybuy.util.currentDate
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException

@AndroidEntryPoint
class EditItemFragment : Fragment() {

    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ItemViewModel by viewModels()
    private val args: EditItemFragmentArgs by navArgs()

    private var category: Category? = null
    private var delegate: Contact? = null
    private var location: Location? = null
    private var imageUri: Uri? = null
    private var item = Item()
    private lateinit var placesClient: PlacesClient
    private val fadeInAnim by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.fade_in
        )
    }
    private val fadeOutAnim by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.fade_out
        )
    }

    private val requestReadContactPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                startForResultContact.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI
                    ).apply {
                        type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                    })
            else
                showActionDialog(
                    "Permission",
                    "The READ_CONTACTS and SEND_SMS permission is required to enable the item delegation feature",
                    null
                )
        }

    private val requestSendSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                sendSms()
            else
                showActionDialog(
                    "Permission",
                    "The READ_CONTACTS and SEND_SMS permission is required to enable the item delegation feature",
                    null
                )
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            try {
                val bitmap = uri?.let {
                    MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        it
                    )
                }
//                val scaledBitmap = bitmap?.let { ImageResizer.reduceBitmapSize(it, 240000) }
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
                        requireContext().openFileOutput("item_image", Context.MODE_PRIVATE)
                            .use {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                            }
                        val file = File(requireContext().filesDir, "item_image")
                        imageUri = Uri.fromFile(file)
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
//                    val scaledBitmap =
//                        bitmap?.let { ImageResizer.reduceBitmapSize(it, 240000) }
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
                            requireContext().openFileOutput(
                                "item_image",
                                Context.MODE_PRIVATE
                            )
                                .use {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                                }
                            val file = File(requireContext().filesDir, "item_image")
                            imageUri = Uri.fromFile(file)
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (t: Throwable) {
                    t.printStackTrace()
                }
            }
        }

    private val startForResultContact =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d("TAG", "Result: ${result.data}")
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val intent = result.data
                val projection = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                )
                val cursor = intent?.data?.let {
                    requireContext().contentResolver.query(
                        it, projection, null, null, null
                    )
                }
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        val nameIndex =
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val numberIndex =
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val number = cursor.getString(numberIndex)
                        val name = cursor.getString(nameIndex)
                        delegate = Contact(name, number)
                        showSnackbar("Name: $name, Number: $number")
                    }
                } catch (t: Throwable) {
                    t.printStackTrace()
                } finally {
                    cursor?.close()
                }
            }
        }

    private val startForResultMap =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d("TAG", "Result: ${result.data}")
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val place = result.data?.let { Autocomplete.getPlaceFromIntent(it) }
                    location =
                        Location(place?.name, place?.latLng?.latitude, place?.latLng?.longitude)
                    showSnackbar("Selected location: ${place?.name}")
                    Log.d("TAG", "Place: $place")
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    val status = result.data?.let { Autocomplete.getStatusFromIntent(it) }
                    status?.statusMessage?.let { Log.d("TAG", it) }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditItemBinding.inflate(layoutInflater, container, false)
        placesClient = Places.createClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.uiState = viewModel.uiState
        binding.item = args.item
        delegate = args.item.delegate
        location = args.item.location

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.message.collect {
                        it?.let {
                            showSnackbar(it)
                            viewModel._message.value = null
                        }
                    }
                }
                launch {
                    viewModel.categories.collect {
                        if (args.categoryId != null) {
                            category = it.find { it1 -> it1.id == args.categoryId }
                            binding.etCategoryTitle.setText(category?.title)
                        }
                    }
                }
                launch {
                    viewModel.uploadSuccess.collect {
                        if (it && delegate != null) {
                            requestSendSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                            viewModel._uploadSuccess.value = false
                        }
                    }
                }
            }
        }

        binding.apply {

            val nameKeyListener = etName.keyListener
            val descriptionKeyListener = etDescription.keyListener
            val priceKeyListener = etPrice.keyListener
            val fadeInAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
            val fadeOutAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)

            etName.keyListener = null
            etDescription.keyListener = null
            etPrice.keyListener = null

            btnEdit.setOnClickListener {
                this@EditItemFragment.viewModel.uiState.set(UIState.EDITING)
                etName.keyListener = nameKeyListener
                etDescription.keyListener = descriptionKeyListener
                etPrice.keyListener = priceKeyListener
                etName.apply {
                    post { etName.setSelection(etName.text.toString().length) }
                    requestFocus()
                }
                btnEdit.startAnimation(fadeOutAnim)
                btnClose.startAnimation(fadeOutAnim)
                btnDone.startAnimation(fadeInAnim)
                tvClickChange.startAnimation(fadeInAnim)
            }
            btnDone.setOnClickListener { updateItem() }
            btnClose.setOnClickListener {
                when (args.from) {
                    "home" ->
                        findNavController().navigate(EditItemFragmentDirections.actionEditItemFragmentToHomeFragment())
                    else ->
                        findNavController().navigate(
                            EditItemFragmentDirections.actionEditItemFragmentToCategoryDetailFragment(
                                category?.id ?: args.item.categoryId
                            )
                        )
                }
            }
            ivItemImage.setOnClickListener {
                if (this@EditItemFragment.viewModel.uiState.get() == UIState.EDITING)
                    launchChangeItemImageDialog()
            }
            btnDelegate.setOnClickListener {
                if (this@EditItemFragment.viewModel.uiState.get() == UIState.EDITING) {
                    requestReadContactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                } else {
                    if (delegate == null)
                        showSnackbar("No contact selected")
                    else showSnackbar("Name: ${delegate?.name}, number: ${delegate?.phone}")
                }
            }
            btnMap.setOnClickListener {
                if (this@EditItemFragment.viewModel.uiState.get() == UIState.EDITING) {
                    startForResultMap.launch(
                        Autocomplete.IntentBuilder(
                            AutocompleteActivityMode.FULLSCREEN,
                            listOf(Field.ID, Field.NAME)
                        )
                            .build(requireActivity())
                    )
                } else if (location != null) {
                    val gmmIntentUri =
                        Uri.parse("google.navigation:q=${Uri.encode(location?.name)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                } else
                    showSnackbar("No location selected")
            }
        }
    }

    private fun updateItem() {
        binding.apply {

            val name = etName.text.toString()
            val description = etDescription.text.toString()
            val price = etPrice.text.toString()

            if (name.isBlank()) {
                etNameLayout.error = "Name is required"
                etName.requestFocus()
                return
            }
            etNameLayout.error = null

            if (description.isBlank()) {
                etDescriptionLayout.error = "Description is required"
                etDescription.requestFocus()
                return
            }
            etDescriptionLayout.error = null

            if (price.isBlank()) {
                etPriceLayout.error = "Price is required"
                etPrice.requestFocus()
                return
            }
            etPriceLayout.error = null

            if (category == null) {
                etCategoryTitleLayout.error = "Category is required"
                etCategoryTitle.requestFocus()
                return
            }
            etCategoryTitleLayout.error = null

            with(this@EditItemFragment.viewModel) {
                uiState.set(UIState.LOADING)
                this@EditItemFragment.item = args.item.copy(
                    name = name,
                    description = description,
                    price = price.toDouble(),
                    modified = currentDate().timeInMillis,
                    delegate = delegate ?: args.item.delegate,
                    location = location ?: args.item.location,
                    categoryId = category?.id,
                    categoryTitle = category?.title,
                    hex = category?.hex
                )
                updateItem(this@EditItemFragment.item, imageUri)
            }

            etName.keyListener = null
            etDescription.keyListener = null
            etPrice.keyListener = null
            btnEdit.startAnimation(fadeInAnim)
            btnClose.startAnimation(fadeInAnim)
            btnDone.startAnimation(fadeOutAnim)
            tvClickChange.startAnimation(fadeOutAnim)
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

    private fun sendSms() {
        val message = "Item details:\nName: ${item.name}\n" +
                "Description: ${item.description}\n" +
                "Price: ${item.price}"
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(delegate?.phone, null, message, null, null)
            showSnackbar("SMS sent")
        } catch (e: Exception) {
            showSnackbar("An error occurred. Try again")
            try {
                requireActivity().startActivity(Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("sms:${delegate?.phone}")
                    putExtra("sms_body", message)
                })
            } catch (e: ActivityNotFoundException) {
                showSnackbar("No message app found")
                e.printStackTrace()
            } catch (e: Exception) {
                showSnackbar("An error occurred: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val categoryList = mutableListOf<String>()
        lifecycleScope.launchWhenResumed {
            viewModel.categories.collect { categoryList.addAll(it.map { it1 -> it1.title }) }
        }
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item, categoryList
        )
        binding.etCategoryTitle.setAdapter(arrayAdapter)

        binding.etCategoryTitle.setOnItemClickListener { _, _, i, _ ->
            category = viewModel.categories.value[i]
            Log.d("TAG", "onResume: Category: $category")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}