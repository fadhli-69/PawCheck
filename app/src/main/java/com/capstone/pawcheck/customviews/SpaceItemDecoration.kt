package com.capstone.pawcheck.customviews

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val position = parent.getChildAdapterPosition(view)

        // Menambahkan spasi kiri dan kanan
        if (position % spanCount != 0) {
            outRect.left = space // Menambahkan jarak di kiri (untuk kolom selain yang pertama)
        }
        outRect.right = space // Menambahkan jarak di kanan

        // Menambahkan spasi atas dan bawah
        if (position < spanCount) {
            outRect.top = space // Menambahkan jarak di atas untuk baris pertama
        }
        outRect.bottom = space // Menambahkan jarak di bawah
    }
}
