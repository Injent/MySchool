package me.injent.myschool.updates.versioncontrol

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import me.injent.myschool.core.data.repository.UserDataRepository
import me.injent.myschool.updates.installer.updateApkFile
import java.io.File
import javax.inject.Inject

private const val VersionCollection = "versions"

class FirebaseVersionController @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext context: Context
) : VersionController {

    private val updateFile: File = context.updateApkFile

    private val currentVersionCode = if (Build.VERSION.SDK_INT >= 33) {
        context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.PackageInfoFlags.of(0)
        ).longVersionCode
    } else {
        @Suppress("DEPRECATION")
        context.packageManager.getPackageInfo(context.packageName, 0).versionCode.toLong()
    }

    override suspend fun getUpdate(): Update? = try {
        firestore
            .collection(VersionCollection)
            .document("current")
            .get().await().toObject(Update::class.java)
            ?.takeIf { update ->
                update.versionCode > currentVersionCode
            }?.apply {
                isIgnored = hasNonInstalledUpdate()
            }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    override suspend fun hasNonInstalledUpdate(): Boolean {
        return updateFile.exists()
    }
}