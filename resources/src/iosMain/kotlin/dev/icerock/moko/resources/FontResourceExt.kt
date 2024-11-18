/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources

import cnames.structs.__CTFont
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreText.CTFontCreateWithGraphicsFont
import platform.Foundation.CFBridgingRelease
import platform.UIKit.UIFont

@OptIn(ExperimentalForeignApi::class)
@Suppress("unused")
fun FontResource.uiFont(withSize: Double): UIFont {
    val ctFont: CPointer<__CTFont>? = CTFontCreateWithGraphicsFont(
        graphicsFont = fontRef,
        size = withSize,
        matrix = null,
        attributes = null
    )
    return CFBridgingRelease(ctFont) as UIFont
}
