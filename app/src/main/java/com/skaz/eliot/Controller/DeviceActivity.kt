package com.skaz.eliot.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.skaz.eliot.Adapters.DevicesRecycleAdapter
import com.skaz.eliot.R
import com.skaz.eliot.Services.DataService
import com.skaz.eliot.Services.UserDataService
import com.skaz.eliot.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.skaz.eliot.Model.DevicesRequest

class DeviceActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var adapter: DevicesRecycleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        refreshDataAdapter()

        val layoutManager =
            LinearLayoutManager(this)
        devicesListView.layoutManager = layoutManager
        devicesListView.setHasFixedSize(true)

        toolbar.title = "Устройства абонента: 0"

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(userDataChangeReceiver, IntentFilter(BROADCAST_USER_DATA_CHANGE))

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.nameLbl) as TextView
        navUsername.text = UserDataService.fio

        val mSwipe = findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipe.setOnRefreshListener {
            refreshDataAdapter()
        }
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

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (App.prefs.session.isNotEmpty()) {
                nameLbl.text = UserDataService.fio
            }
        }
    }

    private fun refreshDataAdapter() {
        val mSwipe = findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipe.isRefreshing = true
        DataService.deviceRequest(DevicesRequest(App.prefs.session)) { devices ->
            if (devices != null) {
                toolbar.title = "Устройства абонента: ${devices.count()}"
                adapter = DevicesRecycleAdapter(
                    this,
                    devices
                )
                devicesListView.adapter = adapter
            }
            mSwipe.isRefreshing = false
        }
    }
}
