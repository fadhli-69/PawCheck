package com.capstone.pawcheck

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DogDiseasePredictor(modelPath: String, context: Context) {

    private val tflite: Interpreter
    private val classLabels = arrayOf(
        "Blepharitis",
        "Conjunctivitis",
        "Entropion",
        "Eyelid Lump",
        "Nuclear Sclerosis",
        "Pigmented Keratitis"
    )

    init {
        tflite = Interpreter(loadModelFile(modelPath, context))
    }

    private fun loadModelFile(modelPath: String, context: Context): ByteBuffer {
        // Memastikan bahwa model ada di folder assets
        val fileDescriptor = context.assets.openFd(modelPath)
        val inputStream = fileDescriptor.createInputStream()
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        // Mengalokasikan ByteBuffer untuk model
        val modelByteBuffer = ByteBuffer.allocateDirect(declaredLength.toInt())
        modelByteBuffer.order(ByteOrder.nativeOrder())

        // Membaca model ke dalam ByteBuffer
        fileChannel.position(startOffset)
        fileChannel.read(modelByteBuffer)
        modelByteBuffer.rewind() // Mengatur posisi kembali ke awal

        return modelByteBuffer
    }



    fun predict(bitmap: Bitmap): Pair<String, Float> {
        // Preprocessing gambar
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
        val byteBuffer = ByteBuffer.allocateDirect(4 * 224 * 224 * 3)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(224 * 224)
        resizedBitmap.getPixels(intValues, 0, 224, 0, 0, 224, 224)

        // Mengonversi nilai pixel ke tensor
        for (pixel in intValues) {
            byteBuffer.putFloat(((pixel shr 16) and 0xFF) / 255.0f) // Red
            byteBuffer.putFloat(((pixel shr 8) and 0xFF) / 255.0f)  // Green
            byteBuffer.putFloat((pixel and 0xFF) / 255.0f)         // Blue
        }

        val inputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), org.tensorflow.lite.DataType.FLOAT32)
        inputTensor.loadBuffer(byteBuffer)

        // Ukuran output harus disesuaikan dengan output dari model
        val outputTensor = TensorBuffer.createFixedSize(intArrayOf(1, 6), org.tensorflow.lite.DataType.FLOAT32)
        tflite.run(inputTensor.buffer, outputTensor.buffer)

        // Mengambil hasil prediksi
        val result = outputTensor.floatArray
        val predictedClassIndex = result.indices.maxByOrNull { result[it] } ?: 0
        val predictedClass = classLabels[predictedClassIndex]
        val confidence = result[predictedClassIndex] * 100

        return Pair(predictedClass, confidence)
    }

    fun getDiseaseDescription(predictedClass: String): String {
        return when (predictedClass) {
            "Blepharitis" -> "Blepharitis is an inflammation of the eyelids that can affect one or both eyelids of a dog. Common symptoms include redness, swelling, itching, and discharge from the eye area."
            "Conjunctivitis" -> "Conjunctivitis is an inflammation of the conjunctiva, which is the thin tissue lining the front of the eyeball and the inside of the eyelids."
            "Entropion" -> "Entropion is a condition where the eyelid folds inward, causing irritation to the eye. It can lead to inflammation, discomfort, and potentially damage to the cornea if untreated."
            "Eyelid Lump" -> "An eyelid lump is an abnormal growth in the dog's eyelid area. The lump could be benign or malignant, and depending on the type, treatment can vary."
            "Nuclear Sclerosis" -> "Nuclear Sclerosis is a normal aging process in dogs where the lens of the eye becomes cloudy. It doesn't impair vision as much as cataracts but should be monitored."
            "Pigmented Keratitis" -> "Pigmented Keratitis is a condition where pigment accumulates on the cornea, potentially leading to vision issues."
            else -> "The accuracy of the prediction is below 65%. Please consult a veterinarian directly."
        }
    }

    fun getTreatmentOptions(predictedClass: String): List<String> {
        return when (predictedClass) {
            "Blepharitis" -> listOf(
                "Warm compress to reduce the risk of recurrence.",
                "Trim the hair around the eyes to reduce fluid buildup.",
                "Use baby shampoo to remove dirt that may clog the meibomian gland openings."
            )
            "Conjunctivitis" -> listOf(
                "Use an eye cleaning solution to clean the area around the eyes from dirt and discharge.",
                "Avoid known allergens and use antihistamines if necessary.",
                "If conjunctivitis is caused by an underlying condition, treatment for that condition is required."
            )
            "Entropion" -> listOf(
                "Accurate diagnosis is crucial to determine the severity of entropion.",
                "Before surgery, eye drops may be prescribed to reduce irritation.",
                "Surgery is usually the most effective treatment for entropion."
            )
            "Eyelid Lump" -> listOf(
                "A thorough examination to determine whether the lump is benign or malignant is essential.",
                "If caused by an infection, antibiotics or anti-inflammatory medications may be prescribed.",
                "Surgery may be required to remove the lump, especially if it is cancerous or affecting vision."
            )
            "Nuclear Sclerosis" -> listOf(
                "Monitor the dog’s eye condition regularly for changes in vision.",
                "If cataracts develop, surgery may be needed to remove them."
            )
            "Pigmented Keratitis" -> listOf(
                "Use artificial tears to keep the cornea moist.",
                "Steroids or NSAIDs may help reduce inflammation.",
                "Address underlying conditions like dry eye to prevent further damage."
            )
            else -> emptyList()
        }
    }

    fun getNote(): String {
        return "Always consult with a veterinarian before administering any treatment to your dog. Proper diagnosis and treatment are crucial to ensure effective recovery."
    }
}
