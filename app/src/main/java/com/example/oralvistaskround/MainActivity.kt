package com.example.oralvistaskround

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {

    private lateinit var imageButton: ImageButton;
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var dbHelper: SessionDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageButton = findViewById(R.id.cameraButton)
        imageButton.setOnClickListener {
            supportFragmentManager.commit {
                replace(R.id.main, CaptureImageFragment()) // Your fragment
                addToBackStack(null) // Optional: allows back navigation
            }
        }

        dbHelper = SessionDatabase(this)

        searchEditText = findViewById(R.id.editTextSearch) // your EditText ID
        searchButton = findViewById(R.id.imageButton)

        searchButton.setOnClickListener {
            val sessionId = searchEditText.text.toString().trim()
            if (sessionId.isEmpty()) {
                Toast.makeText(this, "Enter Session ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val session = dbHelper.getSession(sessionId)
            if (session != null) {
                // Open session details fragment
                val fragment = SessionDetailsFragment()
                val bundle = Bundle()
                bundle.putSerializable(SessionDetailsFragment.ARG_SESSION, session)
                fragment.arguments = bundle

                supportFragmentManager.commit {
                    replace(R.id.main, fragment)
                    addToBackStack(null)
                }

            } else {
                Toast.makeText(this, "No such session found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}