package shachar.afeka.course.volunteers.utilities

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.tasks.await
import shachar.afeka.course.volunteers.models.User
import java.lang.IllegalStateException
import java.util.Date

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
        val result = getUserDBRecord(uid)

        return if (result == null || !result.exists())
            null
        else
            User.Builder()
                .id(result.id)
                .phone(result.getString("phone"))
                .email(result.getString("email"))
                .residence(result.getString("residence"))
                .name(result.getString("name"))
                .createdAt(result.getTimestamp("createdAt")?.toDate())
                .updatedAt(result.getTimestamp("updatedAt")?.toDate())
                .build()
    }

    suspend fun userExist(uid: String): Boolean {
        val dbRecord = getUserDBRecord(uid)
        return dbRecord != null && dbRecord.exists()
    }

    suspend fun addUser(
        id: String,
        name: String?,
        email: String?,
        phone: String?,
    ) {
        val now = Date()

        val docData = hashMapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "residence" to null,
            "createdAt" to now,
            "updatedAt" to now
        )

        db.collection(Constants.Models.USERS).document(id).set(docData).await()
    }

    suspend fun updateUser(
        id: String,
        name: String,
        email: String,
        phone: String,
        residence: String
    ) {
        val docData = mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "residence" to residence,
            "updatedAt" to Date()
        )
        db.collection(Constants.Models.USERS).document(id).update(docData).await()
    }

    private suspend fun getUserDBRecord(uid: String): DocumentSnapshot? {
        return db.collection(Constants.Models.USERS)
            .document(uid)
            .get().await()
    }

    suspend fun addOrganization(
        name: String,
        about: String,
        lat: Double,
        lon: Double,
        ownerId: String
    ) {
        val now = Date()

        val docData = hashMapOf(
            "name" to name,
            "about" to about,
            "headquarters" to GeoPoint(lat, lon),
            "ownerId" to ownerId,
            "createdAt" to now,
            "updatedAt" to now
        )

        db.collection(Constants.Models.ORGANIZATIONS).document().set(docData).await()
    }
}