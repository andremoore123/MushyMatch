package capstone.project.mushymatch.view.scan.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
    private var searchQuery: String = ""



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

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                performSearch(query)
            }
        })

    }

    internal fun performSearch(query: String) {
        // Lakukan pencarian sesuai dengan query
        // Misalnya, filter daftar jamur berdasarkan query nama
        val filteredMushrooms = mushroomViewModel.mushrooms.value?.filter { mushroom ->
            mushroom.name.contains(query, ignoreCase = true)
        }

        // Perbarui daftar jamur pada adapter dengan hasil pencarian
        filteredMushrooms?.let { mushroomAdapter.setMushrooms(it) }
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
