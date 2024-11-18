/*
 * Copyright 2023 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.getUIColor
import kotlinx.cinterop.DoubleVarOf
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloat
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun colorResource(resource: ColorResource): Color {
    // TODO https://github.com/icerockdev/moko-resources/issues/443
    //  recompose when appearance changed (now not works in runtime!)
    val darkMode: Boolean = isSystemInDarkTheme()
    return remember(resource, darkMode) {
        val uiColor: UIColor = resource.getUIColor()

        memScoped {
            val red: DoubleVarOf<CGFloat> = alloc()
            val green: DoubleVarOf<CGFloat> = alloc()
            val blue: DoubleVarOf<CGFloat> = alloc()
            val alpha: DoubleVarOf<CGFloat> = alloc()

            uiColor.getRed(
                red = red.ptr,
                green = green.ptr,
                blue = blue.ptr,
                alpha = alpha.ptr
            )

            Color(
                red = red.value.toFloat(),
                green = green.value.toFloat(),
                blue = blue.value.toFloat(),
                alpha = alpha.value.toFloat()
            )
        }
    }
}
