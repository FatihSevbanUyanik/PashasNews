package com.example.pashanews.util

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory

class ImgUtil {
    companion object {
        fun loadImage(imgView: ImageView, url:String?, goneOnFailed: Boolean = false) {
            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
            val circularProgressDrawable = CircularProgressDrawable(imgView.context)
            circularProgressDrawable.strokeWidth = 6f
            circularProgressDrawable.centerRadius = 40f
            circularProgressDrawable.setColorSchemeColors(Color.parseColor("#FF5722"))
            circularProgressDrawable.start()

            Glide.with(imgView.context)
                .load(url)
                .placeholder(circularProgressDrawable)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .timeout(30000)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (goneOnFailed) { imgView.visibility = View.GONE }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(imgView)
        }
    }

}