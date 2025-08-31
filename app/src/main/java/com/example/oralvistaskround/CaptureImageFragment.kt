package com.example.oralvistaskround

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.oralvistaskround.R
import java.io.File

class CaptureImageFragment : Fragment() {

    private lateinit var imageButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter
    private lateinit var doneButton: View

    private val imageList = mutableListOf<Uri>()
    private var imageUri: Uri? = null

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && imageUri != null) {
                imageList.add(imageUri!!)
                adapter.notifyItemInserted(imageList.size - 1)
            }
        }

    private val requestCameraPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                launchCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_capture_image, container, false)

        imageButton = view.findViewById(R.id.cameraButton)
        recyclerView = view.findViewById(R.id.recyclerView)
        doneButton = view.findViewById(R.id.doneButton)

        adapter = ImageAdapter(imageList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        imageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                launchCamera()
            } else {
                requestCameraPermission.launch(android.Manifest.permission.CAMERA)
            }
        }

        doneButton.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelableArrayList("images", ArrayList(imageList))
            }
            val fragment = userDetails().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_capture_image, fragment)
                .commit()
        }

        return view
    }

    private fun launchCamera() {
        val imageFile = File(requireContext().cacheDir, "${System.currentTimeMillis()}.jpg")
        imageUri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            imageFile
        )
        takePhotoLauncher.launch(imageUri)
    }
}
