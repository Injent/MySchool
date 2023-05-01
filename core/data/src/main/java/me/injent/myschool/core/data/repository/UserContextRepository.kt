package me.injent.myschool.core.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.injent.myschool.core.datastore.MsPreferencesDataSource
import me.injent.myschool.core.model.UserContext
import javax.inject.Inject

interface UserContextRepository {
    val userContext: Flow<UserContext?>
    suspend fun setUserContext(userContext: UserContext)
}

class OfflineFirstUserContextRepository @Inject constructor(
    private val userDataSource: MsPreferencesDataSource,
    private val firestore: FirebaseFirestore
) : UserContextRepository {

    override val userContext: Flow<UserContext?>
        get() = userDataSource.userData.map { it.userContext }

    override suspend fun setUserContext(userContext: UserContext) {
        userDataSource.setUserContext(userContext)

        val firebaseUserData = hashMapOf(
            "registrationDate" to Timestamp.now(),
            "sex" to userContext.sex,
            "class" to userContext.group.name
        )
        val documentId = userContext.userId.toString()
        val userDocument = firestore.collection("users").document(documentId)
        userDocument.get().addOnCompleteListener {
            if (!it.result.exists()) {
                userDocument.set(firebaseUserData)
            }
        }
    }
}