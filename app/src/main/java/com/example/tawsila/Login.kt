package com.example.tawsila

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Login : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.18:8080")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .build()

    private val microserviceApi = retrofit.create(MicroServiceApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        val usernameLayout = findViewById<TextInputLayout>(R.id.username)
        val passwordLayout = findViewById<TextInputLayout>(R.id.password)

        val usernameEditText = usernameLayout.findViewById<TextInputEditText>(R.id.editTextUsername)
        val passwordEditText = passwordLayout.findViewById<TextInputEditText>(R.id.editTextPassword)



        val loginButton = findViewById<Button>(R.id.goButton)
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Create an instance of AuthRequest
            val authRequest = AuthRequest(username, password)

            // Make the API call for authentication
            val loginCall: Call<String> = microserviceApi.getToken(authRequest)
            // Check if email is in a valid format
            if (username.isEmpty()) {
                usernameLayout.error = "Enter a username"
                return@setOnClickListener
            } else {
                // Clear the error if email is valid
                usernameLayout.error = null
            }

            // Check if password not empty
            if (password.isEmpty()) {
                passwordLayout.error = "Passwords cannot be empty"
                return@setOnClickListener
            } else {
                // Clear the error if passwords match
                passwordLayout.error = null
            }
            loginCall.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        val token: String? = response.body()
                        // Handle the successful login, maybe store the token for future requests
                        Log.d("Login", "Login successful. Token: $token")
                        // Navigate to the next activity since login was successful
                        val intent = Intent(this@Login, Interface::class.java)
                        startActivity(intent)

                    } else {
                        // Handle unsuccessful login
                        val errorBody = response.errorBody()?.string()
                        Log.e("Login", "Login failed: $errorBody")
                        // Check the response code for invalid credentials
                        if (response.code() == 401) {
                            // Unauthorized (invalid username or password)
                            // Show an error message to the user
                            Toast.makeText(this@Login, "Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // Handle network or other errors during login
                    Log.e("Login", "Login failed: ${t.message}")
                    t.printStackTrace()
               }
            })
        }

        val goSignupButton = findViewById<Button>(R.id.goSignup)
        goSignupButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}