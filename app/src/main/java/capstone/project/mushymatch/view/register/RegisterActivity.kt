package capstone.project.mushymatch.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import capstone.project.mushymatch.R
import capstone.project.mushymatch.databinding.ActivityRegisterBinding
import capstone.project.mushymatch.view.login.loginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var bind: ActivityRegisterBinding
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText
    private lateinit var forgot: TextView
    private lateinit var btnRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        bind = ActivityRegisterBinding.inflate(layoutInflater)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        email = findViewById(R.id.email)
        forgot = findViewById(R.id.btnMasuk)
        btnRegister = findViewById(R.id.btnregister)

        forgot.setOnClickListener{
            intent = Intent(applicationContext, loginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener{
            val namatext = username.text.toString()
            val emailtext = email.text.toString()
            val passwordtext = password.text.toString()

            if (TextUtils.isEmpty(namatext)) {
                username.error = "Silahkan Isi Username Anda"
                username.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(emailtext)) {
                email.error = "Silahkan Isi Email Anda"
                email.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(passwordtext)) {
                password.error = "Silahkan Isi Password Anda"
                password.requestFocus()
                return@setOnClickListener
            }
        }
    }
}