package me.injent.myschool.updates.versioncontrol

/**
 * Uses for checking existed localy and remotely updates
 */
interface VersionController {
    /**
     * Gets most actual update if current version code of app equals or higher than actual then
     * returns null
     */
    suspend fun getUpdate(): Update?

    /**
     * Checks if update apk file is existing on local device and returns true if yes
     */
    suspend fun hasNonInstalledUpdate(): Boolean
}