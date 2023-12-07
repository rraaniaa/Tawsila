package com.example.tawsila

import CovoiturageAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListeCovoiturageActivity : AppCompatActivity(), CovoiturageAdapter.OnItemClickListener {

    private lateinit var covoiturageAdapter: CovoiturageAdapter
    val retrofit = Retrofit.Builder()
        .baseUrl(MicroServiceApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .build()

    private val microserviceApi = retrofit.create(MicroServiceApi::class.java)
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_covoiturages)

        recyclerView = findViewById(R.id.recyclerViewClients)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        covoiturageAdapter = CovoiturageAdapter(emptyList(), this)
        covoiturageAdapter.setOnItemClickListener(this)
        recyclerView.adapter = covoiturageAdapter

        // Call the function to fetch and display covoiturages
        fetchAndDisplayCovoiturages()
    }

    override fun onItemClick(covoiturage: Covoiturage) {
        // Handle item click, e.g.,launch DetailActivity
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("covoiturage", covoiturage)
        startActivity(intent)
    }

    private fun fetchAndDisplayCovoiturages() {
        val depart = "bizerte"
        val destination = "tunis"
        val date = "2023-11-21"

        val baseUrl = "http://192.168.56.1:8080/driver/covsddd/"
        val url = "${baseUrl}?depart=$depart&destination=$destination&date=$date"
        val call: Call<List<Covoiturage>> = microserviceApi.getFilteredCovoiturages(url)



        call.enqueue(object : Callback<List<Covoiturage>> {
            override fun onResponse(call: Call<List<Covoiturage>>, response: Response<List<Covoiturage>>) {
                if (response.isSuccessful) {
                    val covoituragesList: List<Covoiturage>? = response.body()
                    if (covoituragesList != null) {
                        Log.d("Liste", "Received ${covoituragesList.size} covoiturages")
                        covoiturageAdapter.setFilteredData(covoituragesList) // Update adapter data
                        recyclerView.visibility = View.VISIBLE
                    } else {
                        Log.e("Liste", "Response body is null")
                    }
                } else {
                    Log.e("Liste", "Failed to fetch filtered covoiturages: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Covoiturage>>, t: Throwable) {
                Log.e("Liste", "Error: ${t.message}")
                t.printStackTrace()
            }
        })
    }



}
