package capstone.project.mushymatch.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import capstone.project.mushymatch.databinding.ActivityRegisterBinding
import capstone.project.mushymatch.view.login.loginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnregister.setOnClickListener{
            val nama = binding.username.text.toString()
            val emailText = binding.email.text.toString()
            val passwordText = binding.password.text.toString()

            if (nama.isEmpty()) {
                binding.username.error = "Silahkan Isi Username Anda"
                binding.username.requestFocus()
                return@setOnClickListener
            }

            if (emailText.isEmpty()) {
                binding.email.error = "Silahkan Isi Email Anda"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                binding.email.error = "Email Tidak Valid"
                binding.email.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.isEmpty()) {
                binding.password.error = "Silahkan Isi Password Anda"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            if (passwordText.length < 6) {
                binding.password.error = "Password Minimal 6 Karakter"
                binding.password.requestFocus()
                return@setOnClickListener
            }

            registerFirebase(emailText, passwordText)
        }

        binding.btnMasuk.setOnClickListener {
            val login = Intent(this, loginActivity::class.java)
            startActivity(login)
        }
    }

    private fun registerFirebase(emailText: String, passwordText: String) {
        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Pendaftaran Berhasil", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, loginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}