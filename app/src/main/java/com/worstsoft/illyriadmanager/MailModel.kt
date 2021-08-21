package com.worstsoft.illyriadmanager

data class MailModel(val messageId: Int,
                val category: MessageCategory = MessageCategory.SYSTEM,
                val received: Boolean = true,
                val sentByPlayerId: Int = 0,
                val sentByPlayerName: String = "System",
                val canBeReplied: Boolean = false,
                val readByReceiver: Boolean = false,
                val isAlliance: Boolean = false,
                val messageSubject: String = "Error : Subject Not Found",
                val mailSentAtTime: String = "Error : Time Not Found") {


    enum class MessageCategory(val id: Int) {
        SYSTEM(8),
        PLAYER(1),
        TRADE(2)
    }
}