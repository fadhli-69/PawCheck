package com.capstone.pawcheck.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel

class RoundedShapeableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ShapeableImageView(context, attrs, defStyleAttr) {

    init {
        val shapeAppearance = ShapeAppearanceModel.builder()
            .setTopLeftCorner(CornerFamily.ROUNDED, 8f)
            .setTopRightCorner(CornerFamily.ROUNDED, 8f)
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .build()

        shapeAppearanceModel = shapeAppearance
    }
}
