package capstone.project.mushymatch.view.scan.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import capstone.project.mushymatch.databinding.ActivityHomePageBinding

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}