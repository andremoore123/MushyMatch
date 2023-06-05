package capstone.project.mushymatch.view.scan

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import capstone.project.mushymatch.databinding.ActivityResultBinding
import capstone.project.mushymatch.view.about.MushroomInformationActivity
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
@Suppress("DEPRECATION")
class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var interpreter: Interpreter
    private var classificationResult = 0
    private val labelList = mutableListOf<String>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        // Load the machine learning model
        interpreter = loadModelFile()

        val bundle = intent.getBundleExtra("bundle")
        val selectedImage = bundle?.getParcelable<Uri>("selected_image")
        val bitmap = BitmapFactory.decodeFile(selectedImage?.path)
        val croppedBitmap = cropToSquare(bitmap)
        binding.previewImage.setImageBitmap(croppedBitmap)

        binding.btnTakePictureAgain.setOnClickListener {
            finish()
        }
        //move to mushroom information activity with label

        binding.btnMoreInformation.setOnClickListener {
            val intent = Intent(this, MushroomInformationActivity::class.java)
            intent.putExtra("label", classificationResult)
            startActivity(intent)
        }


        processImage(croppedBitmap)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadModelFile(): Interpreter {
        val modelFileDescriptor = assets.openFd("model.tflite")
        val inputStream = FileInputStream(modelFileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = modelFileDescriptor.startOffset
        val declaredLength = modelFileDescriptor.declaredLength
        val mappedByteBuffer: MappedByteBuffer =
            fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        val interpreter = Interpreter(mappedByteBuffer)
        inputStream.close()
        return interpreter
    }

    private fun preprocessImage(image: Bitmap): ByteBuffer {
        val inputSize = 224
        val resizedImage = Bitmap.createScaledBitmap(image, inputSize, inputSize, false)
        val inputBuffer = ByteBuffer.allocateDirect(inputSize * inputSize * 3 * 4)

        inputBuffer.order(ByteOrder.nativeOrder())
        inputBuffer.rewind()

        val pixels = IntArray(inputSize * inputSize)
        resizedImage.getPixels(pixels, 0, inputSize, 0, 0, inputSize, inputSize)

        for (pixelValue in pixels) {
            val r = (pixelValue shr 16 and 0xFF).toFloat()
            val g = (pixelValue shr 8 and 0xFF).toFloat()
            val b = (pixelValue and 0xFF).toFloat()

            inputBuffer.putFloat(r)
            inputBuffer.putFloat(g)
            inputBuffer.putFloat(b)
        }

        return inputBuffer
    }

    private fun runInference(inputBuffer: ByteBuffer): Pair<Int, Float> {
        val outputShape = interpreter.getOutputTensor(0).shape()
        val outputSize = outputShape[1]

        val outputBuffer = ByteBuffer.allocateDirect(outputSize * 4)
        outputBuffer.order(ByteOrder.nativeOrder())
        outputBuffer.rewind()

        interpreter.run(inputBuffer, outputBuffer)

        return processOutput(outputBuffer)
    }

    private fun processOutput(outputBuffer: ByteBuffer): Pair<Int, Float> {
        val outputFloatArray = FloatArray(outputBuffer.capacity() / 4)
        outputBuffer.rewind()
        outputBuffer.asFloatBuffer().get(outputFloatArray)

        var maxIndex = 0
        var maxValue = outputFloatArray[0]
        for (i in 1 until outputFloatArray.size) {
            if (outputFloatArray[i] > maxValue) {
                maxValue = outputFloatArray[i]
                maxIndex = i
            }
        }

        return Pair(maxIndex, maxValue)
    }

    @SuppressLint("SetTextI18n")
    private fun processImage(image: Bitmap) {
        val inputBuffer = preprocessImage(image)
        val (maxIndex, maxValue) = runInference(inputBuffer)

        // Load the label list if it's empty
        if (labelList.isEmpty()) {
            loadLabelList()
        }

        this.classificationResult = maxIndex + 1
        val className = labelList[maxIndex]


        binding.tvResultName.text = "Class: $className"
        binding.tvResultAccuracy.text = "Accuracy: $maxValue"
    }

    private fun loadLabelList() {
        val labelFilename = "labels.txt"
        try {
            val labels = assets.open(labelFilename).bufferedReader().useLines { it.toList() }
            labelList.clear()
            labelList.addAll(labels)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val size = if (width < height) width else height

        val x = (width - size) / 2
        val y = (height - size) / 2

        val matrix = Matrix()

        return Bitmap.createBitmap(bitmap, x, y, size, size, matrix, true)
    }

}


