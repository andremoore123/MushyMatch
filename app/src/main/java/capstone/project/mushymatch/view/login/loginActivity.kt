package capstone.project.mushymatch.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import capstone.project.mushymatch.databinding.ActivityLoginBinding
import capstone.project.mushymatch.view.register.RegisterActivity
import capstone.project.mushymatch.view.home.HomePageActivity
import com.google.firebase.auth.FirebaseAuth

class loginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username.isEmpty()) {
                binding.username.error = "Silahkan Isi Username Anda"
                binding.username.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.password.error = "Silahkan Isi Password Anda"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            loginFirebase(username, password)
        }

        binding.textRegister.setOnClickListener {
            val register = Intent(this, RegisterActivity::class.java)
            startActivity(register)
        }
    }

    private fun loginFirebase(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        if (user != null) {
                            Toast.makeText(this, "Selamat Datang ", Toast.LENGTH_LONG).show()
                        }
                        val intent = Intent(this, HomePageActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Email atau Password Salah", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }
}
