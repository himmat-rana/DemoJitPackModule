package com.example.mylibrary.data.network.pubsub

import android.content.Context
import com.example.mylibrary.data.db.AppDatabase
import com.example.mylibrary.model.Agent
import com.example.mylibrary.model.ChatMessage
import com.example.mylibrary.utils.parseJsonValue
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PubSubChatMessageDto {
    lateinit var messageId: String
    lateinit var sessionId: String
    lateinit var companyId: String
    lateinit var mimeType: String
    lateinit var message: String
    lateinit var extraMessageData: Map<String, Any?>
    lateinit var messageTime: String
    lateinit var sender: String
    lateinit var senderType: String
    lateinit var createdAt: String
    lateinit var updatedAt: String
}

fun listenForUserNewMessageEvents(userId:String, context: Context) {
    val pubsub:PubSub = PubSub.getPubsub()
    val topic = "io.supportgenie.session.message.user.$userId"
    println("listening to topic $topic")
    pubsub.registerListener(topic) {
        println("got user new message event for ${userId}")
        if (it != null) {
            val dto: PubSubChatMessageDto? = parseJsonValue<PubSubChatMessageDto>(it, PubSubChatMessageDto::class.java)

            if (dto != null) {
                val db = AppDatabase.getDatabase(context)
                val dao = db.chatMessageDao()
                dao.insert(ChatMessage(dto))
            }
        }
    }
}
