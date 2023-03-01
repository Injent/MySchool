package me.injent.myschool.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "people")
data class PersonEntity(
    @PrimaryKey
    val id: Long,
    val shortName: String,
    val locale: String,
    val birthday: Instant? = null,
    val sex: String,
    val roles: List<String>,
    val phone: String? = null
)
