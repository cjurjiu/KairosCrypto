package com.example.cryptodrawablesprovider.svg


import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.PictureDrawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target

/**
 * Listener which stops the animation on any animation drawable set as the placeholder image.
 *
 * If the load is successful, sets the view to render the SVG contents using software. Hardware
 * acceleration is not supported by SVG-Android as of now.
 */
class CryptoSmartSvgLoadListener : RequestListener<PictureDrawable> {

    override fun onLoadFailed(e: GlideException?,
                              model: Any,
                              target: Target<PictureDrawable>,
                              isFirstResource: Boolean): Boolean {

        val view = getViewFromTarget(target)
        view.setLayerType(View.LAYER_TYPE_NONE,null)
        stopAnimation(view)
        return false
    }

    override fun onResourceReady(resource: PictureDrawable,
                                 model: Any,
                                 target: Target<PictureDrawable>,
                                 dataSource: DataSource,
                                 isFirstResource: Boolean): Boolean {

        val view = getViewFromTarget(target)
        view.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        stopAnimation(view)
        return false
    }

    private fun getViewFromTarget(target: Target<PictureDrawable>) = (target as ImageViewTarget<*>).view

    private fun stopAnimation(view: ImageView) {
        if (view.drawable is AnimatedVectorDrawable) {
            (view.drawable as AnimatedVectorDrawable).stop()
        }
    }

}