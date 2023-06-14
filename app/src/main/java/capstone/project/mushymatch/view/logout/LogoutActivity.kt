package capstone.project.mushymatch.view.logout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import capstone.project.mushymatch.databinding.ActivityLogoutBinding
import capstone.project.mushymatch.view.login.loginActivity
import com.google.firebase.auth.FirebaseAuth

class LogoutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLogoutBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Di dalam onCreate() atau metode lain yang sesuai di ProfilActivity
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            binding.emailText.text = email
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, loginActivity::class.java)
        startActivity(intent)
        finish() // Close the profile activity to prevent going back to it using the back button
    }
}
