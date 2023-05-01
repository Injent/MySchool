package me.injent.myschool.updates.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.injent.myschool.updates.versioncontrol.FirebaseVersionController
import me.injent.myschool.updates.versioncontrol.VersionController

@Module
@InstallIn(SingletonComponent::class)
interface UpdatesModule {
    @Binds
    fun bindsVersionController(
        firebaseVersionController: FirebaseVersionController
    ): VersionController

    companion object {
        @Provides
        fun providesFirestore() = Firebase.firestore
    }
}