package capstone.project.mushymatch.view.scan.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import capstone.project.mushymatch.databinding.ActivityHomePageBinding
import capstone.project.mushymatch.view.scan.DetectionActivity

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnScan.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
        }

    }
}