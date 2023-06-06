package capstone.project.mushymatch.view.about

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import capstone.project.mushymatch.R
import capstone.project.mushymatch.api.ApiConfig
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.databinding.ActivityMushroomInformationBinding
import capstone.project.mushymatch.view.recipes.list.ListRecipesActivity
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Suppress("DEPRECATION")
class MushroomInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMushroomInformationBinding
    private var idMushroom = 0
    private var isHabitatExpanded = false
    private lateinit var viewModel: MushroomInformationViewModel
    private lateinit var repository: MushroomRepository
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout
    private val coroutineScope = CoroutineScope(Dispatchers.Main)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMushroomInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)


        // Inisialisasi repository
        repository = MushroomRepository(ApiConfig.createApiService())

        // Inisialisasi ViewModel
        val viewModelFactory = MushroomInformationViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MushroomInformationViewModel::class.java]


        // Mendapatkan maxIndex dari intent
        val maxIndex = intent.getIntExtra("label", 0)
        Log.d("MushroomInformation", "label: $maxIndex")

        viewModel.getMushroomDetail(maxIndex)

        //loading
//        showLoading(true)
        shimmerFrameLayout = binding.shimmerViewContainer
        startShimmer()
        coroutineScope.launch {
            delay(1000)
            viewModel.mushroomDetail.observe(this@MushroomInformationActivity) { mushroomDetail ->
                binding.tvMushroomName.text = mushroomDetail.name
                binding.tvMushroomScientificName.text = mushroomDetail.latinName
                binding.tvDescription.text = mushroomDetail.description
                binding.tvDescHabit.text = mushroomDetail.habitat
                Glide.with(this@MushroomInformationActivity)
                    .load(mushroomDetail.picture)
                    .into(binding.imageMushroom)
                idMushroom = mushroomDetail.idJamur
                stopShimmer()


            }

        }

        supportActionBar?.apply {
            supportActionBar?.title = "Mushroom Information"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }


        binding.ivHabitat.isVisible = true
        binding.layoutHabitat.setOnClickListener{
            toggleHabitat()
        }

        binding.layoutRecipes.setOnClickListener{
            val intent = Intent(this, ListRecipesActivity::class.java)
            intent.putExtra("idJamur", idMushroom)
            startActivity(intent)
        }

    }

    //stop shimmer
    private fun stopShimmer() {
        shimmerFrameLayout.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.content.visibility = View.VISIBLE
    }

    //start shimmer
    private fun startShimmer() {
        shimmerFrameLayout.startShimmer()
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.content.visibility = View.GONE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() // Atur aksi saat tombol kembali diklik
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //toggleHabitat
    private fun toggleHabitat() {
        if (isHabitatExpanded) {
            // Contract description
            val rotateUpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_reverse)
            rotateUpAnimation.fillAfter = true
            binding.ivHabitat.startAnimation(rotateUpAnimation)
            binding.tvDescHabit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
            binding.tvDescHabit.visibility = View.GONE
        } else {
            // Expand description
            val rotateDownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_180) // Updated animation
            rotateDownAnimation.fillAfter = true
            binding.ivHabitat.startAnimation(rotateDownAnimation)
            binding.tvDescHabit.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down))
            binding.tvDescHabit.visibility = View.VISIBLE
        }

        isHabitatExpanded = !isHabitatExpanded
    }

}