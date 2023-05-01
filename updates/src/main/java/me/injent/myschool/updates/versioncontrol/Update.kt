package me.injent.myschool.updates.versioncontrol

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Update(
    @PropertyName("versionName") val versionName: String = "",
    @PropertyName("content") val content: String = "",
    @PropertyName("url") val url: String = "",
    @PropertyName("date") val date: Timestamp = Timestamp(0, 0),
    @PropertyName("required") val required: Boolean = false
)