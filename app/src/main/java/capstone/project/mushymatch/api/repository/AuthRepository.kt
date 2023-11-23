package capstone.project.mushymatch.api.repository

import android.content.Intent
import android.widget.Toast
import capstone.project.mushymatch.view.home.HomePageActivity
import capstone.project.mushymatch.view.login.loginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await


/*
   Created by Andre Eka Putra on 22/11/23
   andremoore431@gmail.com
*/

class AuthRepository : IAuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override suspend fun login(username: String, password: String): Result<AuthResponse> = try {
        val authResult = auth.signInWithEmailAndPassword(username, password).await()
        val user = authResult.user

        if (user != null) {
            Result.Success(AuthResponse(isError = false, message = "Selamat Datang"))
        } else {
            Result.Error("User is null")
        }
    } catch (e: Exception) {
        Result.Error("Email atau Password Salah")
    }

    override suspend fun register(email: String, password: String): Result<AuthResponse> = try {
        auth.createUserWithEmailAndPassword(email, password).await()
        Result.Success(AuthResponse(isError = false, message = "Pendaftaran Berhasil"))
    } catch (e: Exception) {
        val errorMessage = e.message ?: "Unknown error"
        Result.Error(message = errorMessage)
    }
}
interface IAuthRepository {
    suspend fun login(username: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, password: String): Result<AuthResponse>}