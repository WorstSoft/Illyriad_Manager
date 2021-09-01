package com.worstsoft.illyriadmanager.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.worstsoft.illyriadmanager.Callbacks
import com.worstsoft.illyriadmanager.R
import com.worstsoft.illyriadmanager.dialogs.InsertDataDialog
import com.worstsoft.illyriadmanager.util.NotificationAdapter
import com.worstsoft.illyriadmanager.util.XMLParser
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private val TAG: String = "MainActivity"
    private var timeout: Boolean = false
    private var swipe: SwipeRefreshLayout? = null
    private var apiKey: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipe = view.findViewById(R.id.notification_refresh_layout)

        val prefs: SharedPreferences = requireActivity().getSharedPreferences("apiKey",
            AppCompatActivity.MODE_PRIVATE
        )
        if (prefs.getString("key", null) == null || prefs.getString("key", "").equals("")) {
            val dialog: InsertDataDialog = InsertDataDialog(object : Callbacks.NotificationCallback {
                override fun execute(apiKey: String, callback: Callbacks.NotificationDialogCallback): Unit {
                    this@NotificationFragment.apiKey = apiKey
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

            dialog.show(requireActivity().supportFragmentManager,"dialog")
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
        requireActivity().runOnUiThread {
            val recyclerView = requireView().findViewById<RecyclerView>(R.id.notification_recycler)
            if (recyclerView.adapter == null) recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            recyclerView.adapter = adapter
            if (swipe!!.isRefreshing) swipe!!.isRefreshing = false
        }
    }
}