package com.example.oralvistaskround

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class userDetails : Fragment() {

    private lateinit var sessionID: EditText
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var saveButton: Button
    private var images: ArrayList<Uri>? = null
    private lateinit var dbHelper: SessionDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionID = view.findViewById(R.id.sessionID)
        name = view.findViewById(R.id.name)
        age = view.findViewById(R.id.age)
        saveButton = view.findViewById(R.id.saveButton)

        // Get images from bundle
        @Suppress("DEPRECATION")
        images = arguments?.getParcelableArrayList("images")

        dbHelper = SessionDatabase(requireContext())

        saveButton.setOnClickListener {
            val id = sessionID.text.toString().trim()
            val userName = name.text.toString().trim()
            val userAge = age.text.toString().trim()

            if (dbHelper.sessionExists(id)) {
                Toast.makeText(requireContext(), "Session ID already exists!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (id.isEmpty() || userName.isEmpty() || userAge.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (images.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No images found!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedPaths = mutableListOf<String>()

            // Save each image to /Android/media/<AppName>/Sessions/<SessionID>/
            val appName = "OralVis" // change to your app name
            val dir = File(
                requireContext().getExternalMediaDirs()[0],
                "$appName/Sessions/$id"
            )
            if (!dir.exists()) dir.mkdirs()

            images?.forEach { uri ->
                val timeStamp = System.currentTimeMillis()
                val fileName = "IMG_${timeStamp}.jpg"
                val destFile = File(dir, fileName)

                if (!destFile.exists()) {  // prevent duplicate save
                    saveUriToFile(uri, destFile)
                    savedPaths.add(destFile.absolutePath)
                }
            }

            // Save into SQLite
            dbHelper.insertOrUpdateSession(
                id,
                userName,
                userAge.toInt(),
                savedPaths.joinToString(",") // store as comma-separated
            )

            Toast.makeText(requireContext(), "Session saved!", Toast.LENGTH_SHORT).show()

            parentFragmentManager.popBackStack()
        }
    }

    private fun saveUriToFile(uri: Uri, destFile: File) {
        try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(destFile)

            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream?.read(buffer).also { length = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, length)
            }

            outputStream.flush()
            outputStream.close()
            inputStream?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
