package com.worstsoft.illyriadmanager

data class NotificationModel(val notificationId: Int,
                        val notificationType: NotificationType,
                        val notificationOverallType: NotificationOverallType,
                        val notificationTownId: Int,
                        val notificationDetail: String = "Error : Notification Not Found",
                        val notificationDateAndTime: String = "") {

    enum class NotificationType(val ID: Int) {
        BUILD_AND_CONSTRUCT_DONE(2),
        RESEARCH_DONE(4),
        CARAVAN_OUT(12),
        TRADE_CARAVAN_IN(13),
        HARVEST_CARAVAN_IN(36),
        HARVEST_CARAVAN_START(48),
        INVALID_NOTIFICATION(1000);

        companion object {
            fun enumFromInt(id: Int): NotificationType {
                return when (id) {
                    2 -> BUILD_AND_CONSTRUCT_DONE
                    4 -> RESEARCH_DONE
                    12 -> CARAVAN_OUT
                    13 -> TRADE_CARAVAN_IN
                    36 -> HARVEST_CARAVAN_IN
                    48 -> HARVEST_CARAVAN_START
                    else -> INVALID_NOTIFICATION
                }
            }
        }
    }

    enum class NotificationOverallType(val ID: Int) {
        TOWN(1),
        RESEARCH(2),
        TRADE(3),
        WEAPON(4),
        DIPLOMATIC(5),
        ALLIANCE(6),
        MAGIC(8),
        INVALID(1000);

        companion object {
            fun enumFromInt(id: Int): NotificationOverallType {
                return when (id) {
                    1 -> TOWN
                    2 -> RESEARCH
                    3 -> TRADE
                    4 -> WEAPON
                    5 -> DIPLOMATIC
                    6 -> ALLIANCE
                    8 -> MAGIC
                    else -> INVALID
                }
            }
        }
    }
}