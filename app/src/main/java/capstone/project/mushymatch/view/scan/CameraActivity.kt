package capstone.project.mushymatch.view.scan

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import capstone.project.mushymatch.databinding.ActivityCameraBinding
import capstone.project.mushymatch.util.*
import java.io.File
import java.io.FileOutputStream

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList.find {
            cameraManager.getCameraCharacteristics(it).get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
        }

        binding.flashlight.setOnClickListener {
            try {
                cameraId?.let { it1 -> cameraManager.setTorchMode(it1, true) }
            } catch (e: CameraAccessException) {
                Toast.makeText(this@CameraActivity, "Tidak dapat mengaktifkan lampu kilat. Pastikan tidak ada aplikasi lain yang menggunakan kamera.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } catch (e: SecurityException) {
                Toast.makeText(this@CameraActivity, "Tidak dapat mengaktifkan lampu kilat. Perizinan tidak diberikan.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }

        binding.importImage.setOnClickListener{
            startGallery()
        }

        binding.captureImage.setOnClickListener { takePhoto() }
        startCamera()

    }

    private var getFile: File? = null

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile

            val savedUri = Uri.fromFile(myFile)
            val bundle = Bundle()
            bundle.putBoolean("isBackCamera", true)
            bundle.putParcelable("selected_image", savedUri)

            val intent = Intent(this@CameraActivity, ResultActivity::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        val isBackCamera = cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: Uri.fromFile(photoFile)

                    val bundle = Bundle()
                    bundle.putBoolean("isBackCamera", isBackCamera)
                    bundle.putParcelable("selected_image", savedUri)

                    if (!isBackCamera) {
                        val rotatedBitmap = rotateImg(BitmapFactory.decodeFile(photoFile.path), isBackCamera)
                        val rotatedFile = createTempFile(this@CameraActivity)
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, FileOutputStream(rotatedFile))
                        bundle.putParcelable("selected_image", Uri.fromFile(rotatedFile))
                    }

                    Log.d(TAG, "Photo capture succeeded: $savedUri")
                    val intent = Intent(this@CameraActivity, ResultActivity::class.java)
                    intent.putExtra("bundle", bundle)
                    startActivity(intent)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Toast.makeText(this@CameraActivity, "Gagal meluncurkan kamera", Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI(state: Boolean) {
        @Suppress("DEPRECATION")
        if (state) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.hide()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.show(WindowInsets.Type.statusBars())
            } else {
                window.clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
                )
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Not getting permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(baseContext, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        hideSystemUI(true)
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        hideSystemUI(false)
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}

