package com.example.tawsila
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tawsila.UserDTO
import com.example.tawsila.R

class DriverAdapter(private val driversList: List<UserDTO>) : RecyclerView.Adapter<DriverAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver_cardview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val driver = driversList[position]

        holder.nameTextView.text = driver.name
        holder.emailTextView.text = driver.email
        // Ajoutez ici d'autres données du conducteur si nécessaire
    }

    override fun getItemCount(): Int {
        return driversList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.driverName)
        val emailTextView: TextView = itemView.findViewById(R.id.driverDetails)
        // Ajoutez ici d'autres vues pour les données supplémentaires du conducteur si nécessaire
    }
}