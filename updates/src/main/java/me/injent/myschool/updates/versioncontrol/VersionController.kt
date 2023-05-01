package me.injent.myschool.updates.versioncontrol

interface VersionController {
    suspend fun getUpdate(): Update?
    suspend fun hasNonInstalledUpdate(): Boolean
}