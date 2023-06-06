package capstone.project.mushymatch.view.scan.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.mushymatch.api.ApiConfig
import capstone.project.mushymatch.api.ApiService
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.api.response.mushroom.GetMushroomResponseItem

import capstone.project.mushymatch.databinding.ActivityHomePageBinding
import capstone.project.mushymatch.view.about.MushroomInformationActivity
import capstone.project.mushymatch.view.scan.DetectionActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePageActivity : AppCompatActivity(), MushroomAdapter.OnMushroomClickListener {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var mushroomAdapter: MushroomAdapter
    private lateinit var mushroomViewModel: MushroomViewModel
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi MushroomViewModel
        val repository = MushroomRepository(ApiConfig.createApiService())
        mushroomViewModel = ViewModelProvider(this, MushroomViewModelFactory(repository))[MushroomViewModel::class.java]

        // Inisialisasi RecyclerView
        mushroomAdapter = MushroomAdapter()

        // Panggil fungsi loadMushrooms() pada MushroomViewModel
        mushroomViewModel.loadMushrooms()
        binding.rvMushrooms.layoutManager = LinearLayoutManager(this)
        binding.rvMushrooms.adapter = mushroomAdapter

        startShimmer()
        // Amati perubahan pada LiveData mushrooms
        coroutineScope.launch {
            delay(2000)
            mushroomViewModel.mushrooms.observe(this@HomePageActivity) { mushrooms ->
                mushroomAdapter.setMushrooms(mushrooms)
                stopShimmer()
            }
        }

        mushroomAdapter.setOnMushroomClickListener(this)
        
        binding.btnScan.setOnClickListener {
            val intent = Intent(this, DetectionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startShimmer() {
        binding.shimmerViewContainer.startShimmer()
        binding.rvMushrooms.visibility = android.view.View.GONE
    }

    //stop shimmer
    private fun stopShimmer() {
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = android.view.View.GONE
        binding.rvMushrooms.visibility = android.view.View.VISIBLE
    }

    override fun onMushroomClick(mushroom: GetMushroomResponseItem) {
        val intent = Intent(this, MushroomInformationActivity::class.java)
        intent.putExtra("label", mushroom.id) // Mengirimkan ID jamur ke MushroomInformationActivity
        startActivity(intent)
    }

}
