package com.wlp.myapplication.frame.binding

import androidx.databinding.BindingAdapter
import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.load.resource.bitmap.CircleCrop
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import android.widget.TextView
import com.wlp.myapplication.util.Utils

/**
 * BindingAdapter绑定适配器
 */
object ViewAdapter {
//    @BindingAdapter(
//        value = ["url", "isCircle", "placeholderRes", "roundedCorner"],
//        requireAll = false
//    )
//    fun setImageUri(
//        imageView: ImageView,
//        url: String?,
//        isCircle: Boolean,
//        placeholderRes: Int,
//        roundedCorner: Int
//    ) {
//        var requestBuilder = Glide.with(imageView.context)
//            .load(url)
//        if (isCircle) {
//            requestBuilder = requestBuilder.apply(RequestOptions.bitmapTransform(CircleCrop()))
//        } else {
//            if (roundedCorner > 0) {
//                requestBuilder =
//                    requestBuilder.apply(RequestOptions.bitmapTransform(RoundedCorners(roundedCorner)))
//            }
//        }
//        requestBuilder.apply(RequestOptions().placeholder(placeholderRes).error(placeholderRes))
//            .into(imageView)
//    }

    @BindingAdapter(value = ["ivSrc"], requireAll = false)
    fun setImageSrc(imageView: ImageView, ivSrc: Int) {
        if (ivSrc != 0) {
            imageView.setImageResource(ivSrc)
        }
    }

    @BindingAdapter(value = ["bindTextColor"], requireAll = false)
    fun setTextColor(textView: TextView, bindTextColor: Int) {
        if (bindTextColor != 0) {
            textView.setTextColor(Utils.getColor(bindTextColor))
        }
    }

    @BindingAdapter(value = ["bindTextStyle"], requireAll = false)
    fun setTextStyle(textView: TextView, bindTextStyle: Int) {
        textView.setTypeface(textView.typeface, bindTextStyle)
    }
}