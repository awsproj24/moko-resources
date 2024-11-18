/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.metadata

class InvalidResourceKeyException(val key: String) : Exception(
    "Key must start with an ASCII letter or underscore and contain only ASCII letters," +
        " underscores or digits: $key"
)

class InvalidImageResourceConfiguration(val imageName: String) : Exception(
    "For image {$imageName} found only dark image resources." +
        " Please, add correct resources, or remove '-dark' flag from name."
)
