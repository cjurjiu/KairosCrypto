package com.catalinj.cryptosmart.presentationlayer.common.view.custom

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.catalinj.cryptosmart.R

/**
 * Custom [RecyclerView] that supports a "maxHeight" constrain the xml layout, which limits its height
 * to that value.
 *
 * Example with a `MaxHeightRecyclerView` that has its `android:height="match_parent"` but also declares
 * a max height of `240dp`:
 * ```
 *     <com.catalinj.cryptosmart.presentationlayer.common.view.custom.MaxHeightRecyclerView
 *       android:id="@+id/my_height_bound_recycler_view"
 *       android:layout_width="match_parent"
 *       android:layout_height="match_parent"
 *       app:maxHeight="240dp" />
 *```
 *
 * Created by catalin on 22/04/2018.
 */
class MaxHeightRecyclerView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RecyclerView(context, attrs, defStyleAttr) {

    private val maxHeightPixels: Int

    init {
        if (attrs != null) {
            val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightRecyclerView)
            maxHeightPixels = typedArray.getDimensionPixelSize(R.styleable.MaxHeightRecyclerView_maxHeight, MAX_HEIGHT_NOT_SET)
            typedArray.recycle()
        } else {
            maxHeightPixels = MAX_HEIGHT_NOT_SET
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val maxHeightAwareHeightSpec =
                if (maxHeightPixels != MAX_HEIGHT_NOT_SET) {
                    View.MeasureSpec.makeMeasureSpec(maxHeightPixels, View.MeasureSpec.AT_MOST)
                } else {
                    heightSpec
                }
        super.onMeasure(widthSpec, maxHeightAwareHeightSpec)
    }

    private companion object {
        const val MAX_HEIGHT_NOT_SET = -2
    }
}