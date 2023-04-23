package com.example.spacelocations.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacelocations.Categories
import com.example.spacelocations.MarkerR
import com.example.spacelocations.R
import com.example.spacelocations.ServiceLocator
import com.example.spacelocations.databinding.FragmentAddMarkerBinding
import com.example.spacelocations.viewmodel.ViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class AddMarkerFragment : Fragment() {
    lateinit var binding: FragmentAddMarkerBinding
    private val viewModel: ViewModel by activityViewModels()

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    lateinit var savedUri: Uri

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMarkerBinding.inflate(layoutInflater)

        this.binding.cameraView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                AddMarkerFragment.REQUIRED_PERMISSIONS,
                AddMarkerFragment.REQUEST_CODE_PERMISSIONS
            )
            startCamera()
        }

        binding.cameraCaptureButton.setOnClickListener {takePhoto() }
        binding.browsePhotoButton.setOnClickListener { browsePhoto() }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.add.setOnClickListener {
            if(viewModel.editMode.value!!)
            {
                var category: Categories? = null
                when (binding.categoriesSpinner.selectedItem.toString()) {
                    "Lift Off" -> category = Categories.LifOff
                    "Primary Stage" -> category = Categories.PrimaryStage
                    "Secondary Stage" -> category = Categories.SecondaryStage
                    else -> category = Categories.All
                }

                val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageInByte = baos.toByteArray()

                ServiceLocator.itemsDao.updateItem(
                    id = viewModel.detailMarker.value!!._id,
                    title = binding.titleEditText.text.toString(),
                    description = binding.descriptionEditText.text.toString(),
                    category = category.toString(),
                    image = imageInByte
                )
                Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
                Thread.sleep(2_000)
                findNavController().navigate(R.id.addmarker_to_map)
            } else
            {
                if(this::savedUri.isInitialized) {
                    val imageByteArray = savedUri.let { uri ->
                        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                            inputStream.readBytes()
                        }
                    }

                    if (imageByteArray != null) {

                        var category: Categories? = null
                        when (binding.categoriesSpinner.selectedItem.toString()) {
                            "Lift Off" -> category = Categories.LifOff
                            "Primary Stage" -> category = Categories.PrimaryStage
                            "Secondary Stage" -> category = Categories.SecondaryStage
                            else -> category = Categories.All
                        }

                        ServiceLocator.itemsDao.insertItem(
                           title = binding.titleEditText.text.toString(),
                           description = binding.descriptionEditText.text.toString(),
                           date = Calendar.getInstance().time.toString(),
                           lat =  viewModel.selectedPosition.value!!.latitude,
                           lon =  viewModel.selectedPosition.value!!.longitude,
                           category = category.toString(),
                           image = imageByteArray)
                    }
                    Toast.makeText(requireContext(), "Added!", Toast.LENGTH_SHORT).show()
                    Thread.sleep(2_000)
                    findNavController().navigate(R.id.addmarker_to_map)
                }
            }
        }

        if(viewModel.editMode.value!!)
        {
            binding.add.text = "Save"

            val marker : MarkerR = viewModel.detailMarker.value!!

            binding.titleEditText.setText( marker.title, TextView.BufferType.EDITABLE)
            binding.descriptionEditText.setText( marker.description, TextView.BufferType.EDITABLE)

            when (marker.category) {
                "Lift Off" -> binding.categoriesSpinner.setSelection(0)
                "Primary Stage" -> binding.categoriesSpinner.setSelection(1)
                "Secondary Stage" -> binding.categoriesSpinner.setSelection(2)
            }

            val bmp = BitmapFactory.decodeByteArray(marker.image, 0, marker.image!!.size)
            binding.imageView.setImageBitmap(bmp)


            binding.cameraView.visibility = View.INVISIBLE
            binding.imageView.visibility = View.VISIBLE
        }

        return binding.root
    }

    fun browsePhoto(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val imageList = listOf<ImageView>(binding.imageView)

            if(data?.clipData != null){
                var count = data.clipData?.itemCount
                for(i in 0 until count!!){
                    var imageUri:Uri = data.clipData?.getItemAt(i)!!.uri
                    imageList[i].setImageURI(imageUri)
                }
            }
            else if(data?.data != null){
                val imageUri: Uri = data.data!!
                savedUri = imageUri
                binding.imageView.setImageURI(null)
                binding.imageView.setImageURI(savedUri)
                binding.cameraView.visibility = View.INVISIBLE
                binding.imageView.visibility = View.VISIBLE
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory,
            SimpleDateFormat(AddMarkerFragment.FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(outputOptions,
            ContextCompat.getMainExecutor(requireContext()), object: ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(AddMarkerFragment.TAG, "Photo capture failed: ${exc.message}", exc)
                }
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    savedUri = Uri.fromFile(photoFile)
                    val msg = "Photo capture succeeded: $savedUri"
                    Log.d(AddMarkerFragment.TAG, msg)
                    binding.imageView.setImageURI(null)
                    binding.imageView.setImageURI(savedUri)
                    binding.cameraView.visibility = View.GONE
                    binding.imageView.visibility = View.VISIBLE
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireContext().filesDir
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraView.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun allPermissionsGranted() = AddMarkerFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
}
