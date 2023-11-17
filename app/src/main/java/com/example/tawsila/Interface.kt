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
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
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

        val fab: FloatingActionButton = findViewById(R.id.fab)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        var isFabClicked = false

        fab.setOnClickListener {
            // Change the background color to white only if not already clicked
            if (!isFabClicked) {
                fab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, android.R.color.white))
                isFabClicked = true

                // Uncheck other items in BottomNavigationView
                for (i in 0 until bottomNavigationView.menu.size()) {
                    bottomNavigationView.menu.getItem(i).isChecked = false
                }

                // Add any other logic or actions you want to perform on click
            }
        }


    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
           // R.id.nav_home -> supportFragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment()).commit()
            R.id.nav_settings -> {
                    val intent = Intent(this, driver_profile::class.java)
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
}