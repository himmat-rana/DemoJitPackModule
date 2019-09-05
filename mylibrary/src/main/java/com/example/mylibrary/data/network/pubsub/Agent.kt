package com.example.mylibrary.data.network.pubsub

import android.content.Context
import com.example.mylibrary.data.db.AppDatabase
import com.example.mylibrary.model.Agent
import com.example.mylibrary.utils.parseJsonValue
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UserStatusDto {
    lateinit var userId: String
    lateinit var companyId: String
    var isAvailable: Boolean = false
}

fun listenForAgentEvents(companyId:String, context: Context) {
    val pubsub:PubSub = PubSub.getPubsub()
    pubsub.registerListener("io.supportgenie.agent.$companyId") {
        if (it != null) {
            val userStatusDto: UserStatusDto? = parseJsonValue<UserStatusDto>(it, UserStatusDto::class.java)

            if (userStatusDto != null) {
                val db = AppDatabase.getDatabase(context)
                val agentDao = db.agentDao()
                agentDao.updateAvailable(userStatusDto.isAvailable, userStatusDto.userId)
            }
        }
    }
}



/*
fun testJsonParse() {
    val data:Map<String, Any?> = mapOf<String, Any?>(
        "userId" to "1234",
        "isAvailable" to true
    )
    val userStatusDto:UserStatusDto? = parseJsonValue<UserStatusDto>(data, UserStatusDto::class.java)
    userStatusDto?.let {
        println("userId ")
        println(it.userId)
        println("isAvailable")
        println(it.isAvailable)
    }
}

*/

