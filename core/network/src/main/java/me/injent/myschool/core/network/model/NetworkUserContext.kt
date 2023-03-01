package me.injent.myschool.core.network.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.UserContext
import me.injent.myschool.core.network.EduGroupSerializer
import me.injent.myschool.core.network.SchoolSerializer

@Serializable
data class NetworkUserContext(
    val userId: Long,
    val personId: Long,
    val shortName: String,
    val roles: List<String>,
    @Serializable(with = SchoolSerializer::class)
    val schools: List<NetworkSchool>,
    @Serializable(with = EduGroupSerializer::class)
    val eduGroups: List<NetworkEduGroup>
)

fun NetworkUserContext.asExternalModel() = UserContext(
    userId,
    personId,
    shortName,
    schools.first().asExternalModel(),
    eduGroups.first().asExternalModel()
)