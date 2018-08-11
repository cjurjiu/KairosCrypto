package com.catalinjurjiu.kairoscrypto.presentationlayer.common.extension

import android.content.res.ColorStateList
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.AppCompatImageView
import com.catalinjurjiu.kairoscrypto.R

fun AppCompatImageView.trendlineForPercent(percent: Float) {
    if (percent > 0f) {
        setImageDrawable(resources.getDrawable(R.drawable.ic_vector_trending_up_black_24dp, null))
        imageTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(resources,
                        R.color.green_primary,
                        context.theme))
    } else {
        setImageDrawable(resources.getDrawable(R.drawable.ic_vector_trending_down_black_24dp, null))
        imageTintList = ColorStateList.valueOf(
                ResourcesCompat.getColor(resources,
                        R.color.red_primary,
                        context.theme))
    }
}