package shachar.afeka.course.volunteers.utilities

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import shachar.afeka.course.volunteers.models.User
import java.lang.IllegalStateException

class DBClient private constructor() {
    private val db = Firebase.firestore

    companion object {
        @Volatile
        private var instance: DBClient? = null

        fun init(): DBClient {
            return instance ?: synchronized(this) {
                instance ?: DBClient().also { instance = it }
            }
        }

        fun getInstance(): DBClient {
            return instance ?: throw IllegalStateException(
                "DBClient must be initialized by calling init() before use."
            )
        }
    }

    suspend fun getUserByUID(uid: String): User? {
        val result = db.collection("users")
            .document(uid)
            .get().await()

        return if (!result.exists())
            null
        else
            User.Builder()
                .phone(result.getString("phone"))
                .email(result.getString("email"))
                .residence(result.getString("residence"))
                .name(result.getString("name"))
                .build()
    }
}