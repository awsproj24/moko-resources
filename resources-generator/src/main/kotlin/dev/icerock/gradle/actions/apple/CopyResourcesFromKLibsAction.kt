/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.actions.apple

import dev.icerock.gradle.utils.klibs
import org.gradle.api.Action
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink
import org.jetbrains.kotlin.library.KotlinLibraryLayout
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import java.io.File

internal abstract class CopyResourcesFromKLibsAction : Action<Task> {

    protected fun copyResourcesFromLibraries(
        linkTask: KotlinNativeLink,
        outputDir: File
    ) {
        linkTask.klibs
            .filter { it.extension == "klib" }
            .filter { it.exists() }
            .forEach { inputFile ->
                linkTask.logger.info("copy resources from $inputFile into $outputDir")
                val klibKonan = org.jetbrains.kotlin.konan.file.File(inputFile.path)
                val klib = KotlinLibraryLayoutImpl(klib = klibKonan, component = "default")
                val layout: KotlinLibraryLayout = klib.extractingToTemp

                try {
                    File(layout.resourcesDir.path).copyRecursively(
                        target = outputDir,
                        overwrite = true
                    )
                } catch (@Suppress("SwallowedException") exc: NoSuchFileException) {
                    linkTask.logger.info("resources in $inputFile not found")
                } catch (@Suppress("SwallowedException") exc: java.nio.file.NoSuchFileException) {
                    linkTask.logger.info("resources in $inputFile not found (empty lib)")
                }
            }
    }
}
