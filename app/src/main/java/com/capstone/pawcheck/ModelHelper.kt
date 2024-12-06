package com.capstone.pawcheck

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.SystemClock
import android.util.Log
import androidx.camera.core.ImageProxy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ModelHelper(
    private val context: Context,
    private val listener: ClassifierListener?,
    private val modelName: String = "dog_disease_detection_model.tflite",
    private val inputImageWidth: Int = 224,
    private val inputImageHeight: Int = 224
) {

    private var tfliteInterpreter: org.tensorflow.lite.Interpreter? = null

    init {
        loadModel()
    }

    private fun loadModel() {
        try {
            val assetFileDescriptor = context.assets.openFd(modelName)
            val fileInputStream = assetFileDescriptor.createInputStream()
            val fileChannel = fileInputStream.channel
            val startOffset = assetFileDescriptor.startOffset
            val declaredLength = assetFileDescriptor.declaredLength
            val modelByteBuffer = fileChannel.map(
                java.nio.channels.FileChannel.MapMode.READ_ONLY,
                startOffset,
                declaredLength
            )
            tfliteInterpreter = org.tensorflow.lite.Interpreter(modelByteBuffer)
        } catch (e: Exception) {
            listener?.onError("Failed to load model: ${e.localizedMessage}")
            Log.e(TAG, "Error loading model: ${e.localizedMessage}")
        }
    }

    fun classifyImage(bitmap: Bitmap) {
        if (tfliteInterpreter == null) {
            listener?.onError("Model not loaded.")
            return
        }

        val tensorImage = preprocessImage(bitmap)

        val outputShape = tfliteInterpreter!!.getOutputTensor(0).shape()
        val outputBuffer = TensorBuffer.createFixedSize(outputShape, DataType.FLOAT32)

        val startTime = SystemClock.uptimeMillis()
        tfliteInterpreter!!.run(tensorImage.buffer, outputBuffer.buffer)
        val inferenceTime = SystemClock.uptimeMillis() - startTime

        val results = outputBuffer.floatArray

        val maxIndex = results.indices.maxByOrNull { results[it] } ?: -1
        if (maxIndex >= 0) {
            val disease = LABELS[maxIndex]
            val diagnosis = DIAGNOSES[maxIndex]
            listener?.onResults(results, inferenceTime, disease, diagnosis)
        } else {
            listener?.onError("Failed to classify the image.")
        }
    }


    private fun preprocessImage(bitmap: Bitmap): TensorImage {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true)

        val byteBuffer = ByteBuffer.allocateDirect(4 * inputImageWidth * inputImageHeight * 3)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputImageWidth * inputImageHeight)
        resizedBitmap.getPixels(intValues, 0, inputImageWidth, 0, 0, inputImageWidth, inputImageHeight)

        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f)
            byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)
            byteBuffer.putFloat((pixel and 0xFF) / 255.0f)
        }

        val tensorBuffer = TensorBuffer.createFixedSize(
            intArrayOf(1, inputImageWidth, inputImageHeight, 3), DataType.FLOAT32
        )

        tensorBuffer.loadBuffer(byteBuffer)

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(tensorBuffer)

        return tensorImage
    }


    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: FloatArray, inferenceTime: Long, disease: String, diagnosis: String)
    }

    companion object {
        private const val TAG = "ModelHelper"

        val LABELS = listOf(
            "Blepharitis",
            "Conjunctivitis",
            "Entropion",
            "Eyelid Lump",
            "Nuclear Sclerosis",
            "Pigmented Keratitis"
        )

        val DIAGNOSES = listOf(
            "Blepharitis is an inflammation of the eyelids that can affect one or both eyelids of a dog. Common symptoms include redness, swelling, itching, and discharge from the eye area. Blepharitis can cause discomfort and may affect the dog's quality of life if left untreated.",
            "Conjunctivitis is an inflammation of the conjunctiva, which is the thin tissue lining the front of the eyeball and the inside of the eyelids. In dogs, conjunctivitis can cause redness, swelling, discharge from the eyes, and discomfort.",
            "Entropion is a condition where the eyelid folds inward, causing irritation to the eye. It can lead to inflammation, discomfort, and potentially damage to the cornea if untreated.",
            "An eyelid lump is an abnormal growth in the dog's eyelid area. The lump could be benign or malignant, and depending on the type, treatment can vary.",
            "Nuclear Sclerosis is a normal aging process in dogs where the lens of the eye becomes cloudy. It doesn't impair vision as much as cataracts but should be monitored.",
            "Pigmented Keratitis is a condition where pigment accumulates on the cornea, potentially leading to vision issues. It often occurs alongside other conditions like dry eye.",
            "The accuracy of the prediction is below 65%, so we are unable to display the detected disease. For now, we can only detect 6 diseases. If your dog shows unusual symptoms, please consult a veterinarian directly for a proper diagnosis."
        )


    }

}
