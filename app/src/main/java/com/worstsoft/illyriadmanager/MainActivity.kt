package com.worstsoft.illyriadmanager

import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.worstsoft.illyriadmanager.dialogs.InsertDataDialog
import com.worstsoft.illyriadmanager.util.NotificationAdapter
import com.worstsoft.illyriadmanager.util.XMLParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private var timeout: Boolean = false
    private var swipe: SwipeRefreshLayout? = null
    private var apiKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe = findViewById(R.id.refresh_layout)

        val prefs: SharedPreferences = getSharedPreferences("apiKey", MODE_PRIVATE)
        if (prefs.getString("key", null) == null || prefs.getString("key", "").equals("")) {
            val dialog: InsertDataDialog = InsertDataDialog(object : Callbacks.NotificationCallback {
                override fun execute(apiKey: String, callback: Callbacks.NotificationDialogCallback): Unit {
                    this@MainActivity.apiKey = apiKey
                    GlobalScope.launch {
                        try {
                            fetchDataNotification()
                            swipe!!.setOnRefreshListener {
                                Log.d(TAG, "Fetching")
                                GlobalScope.launch {
                                    fetchDataNotification()
                                }
                            }
                            callback.onComplete()
                        } catch (err: NoSuchFieldException) {
                            swipe!!.setOnRefreshListener(null)
                            callback.onFailure()
                        }
                    }
                }
            })

            dialog.show(supportFragmentManager,"dialog")
        } else {
            apiKey = prefs.getString("key", null)!!
            GlobalScope.launch {
                fetchDataNotification()
                swipe!!.setOnRefreshListener {
                    Log.d(TAG, "Fetching")
                    GlobalScope.launch {
                        fetchDataNotification()
                    }
                }
            }
        }


    }

    suspend fun fetchDataNotification() {
        val data = XMLParser.getNotification(apiKey)
        val adapter = NotificationAdapter(data.reversed())
        runOnUiThread {
            val recyclerView = findViewById<RecyclerView>(R.id.recycler)
            if (recyclerView.adapter == null) recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = adapter
            if (swipe!!.isRefreshing) swipe!!.isRefreshing = false
        }
    }
}