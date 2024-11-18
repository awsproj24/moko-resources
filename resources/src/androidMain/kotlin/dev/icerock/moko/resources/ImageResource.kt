/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

actual data class ImageResource(
    @DrawableRes val drawableResId: Int
) {

    fun getDrawable(context: Context): Drawable? = ContextCompat.getDrawable(context, drawableResId)
}
