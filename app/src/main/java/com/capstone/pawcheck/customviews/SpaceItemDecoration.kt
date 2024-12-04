package com.capstone.pawcheck.customviews

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount
        val position = parent.getChildAdapterPosition(view)

        if (position % spanCount != 0) {
            outRect.left = space
        }
        outRect.right = space

        if (position < spanCount) {
            outRect.top = space
        }
        outRect.bottom = space
    }
}
