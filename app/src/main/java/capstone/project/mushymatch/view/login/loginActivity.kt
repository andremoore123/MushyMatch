package capstone.project.mushymatch.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import capstone.project.mushymatch.R
import capstone.project.mushymatch.databinding.ActivityLoginBinding
import capstone.project.mushymatch.view.register.RegisterActivity

class loginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var forgot: TextView
    private lateinit var btnLogin: Button
    private lateinit var bind: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bind = ActivityLoginBinding.inflate(layoutInflater)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        forgot = findViewById(R.id.textView5)
        btnLogin = findViewById(R.id.btnLogin)

        forgot.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val usernameText = username.text.toString()
            val passwordText = password.text.toString()

            if (TextUtils.isEmpty(usernameText)) {
                username.error = "Silahkan Isi Username Anda"
                username.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(passwordText)) {
                password.error = "Silahkan Isi Password Anda"
                password.requestFocus()
                return@setOnClickListener
            }
        }
    }
//    private fun loadMainActivity() {
//        val intent = Intent(applicationContext, MainActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//    }
}