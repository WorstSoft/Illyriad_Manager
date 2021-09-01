package com.worstsoft.illyriadmanager

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import com.worstsoft.illyriadmanager.dialogs.InsertDataDialog
import com.worstsoft.illyriadmanager.fragments.MainMenuFragment
import com.worstsoft.illyriadmanager.fragments.NotificationFragment
import com.worstsoft.illyriadmanager.util.NotificationAdapter
import com.worstsoft.illyriadmanager.util.XMLParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private val notificationFragment: NotificationFragment = NotificationFragment()
    private val mainMenuFragment: MainMenuFragment = MainMenuFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_fragment_container, mainMenuFragment)
            commit()
        }

        val drawerLayout = findViewById<DrawerLayout>(R.id.main_drawer_layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        findViewById<NavigationView>(R.id.main_navigation).setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_item -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.main_fragment_container, mainMenuFragment)
                        commit()
                    }
                    true
                }
                R.id.notification_item -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.main_fragment_container, notificationFragment)
                        commit()
                    }
                    true
                }
                R.id.mail_item -> {
                    true
                }
                else -> false
            }
        }
        drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle?.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle!!.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }


}