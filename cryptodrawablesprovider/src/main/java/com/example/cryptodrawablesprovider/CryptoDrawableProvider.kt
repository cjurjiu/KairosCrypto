package com.example.cryptodrawablesprovider

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.PictureDrawable
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.catalinjurjiu.common.ActiveActivityProvider
import com.example.cryptodrawablesprovider.glideconfig.GlideApp
import com.example.cryptodrawablesprovider.glideconfig.GlideRequests
import com.example.cryptodrawablesprovider.svg.KairosCryptoSvgLoadListener

/**
 * Helper which uses Glide to fetch crypto currencies icons from Github, and to set them on an
 * [ImageView] after loading.
 *
 * Call [setImage] to trigger a load.
 *
 * @constructor Creates a new instance
 * @param context context used to create the Glide RequestManager instance
 * @param activeActivityProvider used to obtain a themed context
 */
class GithubCryptoIconHelper(context: Context,
                             val activeActivityProvider: ActiveActivityProvider) : ImageHelper<String> {

    private val requestsManager: GlideRequests = GlideApp.with(context.applicationContext)
    private val errorDrawable =
            ContextCompat.getDrawable(activeActivityProvider.activeActivity, ERROR_DRAWABLE_ID)
    private val listener = KairosCryptoSvgLoadListener()

    override fun setImage(imageView: ImageView, resourceIdentifier: String) {
        val resourceUrl = String.format(GITHUB_URL, resourceIdentifier.toLowerCase())
        val animDrawable = activeActivityProvider.activeActivity
                .getDrawable(PLACEHOLDER_DRAWABLE_ID) as AnimatedVectorDrawable
        animDrawable.start()
        requestsManager.`as`(PictureDrawable::class.java)
                .fitCenter()
                .error(errorDrawable)
                .placeholder(animDrawable)
                .listener(listener)
                .load(resourceUrl)
                .into(imageView)
    }

    private companion object {
        const val GITHUB_URL = "https://raw.githubusercontent.com/cjurjiu/cryptocurrency-icons/master/svg/color/%s.svg"
        val ERROR_DRAWABLE_ID = R.drawable.avd_error
        val PLACEHOLDER_DRAWABLE_ID = R.drawable.avd_progress_indeterminate_circular
    }
}