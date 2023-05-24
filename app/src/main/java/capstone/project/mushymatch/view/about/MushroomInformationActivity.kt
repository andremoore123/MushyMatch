package capstone.project.mushymatch.view.about

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import capstone.project.mushymatch.R
import capstone.project.mushymatch.databinding.ActivityMushroomInformationBinding
import capstone.project.mushymatch.view.recipes.list.ListRecipesActivity

@Suppress("DEPRECATION")
class MushroomInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMushroomInformationBinding
    private var isDescriptionExpanded = false
    private var isHabitatExpanded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMushroomInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            supportActionBar?.title = "Mushroom Information"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }

        binding.ivDesc.isVisible = true
        binding.layoutDescription.setOnClickListener{
            toggleDescription()
        }

        binding.ivHabitat.isVisible = true
        binding.layoutHabitat.setOnClickListener{
            toggleHabitat()
        }

        binding.layoutRecipes.setOnClickListener{
            val intent = Intent(this, ListRecipesActivity::class.java)
            startActivity(intent)
        }

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


    private fun toggleDescription() {
        if (isDescriptionExpanded) {
            // Contract description
            val rotateUpAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_180_reverse)
            rotateUpAnimation.fillAfter = true
            binding.ivDesc.startAnimation(rotateUpAnimation)
            binding.tvDescription.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up))
            binding.tvDescription.visibility = View.GONE
        } else {
            // Expand description
            val rotateDownAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_180) // Updated animation
            rotateDownAnimation.fillAfter = true
            binding.ivDesc.startAnimation(rotateDownAnimation)
            binding.tvDescription.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_down))
            binding.tvDescription.visibility = View.VISIBLE
        }

        isDescriptionExpanded = !isDescriptionExpanded
    }

}