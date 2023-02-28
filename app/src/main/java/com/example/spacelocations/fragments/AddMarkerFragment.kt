package com.example.spacelocations.fragments

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.spacelocations.R
import com.example.spacelocations.databinding.FragmentAddMarkerBinding
import com.example.spacelocations.models.Position.MarkerModel
import com.example.spacelocations.models.Position.Position
import com.example.spacelocations.viewmodel.ViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
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
        }

        binding.cameraCaptureButton.setOnClickListener {
            takePhoto()
            saveMarker()
        }
        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    private fun saveMarker()
    {

    }

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
                    val savedUri = Uri.fromFile(photoFile)

                    var category: Categories? = null
                    when (binding.categoriesSpinner.selectedItem.toString()) {
                        "Lift Off" -> category = Categories.LifOff
                        "Primary Stage" -> category = Categories.PrimaryStage
                        "Secondary Stage" -> category = Categories.SecondaryStage
                    }

                    val marker =
                        MarkerModel(
                            viewModel.selectedPosition.value!!,
                            // Position(41.4534227,2.1841046),
                            //Position(Random.nextDouble(0.0, 40.0), Random.nextDouble(0.0, 40.0)),
                            binding.titleEditText.text.toString(),
                            binding.descriptionEditText.text.toString(),
                            Calendar.getInstance().time.toString(),
                            savedUri,
                            category!!
                        )

                    viewModel.addMarker(marker)
                    println(savedUri)



                    findNavController().navigate(R.id.addmarker_to_map)
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
        cameraProviderFuture.addListener(
            Runnable {
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview =
                    Preview.Builder().build().also {
                        it.setSurfaceProvider(binding.cameraView.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            },
            ContextCompat.getMainExecutor(requireContext())
        )
    }

    private fun allPermissionsGranted() = AddMarkerFragment.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }
}
