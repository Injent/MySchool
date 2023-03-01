package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.ShortUserInfo

@Serializable
data class NetworkShortUserInfo(
    val userId: Long,
    val shortName: String,
    val sex: String
)

fun NetworkShortUserInfo.asExternalModel() = ShortUserInfo(
    userId = userId,
    shortName = shortName,
    sex = sex
)