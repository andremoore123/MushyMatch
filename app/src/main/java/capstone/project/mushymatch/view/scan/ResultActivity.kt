package capstone.project.mushymatch.view.scan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import capstone.project.mushymatch.R
import capstone.project.mushymatch.databinding.ActivityResultBinding
import capstone.project.mushymatch.view.about.MushroomInformationActivity


@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            supportActionBar?.title = "Mushroom identification"
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.back_to) // Ganti dengan gambar ikon kembali yang diinginkan
        }



        val bundle = intent.getBundleExtra("bundle")
        val selectedImage = bundle?.getParcelable<Uri>("selected_image")

        val bitmap = BitmapFactory.decodeFile(selectedImage?.path)
        val croppedBitmap = cropToSquare(bitmap)
        binding.previewImage.setImageBitmap(croppedBitmap)
        binding.btnTakePictureAgain.setBackgroundColor(resources.getColor(R.color.black))

        binding.btnMoreInformation.setOnClickListener {
            //move to AboutMushroomActivity
            val intent = Intent(this, MushroomInformationActivity::class.java)
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

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val size = if (width < height) width else height

        val x = (width - size) / 2
        val y = (height - size) / 2

        val matrix = Matrix()
        matrix.postRotate(90f) // Rotates the image if needed

        return Bitmap.createBitmap(bitmap, x, y, size, size, matrix, true)
    }
}