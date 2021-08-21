package com.worstsoft.illyriadmanager

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private var timeout: Boolean = false
    private var swipe: SwipeRefreshLayout? = null
    private var apiKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        swipe = findViewById(R.id.refresh_layout)

        fetchDataNotification()

        swipe!!.setOnRefreshListener {
            Log.d(TAG, "Fetching")
            fetchDataNotification()
        }

    }

    fun fetchDataNotification() {
        GlobalScope.launch(Dispatchers.Default) {
            val data = XMLParser.getNotification("AQAAADvEkSqrWIL5ytsPVlqQslWqCHBLAZhJJHRdkCBRe2sQ6oxn2MDsxnQ0egbx05xobqJYL73kF-qc5z-A0y7Aex4=")
            val adapter = NotificationAdapter(data.reversed())
            runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.recycler)
                if (recyclerView.adapter == null) recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter
                if (swipe!!.isRefreshing) swipe!!.isRefreshing = false
            }
        }
    }
}