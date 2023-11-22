package capstone.project.mushymatch.api.repository

import android.content.Intent
import android.widget.Toast
import capstone.project.mushymatch.view.home.HomePageActivity
import com.google.firebase.auth.FirebaseAuth


/*
   Created by Andre Eka Putra on 22/11/23
   andremoore431@gmail.com
*/

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun login(username: String, password: String, callback: (AuthResponse) -> Unit) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        callback(AuthResponse(isError = false, message = "Selamat Datang"))
                    } else {
                        // Handle the case when the user is null
                        callback(AuthResponse(isError = true, message = "User is null"))
                    }
                } else {
                    callback(AuthResponse(isError = true, message = "Email atau Password Salah"))
                }
            }
    }
}