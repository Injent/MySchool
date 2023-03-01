package me.injent.myschool.core.model.datastore

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
    val schoolId: Int,
    val schoolName: String,
    val schoolType: String,
    val eduGroupId: String,
    val eduGroupName: String,
    val timetable: Long,
    val studyYear: Int
)

fun UserContext.toSaveableModel(): SaveableUserContext {
    return SaveableUserContext(
        userId = userId,
        personId = personId,
        shortName = shortName,
        schoolId = school.id,
        schoolName = school.name,
        schoolType = school.type,
        eduGroupId = eduGroup.id,
        eduGroupName = eduGroup.name,
        timetable = eduGroup.timetable,
        studyYear = eduGroup.studyYear
    )
}

fun SaveableUserContext.toDomainModel(): UserContext {
    return UserContext(
        userId = userId,
        personId = personId,
        shortName = shortName,
        school = School(schoolId, schoolName,schoolType),
        eduGroup = EduGroup(eduGroupId, eduGroupName, timetable, studyYear)
    )
}