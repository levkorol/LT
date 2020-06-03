package com.skaz.eliot.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.skaz.eliot.R
import com.skaz.eliot.Services.UserDataService
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main_options.*

class OptionsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.nameLbl) as TextView
        navUsername.text = UserDataService.fio
        fioLbl.text = UserDataService.fio
        addressLbl.text = UserDataService.address

        val mToolbar = findViewById(R.id.toolbar) as Toolbar
        mToolbar.setTitle("№ ${UserDataService.schet}")

    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
                val deviceIntent = Intent(this, DeviceActivity::class.java)
                deviceIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(deviceIntent)
            }
            R.id.nav_gallery -> {
                val optionsIntent = Intent(this, OptionsActivity::class.java)
                optionsIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(optionsIntent)
            }
            R.id.nav_slideshow -> {
                UserDataService.logout()
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                startActivity(mainIntent)
            }

        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
