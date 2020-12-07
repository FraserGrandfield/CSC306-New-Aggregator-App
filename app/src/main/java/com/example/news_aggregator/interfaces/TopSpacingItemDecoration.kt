package com.example.news_aggregator.interfaces

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Class for adding spacing to articles when they are displayed in the recycle view.
 * @property padding Int
 * @constructor
 */
class TopSpacingItemDecoration(private val padding: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
    }
}