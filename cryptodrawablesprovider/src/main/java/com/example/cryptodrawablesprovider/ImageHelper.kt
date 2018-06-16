package com.example.cryptodrawablesprovider

import android.widget.ImageView

/**
 * Helper which takes care of obtaining, decoding and preparing arbitrary images, and then sets them
 * on Android [ImageView]s.
 *
 * Images might be identified in a number of ways: In certain cases they might be identified by an
 * Int id, or a String, whereas in other cases they might be identified via an URL, or file path.
 */
interface ImageHelper<R> {

    /**
     * Sets the image identified by the [resourceIdentifier] on the [imageView].
     */
    fun setImage(imageView: ImageView, resourceIdentifier: R)
}