package com.example.cryptodrawablesprovider.svg

import android.support.annotation.NonNull
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.caverock.androidsvg.SVGParseException
import com.bumptech.glide.load.resource.SimpleResource
import com.caverock.androidsvg.SVG
import java.io.IOException
import java.io.InputStream


/**
 * Decodes an SVG internal representation from an [InputStream].
 */
class SvgDecoder : ResourceDecoder<InputStream, SVG> {

    override fun handles(@NonNull source: InputStream, @NonNull options: Options): Boolean {
        return true
    }

    @Throws(IOException::class)
    override fun decode(@NonNull source: InputStream, width: Int, height: Int,
               @NonNull options: Options): Resource<SVG> {
        try {
            val svg = SVG.getFromInputStream(source)
            return SimpleResource(svg)
        } catch (ex: SVGParseException) {
            throw IOException("Cannot load SVG from stream", ex)
        }

    }
}