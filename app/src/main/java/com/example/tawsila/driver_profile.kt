package com.example.tawsila

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton

class driver_profile : AppCompatActivity() {
    private lateinit var imgProfile: ImageView
    // Declare the launcher for the image picker activity result
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!

                // Use the Uri object instead of File to avoid storage permissions
                imgProfile.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile) // Assurez-vous que le nom du fichier de mise en page XML correspondant est correct.
        // Vous pouvez ajouter du code supplémentaire pour configurer l'activité du profil du conducteur ici.
        // Initialize your ImageView
        imgProfile = findViewById(R.id.profile_image)

        // Set onClickListener for the FloatingActionButton
        val uploadButton: FloatingActionButton = findViewById(R.id.floatingActionButton4)
        uploadButton.setOnClickListener {
            // Launch the image picker
            ImagePicker.with(this)
                .compress(1024)         // Final image size will be less than 1 MB (Optional)
                .maxResultSize(
                    1080,
                    1080
                )  // Final image resolution will be less than 1080 x 1080 (Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
        }
    }