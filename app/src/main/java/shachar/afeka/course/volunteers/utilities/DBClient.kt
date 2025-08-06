package shachar.afeka.course.volunteers.utilities

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
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

    fun getUserByUID(uid: String): Task<DocumentSnapshot?> {
        return db.collection("users")
            .document(uid)
            .get()
    }
}