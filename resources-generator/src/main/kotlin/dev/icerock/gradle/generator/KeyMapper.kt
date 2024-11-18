/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator

internal fun generateKey(input: String): String {
    return input
        .replace("-", "_")
        .replace(".", "_")
}

internal fun normalizePathName(input: String): String {
    return generateKey(input).replace(" ", "_")
}
