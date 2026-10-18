package com.example.data

import android.content.Context
import com.example.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class LionDenRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeCareers(userId: String): Flow<List<StudentCareer>> = callbackFlow {
        val path = "users/$userId/careers"
        val registration = db.collection("users").document(userId).collection("careers")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(StudentCareer::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    }.sortedByDescending { it.updatedAt?.seconds ?: it.createdAt?.seconds ?: 0L }
                    trySend(items)
                }
            }
        awaitClose { registration.remove() }
    }

    fun observeHallOfFame(userId: String): Flow<List<AlumniRecord>> = callbackFlow {
        val path = "users/$userId/hall_of_fame"
        val registration = db.collection("users").document(userId).collection("hall_of_fame")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, path)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val items = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(AlumniRecord::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                    }.sortedByDescending { it.finalCgpa }
                    trySend(items)
                }
            }
        awaitClose { registration.remove() }
    }

    suspend fun createCareer(career: StudentCareer): Result<String> {
        val uid = requireUserId()
        val collectionRef = db.collection("users").document(uid).collection("careers")
        val docRef = if (career.id.isNotBlank()) collectionRef.document(career.id) else collectionRef.document()
        val payload = career.copy(
            id = docRef.id,
            userId = uid,
            inventory = career.inventory.take(25),
            relationships = career.relationships.take(25),
            recentLogs = career.recentLogs.take(30),
            badges = career.badges.take(25),
            createdAt = null,
            updatedAt = null
        )
        return try {
            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun updateCareer(career: StudentCareer): Result<Unit> {
        val uid = requireUserId()
        require(career.id.isNotBlank()) { "Career ID must not be blank for update" }
        val docRef = db.collection("users").document(uid).collection("careers").document(career.id)
        return try {
            val existingCreatedAt = career.createdAt ?: docRef.get().await().getTimestamp("createdAt")
            requireNotNull(existingCreatedAt) { "Career createdAt must not be null for update" }
            val updatedPayload = career.copy(
                userId = uid,
                inventory = career.inventory.take(25),
                relationships = career.relationships.take(25),
                recentLogs = career.recentLogs.take(30),
                badges = career.badges.take(25),
                createdAt = existingCreatedAt,
                updatedAt = null
            )
            docRef.set(updatedPayload).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun getCareerById(ownerUid: String, careerId: String): Result<StudentCareer?> {
        val docRef = db.collection("users").document(ownerUid).collection("careers").document(careerId)
        return try {
            val snap = docRef.get().await()
            Result.success(snap.toObject(StudentCareer::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.GET, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun deleteCareer(careerId: String): Result<Unit> {
        val uid = requireUserId()
        val docRef = db.collection("users").document(uid).collection("careers").document(careerId)
        return try {
            docRef.delete().await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.DELETE, docRef.path)
            Result.failure(e)
        }
    }

    suspend fun addAlumniRecord(record: AlumniRecord): Result<String> {
        val uid = requireUserId()
        val collectionRef = db.collection("users").document(uid).collection("hall_of_fame")
        val docRef = if (record.id.isNotBlank()) collectionRef.document(record.id) else collectionRef.document()
        val payload = record.copy(
            id = docRef.id,
            userId = uid,
            createdAt = null,
            updatedAt = null
        )
        return try {
            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, docRef.path)
            Result.failure(e)
        }
    }
}
