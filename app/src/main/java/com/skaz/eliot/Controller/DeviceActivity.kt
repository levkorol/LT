package com.skaz.eliot.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
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
import java.util.*
import android.support.v4.widget.SwipeRefreshLayout
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DevicesRequest


class DeviceActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var startCal = Calendar.getInstance()
    var endCal = Calendar.getInstance()
    var yearStart = startCal.get(Calendar.YEAR)
    var monthStart = startCal.get(Calendar.MONTH)
    var dayStart = startCal.get(Calendar.DAY_OF_MONTH)
    var yearEnd = endCal.get(Calendar.YEAR)
    var monthEnd = endCal.get(Calendar.MONTH)
    var dayEnd = endCal.get(Calendar.DAY_OF_MONTH)
    lateinit var adapter: DevicesRecycleAdapter

    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        adapter = DevicesRecycleAdapter(
            this,
            ArrayList<Device>()
        )

        refreshDataAdapter()

        devicesListView.adapter = adapter

        val layoutManager = LinearLayoutManager(this)
        devicesListView.layoutManager = layoutManager
        devicesListView.setHasFixedSize(true)

        toolbar.title = "Устройства абонента: ${adapter.itemCount}" //TODO sozdat sobitie

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
            if (UserDataService.isLoggedIn) {
                nameLbl.text = UserDataService.fio
            }
        }
    }

    private fun refreshDataAdapter() {
        val mSwipe = findViewById<View>(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        mSwipe.isRefreshing = true
        DataService.deviceRequest(DevicesRequest(UserDataService.authToken)) { devices ->
            if (devices != null) {
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
