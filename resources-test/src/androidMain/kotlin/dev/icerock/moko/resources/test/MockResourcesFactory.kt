/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.test

import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.FontResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

actual fun createImageResourceMock(): ImageResource = ImageResource(0)
actual fun createStringResourceMock(): StringResource = StringResource(0)
actual fun createFileResourceMock(): FileResource = FileResource(0)
actual fun createFontResourceMock(): FontResource = FontResource(0)
actual fun createColorResourceMock(): ColorResource = ColorResource(0)
