package me.injent.myschool.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import me.injent.myschool.core.database.util.LocalDateConverter
import me.injent.myschool.core.model.Person
import me.injent.myschool.core.model.Sex

@Entity(tableName = "persons")
data class PersonEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "person_id")
    val personId: Long,
    @ColumnInfo(name = "short_name")
    val shortName: String,
    val locale: String,
    val birthday: LocalDate? = null,
    val sex: Sex,
    val roles: List<String>,
    val phone: String? = null
)

fun PersonEntity.asExternalModel() = Person(
    id = id,
    personId = personId,
    shortName = shortName,
    locale = locale,
    birthday = birthday,
    sex = sex,
    roles = roles,
    phone = phone
)

data class PersonIdAndName(
    @ColumnInfo(name = "person_id")
    val id: Long,
    @ColumnInfo(name = "short_name")
    val name: String
)