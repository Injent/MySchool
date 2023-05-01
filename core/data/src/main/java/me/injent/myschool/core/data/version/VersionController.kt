package me.injent.myschool.core.data.version

interface VersionController {
    suspend fun getUpdate(): Update?
    suspend fun installUpdate(update: Update)
}