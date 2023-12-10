package com.example.tawsila

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log

class ReservationDriverAdapter(
    private var originalReservationsList: List<Reservation>,
    private val context: Context
) : RecyclerView.Adapter<ReservationDriverAdapter.ViewHolder>() {

    private var filteredReservationsList: List<Reservation> = originalReservationsList
    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(reservation: Reservation)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_reservationdriver_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reservation = filteredReservationsList[position]

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(reservation)
            val intent = Intent(holder.itemView.context, ListeReservationActivity::class.java)
            holder.itemView.context.startActivity(intent)

        }

        holder.bind(reservation)
    }

    override fun getItemCount(): Int = filteredReservationsList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val _id: TextView = itemView.findViewById(R.id.participationIDTextView)
        private val etat: TextView = itemView.findViewById(R.id.etatTextView)
        private val departureTextView: TextView = itemView.findViewById(R.id.departTextView)
        private val destinationTextView: TextView = itemView.findViewById(R.id.destinationTextView)
        private val acceptButton: Button = itemView.findViewById(R.id.acceptButton) // Replace with your actual button ID
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton) // Replace with your actual button ID

        //     private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(reservation: Reservation) {
            _id.text = reservation.participationID ?: "N/A"

            val etatValue = reservation.etat
            etat.text = if (etatValue == 1) {
                "En attente"
            } else {
                "Accepted"
            }
            // Show accept and delete buttons if etatValue is 1, hide otherwise
            acceptButton.visibility = if (etatValue == 1) View.VISIBLE else View.GONE
            deleteButton.visibility = if (etatValue == 1) View.VISIBLE else View.GONE


            if (etatValue == 1) {
                etat.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorGreen))
                etat.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            } else {
                etat.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorBlue))
                etat.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            }
            Log.d("reservation", "Fetching res details : ${reservation}")

            // Fetch user details only if the reservation still matches the current item
            if (reservation.participationID != null) {
                Log.d("ViewHolder", "Fetching user details for reservation ID: ${reservation.participationID}")
                fetchUserDetails(reservation.clientID)
            } else {
                Log.e("ViewHolder", "Invalid reservation with null participationID")
            }
            acceptButton.setOnClickListener {
                // Perform the action when the "Accept" button is clicked
                handleAcceptButtonClick(reservation)
                Log.e("handleAcceptButtonClick", "handleAcceptButtonClick oku")

            }

            deleteButton.setOnClickListener {
                // Perform the action when the "Accept" button is clicked
                handleDeletedButtonClick(reservation)
                Log.e("handleAcceptButtonClick", "handleAcceptButtonClick oku")

            }
        }

        private fun handleAcceptButtonClick(reservation: Reservation) {
            val baseUrl = "http://192.168.56.1:3002/participationDriver/${reservation.participationID}"
            val url = "${baseUrl}?etat=2"
            Log.e("URL", "{$url}")

            val retrofit = Retrofit.Builder()
                .baseUrl(MicroServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .build()

            val microserviceApi = retrofit.create(MicroServiceApi::class.java)

            val call: Call<Reservation> = microserviceApi.UpdateReservations(url)

            call.enqueue(object : Callback<Reservation> {
                override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                    if (response.isSuccessful) {
                        val updatedReservation: Reservation? = response.body()
                        if (updatedReservation != null) {
                            Log.d("ReservationDriverAdapter", "Reservation updated successfully")
                            // Handle the updatedReservation as needed
                        } else {
                            Log.e("ReservationDriverAdapter", "Response body is null")
                        }
                    } else {
                        Log.e("ReservationDriverAdapter", "Failed to update reservation: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Reservation>, t: Throwable) {
                    Log.e("ReservationDriverAdapter", "Error updating reservation: ${t.message}")
                    // Handle the failure as you did before
                }
            })

        }


        private fun handleDeletedButtonClick(reservation: Reservation) {
            val baseUrl = "http://192.168.56.1:3002/participation/${reservation.participationID}"
            val url = "${baseUrl}?etat=2"
            Log.e("URL", "{$baseUrl}")

            val retrofit = Retrofit.Builder()
                .baseUrl(MicroServiceApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .build()

            val microserviceApi = retrofit.create(MicroServiceApi::class.java)

            val call: Call<Reservation> = microserviceApi.DeleteReservations(url)

            call.enqueue(object : Callback<Reservation> {
                override fun onResponse(call: Call<Reservation>, response: Response<Reservation>) {
                    if (response.isSuccessful) {
                        val updatedReservation: Reservation? = response.body()
                        if (updatedReservation != null) {
                            Log.d("ReservationDriverAdapter", "Reservation updated successfully")
                            // Handle the updatedReservation as needed
                        } else {
                            Log.e("ReservationDriverAdapter", "Response body is null")
                        }
                    } else {
                        Log.e("ReservationDriverAdapter", "Failed to update reservation: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Reservation>, t: Throwable) {
                    Log.e("ReservationDriverAdapter", "Error updating reservation: ${t.message}")
                    // Handle the failure as you did before
                }
            })

        }

        private fun fetchUserDetails(userID: Int) {
            // Use the Retrofit or any other networking library to make the call
            val baseUrl = "http://192.168.56.1:8080/auth/2" // Replace with your actual user details API endpoint

            Log.e("URL", "{$baseUrl}")
            val retrofit = Retrofit.Builder()
                .baseUrl(MicroServiceApi.BASE_URL)  // Corrected to use UserServiceApi
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build()
                )
                .build()

            val userServiceApi = retrofit.create(MicroServiceApi::class.java)
            val call: Call<UserDTO> = userServiceApi.getParticipantInfo(baseUrl)

            call.enqueue(object : Callback<UserDTO> {
                override fun onResponse(call: Call<UserDTO>, response: Response<UserDTO>) {
                    if (response.isSuccessful) {
                        val user: UserDTO? = response.body()
                        if (user != null) {
                            departureTextView.text = "Name: ${user.name}"
                            destinationTextView.text = "Email: ${user.email}"
                           // dateTextView.text = " ${user.roleName}"

                            // Log the coordinates
                            Log.d("ViewHolder", "Latitude: ${user.name}, Longitude: ${user.email}")
                        } else {
                            Log.e("ViewHolder", "Response body is null")
                        }
                    } else {
                        Log.e("ViewHolder", "Failed to fetch user details: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<UserDTO>, t: Throwable) {
                    Log.e("ViewHolder", "Error: ${t.message}")
                    t.printStackTrace()
                }
            })



        }
    }

    fun setFilteredData(newData: List<Reservation>) {
        filteredReservationsList = newData
        notifyDataSetChanged()
    }

    fun updateOriginalData(newData: List<Reservation>) {
        originalReservationsList = newData
        setFilteredData(newData)
    }
}
