package com.example.tawsila

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream

class driver_profile : AppCompatActivity() {
    private lateinit var imgProfile: ImageView
    private lateinit var fullNameEditText: TextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var selectedImageUri: Uri


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://169.254.142.86:8080")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    private val microserviceApi = retrofit.create(MicroServiceApi::class.java)

    // Declare the launcher for the image picker activity result
    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                // Image Uri will not be null for RESULT_OK
                val fileUri = data?.data!!
                selectedImageUri = fileUri

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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_driver_profile) // Assurez-vous que le nom du fichier de mise en page XML correspondant est correct.
        // Vous pouvez ajouter du code supplémentaire pour configurer l'activité du profil du conducteur ici.
        // Initialize your ImageView
        val userId: Long = intent.getLongExtra("USER_ID", -1)
        Log.e("YourTag", "userID: ${userId}")

        imgProfile = findViewById(R.id.profile_image)
        fullNameEditText = findViewById(R.id.full_name)
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)


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

        // Call the API to get user information based on the user ID
// Replace the placeholder "getUserInfo" with your actual API endpoint
        val getUserInfoCall: Call<UserDTO> = microserviceApi.getUserInfo(userId)
        getUserInfoCall.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    // Fill the input fields with user information
                    fullNameEditText.setText(user?.name)
                    usernameEditText.setText(user?.name)
                    emailEditText.setText(user?.email)

                    // Check and handle profile image
                    val profileImage = user?.profileImage
                    if (profileImage != null) {
                        when (profileImage) {
                            is ByteArray -> {
                                // Handle byte array (e.g., convert it to a Bitmap and set it to an ImageView)
                                val bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.size)
                                imgProfile.setImageBitmap(bitmap)
                            }
                            is String -> {
                                // Handle the case where the server sends the image as a base64-encoded string
                                // You need to decode the base64 string and set it to an ImageView
                                val decodedBytes = Base64.decode(profileImage as String, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                imgProfile.setImageBitmap(bitmap)
                            }
                            else -> {
                                // Handle other cases if needed
                            }
                        }
                    }
                } else {
                    Log.e("YourTag", "Failed to retrieve user information. Code: ${response.code()}")
                    Log.e("YourTag", "Response: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e("YourTag", "Network error: ${t.message}", t)
            }
        })
// Call the API to update user information and profile image
        val updateButton: Button = findViewById(R.id.update)
        updateButton.setOnClickListener {
            // Retrieve user input data
            val fullName = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
           val password = passwordEditText.text.toString()

            // Check if the profile image has been initialized
            if (::selectedImageUri.isInitialized) {
                // Call the API to upload the image
                uploadImage(userId, selectedImageUri)
            }


            // Create a UserDTO object with the updated data
            val updatedUser: UserDTO

            // Check if the password input is not empty
            if (password.isNotEmpty()) {
                updatedUser = UserDTO(fullName, email, password, null)
            } else {
                // If the password is empty, create a UserDTO without updating the password
                updatedUser = UserDTO(fullName, email, null, null)
            }


            // Make the API call to update user information
            val call: Call<String> = microserviceApi.updateUser(userId, updatedUser)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        // Handle success
                        Toast.makeText(
                            this@driver_profile,
                            "User information updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        refreshUserData(userId)
                        // Optionally, navigate to another activity or perform other actions
                        // Example: startActivity(Intent(this@YourActivity, AnotherActivity::class.java))
                    } else {
                        // Handle failure
                        Toast.makeText(
                            this@driver_profile,
                            "Failed to update user information",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle network errors or other failures
                    Toast.makeText(
                        this@driver_profile,
                        "Network error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }




    }
    // Function to convert Bitmap image to base64 string
   /* private fun convertImageToBase64(imageView: ImageView): String? {
        val drawable = imageView.drawable
        if (drawable is BitmapDrawable) {
            val bitmap = drawable.bitmap
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
            return Base64.encodeToString(byteArray, Base64.DEFAULT)
        }
        return null
    }
*/
    // Function to refresh user data from the API
    private fun refreshUserData(userId: Long) {
        val getUserInfoCall: Call<UserDTO> = microserviceApi.getUserInfo(userId)
        getUserInfoCall.enqueue(object : Callback<UserDTO> {
            override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                if (response.isSuccessful) {
                    val user = response.body()

                    // Update the UI with the refreshed user information
                    fullNameEditText.setText(user?.name)
                    usernameEditText.setText(user?.name)
                    emailEditText.setText(user?.email)

                    // Check and handle profile image
                    val profileImage = user?.profileImage
                    if (profileImage != null) {
                        when (profileImage) {
                            is ByteArray -> {
                                val bitmap = BitmapFactory.decodeByteArray(profileImage, 0, profileImage.size)
                                imgProfile.setImageBitmap(bitmap)
                            }
                            is String -> {
                                val decodedBytes = Base64.decode(profileImage as String, Base64.DEFAULT)
                                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                                imgProfile.setImageBitmap(bitmap)
                            }
                            else -> {
                                // Handle other cases if needed
                            }
                        }
                    }
                } else {
                    Log.e("YourTag", "Failed to retrieve user information. Code: ${response.code()}")
                    Log.e("YourTag", "Response: ${response.raw()}")
                }
            }

            override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                Log.e("YourTag", "Network error: ${t.message}", t)
            }
        })
    }
    private fun uploadImage(userId: Long, fileUri: Uri) {
        // Check if selectedImageUri has been initialized
        if (!::selectedImageUri.isInitialized) {
            Log.e("profil_image", "Please select an image first")
            return
        }

        // Utilisez l'InputStream pour obtenir le contenu du fichier
        val contentResolver = this.contentResolver
        val inputStream: InputStream? = contentResolver.openInputStream(fileUri)

        // Vérifiez si l'InputStream est non nul
        if (inputStream != null) {
            // Utilisez l'InputStream pour obtenir le contenu du fichier
            val byteArray = inputStream.readBytes()

            // Créez une demande de fichier avec le contenu lu
            val requestFile: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), byteArray)
            val body: MultipartBody.Part = MultipartBody.Part.createFormData("file", "image_file", requestFile)

            // Appelez l'API pour télécharger l'image
            val call: Call<String> = microserviceApi.setImageById(userId, body)

            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        // Image uploaded successfully
                        Log.i("profil_image", "Image uploaded successfully")
                    } else {
                        // Handle unsuccessful response
                        Log.e("profil_image", "Unsuccessful response: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle network or other errors during image upload
                    Log.e("profil_image", "Error: ${t.message}")
                }
            })

            // Fermez l'inputStream après utilisation
            inputStream.close()
        } else {
            Log.e("profil_image", "Invalid InputStream for selected image")
        }
    }
    }

