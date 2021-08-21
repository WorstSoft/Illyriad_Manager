package com.worstsoft.illyriadmanager

interface Callbacks {
    interface NotificationCallback {
        fun execute(apiKey: String, callback: NotificationDialogCallback): Unit
    }
    interface NotificationDialogCallback {
        fun onComplete(): Unit
        fun onFailure(): Unit
    }
}