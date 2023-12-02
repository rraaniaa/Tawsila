package com.example.tawsila

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SignUp : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.69:8080")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    private val microserviceApi = retrofit.create(MicroServiceApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_sign_up)

        val nameLayout = findViewById<TextInputLayout>(R.id.name)
        val emailLayout = findViewById<TextInputLayout>(R.id.email)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password)
        val repasswordLayout = findViewById<TextInputLayout>(R.id.repassword)
        val spinnerGender = findViewById<Spinner>(R.id.spinnerGender)
        val goButton = findViewById<Button>(R.id.register)
        val loginButton = findViewById<Button>(R.id.Login)


        // Initialize the Spinner for Gender
        val genderArray = resources.getStringArray(R.array.gender_options)
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        // Set a click listener on the "GO" button
        goButton.setOnClickListener {
            val name = nameLayout.editText?.text.toString()
            val email = emailLayout.editText?.text.toString()
            val password = passwordLayout.editText?.text.toString()
            val repassword = repasswordLayout.editText?.text.toString()
            val gender = spinnerGender.selectedItem.toString()

            // Check if email is in a valid format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailLayout.error = "Enter a valid email address"
                return@setOnClickListener
            } else {
                // Clear the error if email is valid
                emailLayout.error = null
            }

            // Check if password and confirm password match
            if (password != repassword) {
                repasswordLayout.error = "Passwords do not match"
                return@setOnClickListener
            } else {
                // Clear the error if passwords match
                repasswordLayout.error = null
            }

            // Example: Register a new user
            val newUserDTO = UserDTO(name, email, password,null, gender)
            val registerCall: Call<String> = microserviceApi.registerUser(newUserDTO)

            registerCall.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val message: String? = response.body()
                        // Handle the registration success message here
                        if (message != null) {
                            // Process the success message
                            Log.d("Registration", "Registration successful: $message")
                            // After successful registration, navigate to the login activity
                        }
                    } else {
                        // Handle unsuccessful registration
                        val errorBody = response.errorBody()?.string()
                        Log.e("Registration", "Registration failed: $errorBody")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle network or other errors during registration
                    Log.e("Registration", "Registration failed: ${t.message}")
                    t.printStackTrace()
                }
            })
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        // Set a click listener on the "Already have an account? LogIn" button
        loginButton.setOnClickListener {
            // Navigate to the login activity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
        }
}