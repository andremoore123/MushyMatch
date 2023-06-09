package capstone.project.mushymatch.view.recipes.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.media3.session.MediaController
import androidx.recyclerview.widget.LinearLayoutManager
import capstone.project.mushymatch.R
import capstone.project.mushymatch.api.ApiConfig
import capstone.project.mushymatch.api.repository.MushroomRepository
import capstone.project.mushymatch.databinding.ActivityCookingRecipesBinding
import capstone.project.mushymatch.view.recipes.list.RecipeAdapter
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class CookingRecipesActivity : AppCompatActivity() {
    private lateinit var viewModel: CookingRecipesViewModel
    private lateinit var binding: ActivityCookingRecipesBinding
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var stepsAdapter: StepsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCookingRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvIngredients.layoutManager = LinearLayoutManager(this)
        binding.rvSteps.layoutManager = LinearLayoutManager(this)

        supportActionBar?.apply {
            supportActionBar?.title = "Cooking Recipe"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }


        val repository = MushroomRepository(ApiConfig.createApiService())
        val viewModelFactory = CookingRecipesViewModelFactory(repository) // Buat ViewModelFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(CookingRecipesViewModel::class.java)

        val recipeId = intent.getIntExtra("recipe_id", -1) // Menggunakan key "recipe_id"
        if (recipeId != -1) {
            viewModel.loadRecipeDetail(recipeId)
        }

        //inisialisasi adapter

        binding.vRecipesVideo
        viewModel.recipeDetail.observe(this) { recipeDetail ->
            binding.let {
                it.tvRecipesName.text = recipeDetail.nameRecipe
                Glide.with(this)
                    .load(recipeDetail.pictRecipe)
                    .into(it.ivRecipesImage)

                val ingredientsList = recipeDetail.ingredients.split("\n").filter { it.isNotBlank() }
                ingredientsAdapter = IngredientsAdapter(ingredientsList)
                it.rvIngredients.adapter = ingredientsAdapter
                Log.d("ingredients", ingredientsList.toString())

                val stepsList = recipeDetail.steps.split("\n").filter { it.isNotBlank() }
                stepsAdapter = StepsAdapter(stepsList)
                it.rvSteps.adapter = stepsAdapter
                Log.d("steps", stepsList.toString())
            }
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
}

