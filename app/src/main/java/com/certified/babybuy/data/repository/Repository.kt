package com.certified.babybuy.data.repository

import com.certified.babybuy.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class Repository @Inject constructor() {

    val firestore = Firebase.firestore
    val auth = Firebase.auth

    fun createUserWithEmailAndPassword(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)

    fun uploadDetails(user: User) =
        firestore.collection("_users").document(user.uid).set(user)

    fun signInWithCredential(firebaseCredential: AuthCredential) =
        auth.signInWithCredential(firebaseCredential)

    fun signInWithEmailAndPassword(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)

    fun sendPasswordResetEmail(email: String) = Firebase.auth.sendPasswordResetEmail(email)

    fun getCategories(uid: String) =
        firestore.collection("_categories").whereEqualTo("uid", uid)
            .orderBy("modified", Query.Direction.DESCENDING)

    fun getItems(uid: String) = firestore.collection("_items").whereEqualTo("uid", uid)
        .orderBy("modified", Query.Direction.DESCENDING)

    fun deleteItem(id: String) = firestore.collection("_items").document(id).delete()
}