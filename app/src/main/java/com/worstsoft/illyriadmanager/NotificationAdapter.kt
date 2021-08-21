package com.worstsoft.illyriadmanager

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val data: List<NotificationModel>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder =
        NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notification_layout, parent, false))

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.image.setImageResource(
            when (data[position].notificationOverallType) {
                NotificationModel.NotificationOverallType.TOWN -> R.drawable.notif_one
                NotificationModel.NotificationOverallType.RESEARCH -> R.drawable.notif_two
                NotificationModel.NotificationOverallType.TRADE -> R.drawable.notif_three
                NotificationModel.NotificationOverallType.WEAPON -> R.drawable.notif_four
                NotificationModel.NotificationOverallType.DIPLOMATIC -> R.drawable.notif_five
                NotificationModel.NotificationOverallType.ALLIANCE -> R.drawable.notif_six
                NotificationModel.NotificationOverallType.MAGIC -> R.drawable.notif_eight
                NotificationModel.NotificationOverallType.INVALID -> R.drawable.illyriad_manager
            }
        )
        holder.detail.text = data[position].notificationDetail
        holder.type.text = when (data[position].notificationType) {
            NotificationModel.NotificationType.BUILD_AND_CONSTRUCT_DONE -> "Construction Done"
            NotificationModel.NotificationType.RESEARCH_DONE -> "Research Done"
            NotificationModel.NotificationType.CARAVAN_OUT -> "Caravan/Cotter Going Out"
            NotificationModel.NotificationType.TRADE_CARAVAN_IN -> "Trade Caravan Going In"
            NotificationModel.NotificationType.HARVEST_CARAVAN_IN -> "Harvest Caravan Going In"
            NotificationModel.NotificationType.HARVEST_CARAVAN_START -> "Harvest Caravan/Cotter Start Harvesting"
            NotificationModel.NotificationType.INVALID_NOTIFICATION -> "ERROR : TYPE NOT FOUND"
        }
    }

    override fun getItemCount(): Int = data.size

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.notification_icon)
        val detail: TextView = itemView.findViewById(R.id.notification_detail)
        val type: TextView = itemView.findViewById(R.id.notification_type)
    }
}