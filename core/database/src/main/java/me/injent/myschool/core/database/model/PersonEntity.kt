package me.injent.myschool.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import me.injent.myschool.core.database.util.LocalDateConverter
import me.injent.myschool.core.model.Person

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val id: Long,
    val shortName: String,
    val locale: String,
    val birthday: LocalDate? = null,
    val sex: String,
    val roles: List<String>,
    val phone: String? = null
)

fun PersonEntity.asExternalModel() = Person(
    id = id,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)
