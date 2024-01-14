/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.tasks

import dev.icerock.gradle.utils.toKonanFile
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.library.KotlinLibraryLayout
import org.jetbrains.kotlin.library.impl.KotlinLibraryLayoutImpl
import java.io.File
import java.io.FileFilter

abstract class CopyExecutableResourcesToApp : DefaultTask() {

    @get:Input
    abstract val klibs: Property<FileCollection>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    init {
        group = "moko-resources"
    }

    @TaskAction
    fun copyResources() {
        val outputDir: File = outputDirectory.get().asFile

        klibs.get()
            .filter { library -> library.extension == "klib" }
            .filter(File::exists)
            .forEach { inputFile ->
                val klibKonan: org.jetbrains.kotlin.konan.file.File = inputFile.toKonanFile()
                val klib = KotlinLibraryLayoutImpl(klib = klibKonan, component = "default")
                val layout: KotlinLibraryLayout = klib.extractingToTemp

                // extracting bundles
                layout
                    .resourcesDir
                    .absolutePath
                    .let(::File)
                    .listFiles(FileFilter { it.extension == "bundle" })
                    // copying bundles to app
                    ?.forEach {
                        logger.info("${it.absolutePath} copying to $outputDir")
                        it.copyRecursively(target = File(outputDir, it.name), overwrite = true)
                    }
            }
    }
}
