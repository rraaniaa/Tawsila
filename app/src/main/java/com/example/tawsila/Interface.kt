package com.example.tawsila

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.behavior.HideBottomViewOnScrollBehavior
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView




class Interface : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_interface)

        // Call the function to set up userId and BottomNavigationView
        setUpBottomNavigationView()

        drawerLayout = findViewById(R.id.drawer_layout)
        // Sample data for testing
        val imageList = listOf(
            ImageItem(R.drawable.ic_launcher_foreground, "Image 1"),
            ImageItem(R.drawable.ic_launcher_foreground, "Image 2"),
            ImageItem(R.drawable.ic_launcher_foreground, "Image 3"),
            ImageItem(R.drawable.ic_launcher_foreground, "Image 3"),
            ImageItem(R.drawable.ic_launcher_foreground, "Image 3")
        )

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(imageList)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val userId: Long = intent.getLongExtra("USER_ID", -1)

        when (item.itemId) {
           // R.id.nav_home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_settings -> {
                    val intent = Intent(this, driver_profile::class.java)
                    intent.putExtra("USER_ID", userId)

                startActivity(intent)}
          //  R.id.nav_share -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ShareFragment()).commit()
         //   R.id.nav_about -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, AboutFragment()).commit()
           R.id.nav_logout -> Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show()
        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun setUpBottomNavigationView() {
        val  userId = intent.getLongExtra("USER_ID", -1)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.bottom_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, Interface::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.bottom_trajet -> {
                    val intent = Intent(this, Profil::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.bottom_Add -> {
                    val intent = Intent(this, driver_trajet::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.bottom_notification -> {
                    val intent = Intent(this, profil_image::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.bottom_profil -> {
                    val intent = Intent(this, Profil::class.java)
                    intent.putExtra("USER_ID", userId)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

}