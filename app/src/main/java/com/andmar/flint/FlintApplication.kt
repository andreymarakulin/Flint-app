package com.andmar.flint

import android.app.Application
import com.andmar.flint.data.DefaultFlintRepository
import com.andmar.flint.data.FlintRepository
import com.andmar.flint.firebase.AuthClient
import com.andmar.flint.firebase.DefaultAuthClient
import com.andmar.flint.firebase.DefaultFirestoreClient
import com.andmar.flint.firebase.FirestoreClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class FlintApplication: Application() {

    lateinit var authClient: DefaultAuthClient
    lateinit var firestoreClient: DefaultFirestoreClient
    lateinit var flintRepository: DefaultFlintRepository

    override fun onCreate() {
        super.onCreate()

        authClient = AuthClient(Firebase.auth)
        firestoreClient = FirestoreClient(Firebase.firestore, authClient)
        flintRepository = FlintRepository(authClient, firestoreClient)
    }
}