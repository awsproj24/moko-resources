/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("Filename")

package dev.icerock.moko.resources.test

import dev.icerock.moko.graphics.Color
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.internal.SupportedLocales
import dev.icerock.moko.resources.provider.RemoteJsStringLoader

actual fun createImageResourceMock(): ImageResource {
    return ImageResource(fileUrl = "", fileName = "")
}

actual fun createStringResourceMock(): StringResource {
    return StringResource(
        key = "",
        loader = RemoteJsStringLoader.Impl(
            supportedLocales = SupportedLocales(locales = emptyList()),
            fallbackFileUri = ""
        )
    )
}

actual fun createFileResourceMock(): FileResource {
    return FileResource(fileUrl = "")
}

actual fun createFontResourceMock(): FontResource {
    return FontResource(fileUrl = "", fontFamily = "")
}

actual fun createColorResourceMock(): ColorResource = ColorResource(Color(0x0), Color(0x0))
