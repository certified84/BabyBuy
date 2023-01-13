package com.certified.babybuy.data.repository

import com.certified.babybuy.data.model.User
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class Repository @Inject constructor() {

    fun createUserWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.createUserWithEmailAndPassword(email, password)

    fun uploadDetails(user: User) =
        Firebase.firestore.collection("_users").document(user.uid).set(user)

    fun signInWithCredential(firebaseCredential: AuthCredential) =
        Firebase.auth.signInWithCredential(firebaseCredential)

    fun signInWithEmailAndPassword(email: String, password: String) =
        Firebase.auth.signInWithEmailAndPassword(email, password)

    fun sendPasswordResetEmail(email: String) = Firebase.auth.sendPasswordResetEmail(email)

    fun getCategories(uid: String) =
        Firebase.firestore.collection("_categories").whereEqualTo("uid", uid)
            .orderBy("modified", Query.Direction.DESCENDING)

    fun getItems(uid: String) = Firebase.firestore.collection("_items").whereEqualTo("uid", uid)
        .orderBy("modified", Query.Direction.DESCENDING)
}