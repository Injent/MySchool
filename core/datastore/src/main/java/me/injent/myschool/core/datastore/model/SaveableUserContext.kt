package me.injent.myschool.core.datastore.model

import kotlinx.serialization.Serializable
import me.injent.myschool.core.model.EduGroup
import me.injent.myschool.core.model.School
import me.injent.myschool.core.model.UserContext

/**
 * The simplified model of [UserContext] which can be saved into DataStore
 */
@Serializable
data class SaveableUserContext(
    val userId: Long,
    val personId: Long,
    val shortName: String,
    val school: SaveableSchool,
    val eduGroup: SaveableEduGroup
)

fun SaveableUserContext.asExternalModel() = UserContext(
    userId = userId,
    personId = personId,
    shortName = shortName,
    school = school.asExternalModel(),
    eduGroup = eduGroup.asExternalModel()
)