package com.worstsoft.illyriadmanager.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.worstsoft.illyriadmanager.Callbacks
import com.worstsoft.illyriadmanager.R
import java.lang.IllegalStateException

class InsertDataDialog(val callback: Callbacks.NotificationCallback): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.notification_dialog_box, null)
            view.findViewById<Button>(R.id.notification_dialog_submit).setOnClickListener {
                view.visibility = View.INVISIBLE
                callback.execute(view.findViewById<EditText>(R.id.notification_dialog_input).text.toString(),
                    object : Callbacks.NotificationDialogCallback {
                        override fun onComplete() {
                            dismiss()
                            val pref: SharedPreferences = requireActivity().getSharedPreferences("apiKey", MODE_PRIVATE)
                            val editor: SharedPreferences.Editor = pref.edit()

                            editor.putString("key",view.findViewById<EditText>(R.id.notification_dialog_input).text.toString())
                            editor.apply()
                        }

                        override fun onFailure() {
                            dismiss()
                            val dialog = InsertDataDialog(callback)
                            dialog.show(requireActivity().supportFragmentManager,"dialog")
                        }

                    })
            }
            builder.setView(view)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null!")
    }

}