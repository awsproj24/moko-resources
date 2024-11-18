/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

@file:UseSerializers(FileSerializer::class)

package dev.icerock.gradle.metadata.resource

import dev.icerock.gradle.generator.normalizePathName
import dev.icerock.gradle.metadata.InvalidImageResourceConfiguration
import dev.icerock.gradle.metadata.InvalidResourceKeyException
import dev.icerock.gradle.metadata.resource.ImageMetadata.Appearance.LIGHT
import dev.icerock.gradle.serialization.ColorResourceSerializer
import dev.icerock.gradle.serialization.FileSerializer
import dev.icerock.gradle.serialization.ResourceMetadataSerializer
import dev.icerock.gradle.utils.calculateHash
import dev.icerock.gradle.utils.calculateResourcesHash
import dev.icerock.gradle.utils.nameWithoutScale
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.io.File

@Serializable(with = ResourceMetadataSerializer::class)
@SerialName("resource-metadata")
sealed interface ResourceMetadata {
    val resourceType: String

    val key: String

    fun contentHash(): String?

    fun assertKeyValue() {
        val validKeyRegex = Regex("[a-zA-Z_][a-zA-Z_0-9]*")

        if (!validKeyRegex.matches(key)) {
            throw InvalidResourceKeyException(key)
        }
    }
}

@Serializable
internal data class StringMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = StringMetadata::class.java.name,
    override val key: String,
    val values: List<LocaleItem>,
) : ResourceMetadata {

    init {
        assertKeyValue()
    }

    @Serializable
    data class LocaleItem(
        val locale: String,
        val value: String,
    )

    @Suppress("MagicNumber")
    override fun contentHash(): String = values.hashCode().toString(16)
}

@Serializable
internal data class PluralMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = PluralMetadata::class.java.name,
    override val key: String,
    val values: List<LocaleItem>,
) : ResourceMetadata {

    init {
        assertKeyValue()
    }

    @Serializable
    data class LocaleItem(
        val locale: String,
        val values: List<PluralItem>,
    )

    @Serializable
    data class PluralItem(
        val quantity: Quantity,
        val value: String,
    ) {
        enum class Quantity {
            ZERO, ONE, TWO, FEW, MANY, OTHER
        }
    }

    @Suppress("MagicNumber")
    override fun contentHash(): String = values.hashCode().toString(16)
}

@Serializable
internal data class ImageMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = ImageMetadata::class.java.name,
    override val key: String,
    val values: List<ImageItem>,
) : ResourceMetadata {

    init {
        assertKeyValue()

        if (values.none { it.appearance == LIGHT }) {
            val imageName: String = values.first().filePath.nameWithoutScale
            throw InvalidImageResourceConfiguration(imageName)
        }
    }

    val isThemed: Boolean
        get() {
            val groupedValidItems: Map<Appearance, List<ImageItem>> =
                values.groupBy { it.appearance }
            return groupedValidItems.containsKey(ImageMetadata.Appearance.DARK) &&
                groupedValidItems.containsKey(ImageMetadata.Appearance.LIGHT)
        }

    @Serializable
    data class ImageItem(
        val quality: String?,
        val appearance: Appearance,
        val filePath: File,
    )

    @Serializable
    enum class Appearance(
        val suffix: String,
        val themeSuffix: String,
        val resourceSuffix: String,
    ) {
        LIGHT(suffix = "-light", themeSuffix = "", resourceSuffix = ""),
        DARK(suffix = "-dark", themeSuffix = "_dark", resourceSuffix = "-night");

        companion object {
            fun getFromFile(file: File): Appearance {
                return if (file.nameWithoutScale.endsWith(Appearance.DARK.suffix)) DARK else LIGHT
            }
        }
    }

    override fun contentHash(): String = values.map {
        it.filePath.calculateResourcesHash()
    }.calculateHash()
}

@Serializable
internal data class FontMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = FontMetadata::class.java.name,
    override val key: String,
    val filePath: File,
) : ResourceMetadata {

    init {
        assertKeyValue()
    }

    override fun contentHash(): String = filePath.calculateResourcesHash()
}

@Serializable
data class ColorMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = ColorMetadata::class.java.name,
    override val key: String,
    val value: ColorItem,
) : ResourceMetadata {

    init {
        assertKeyValue()
    }

    @Serializable(with = ColorResourceSerializer::class)
    sealed interface ColorItem {
        val colorType: String

        @Serializable
        data class Single(
            @OptIn(ExperimentalSerializationApi::class)
            @EncodeDefault
            override val colorType: String = getJavaName(),
            val color: Color,
        ) : ColorItem {
            companion object {
                internal fun getJavaName(): String {
                    return Single::class.java.name.replace('$', '.')
                }
            }
        }

        @Serializable
        data class Themed(
            @OptIn(ExperimentalSerializationApi::class)
            @EncodeDefault
            override val colorType: String = getJavaName(),
            val light: Color,
            val dark: Color,
        ) : ColorItem {
            companion object {
                internal fun getJavaName(): String {
                    return Themed::class.java.name.replace('$', '.')
                }
            }
        }
    }

    @Serializable
    data class Color(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Int,
    ) {
        @Suppress("MagicNumber")
        fun toArgbHex(): String {
            return listOf(alpha, red, green, blue).joinToString(separator = "") {
                it.toString(16).padStart(2, '0')
            }
        }

        @Suppress("MagicNumber")
        fun toRgbaHex(): String {
            return listOf(red, green, blue, alpha).joinToString(separator = "") {
                it.toString(16).padStart(2, '0')
            }
        }
    }

    @Suppress("MagicNumber")
    override fun contentHash(): String = value.hashCode().toString(16)
}

@Serializable
internal data class FileMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = FileMetadata::class.java.name,
    override val key: String,
    val relativePath: File,
    val filePath: File,
) : ResourceMetadata, HierarchyMetadata {

    init {
        assertKeyValue()
    }

    override val path: List<String> = getFilePath(filePath, relativePath)

    override fun contentHash(): String = filePath.calculateResourcesHash()
}

@Serializable
internal data class AssetMetadata(
    @OptIn(ExperimentalSerializationApi::class)
    @EncodeDefault
    override val resourceType: String = AssetMetadata::class.java.name,
    override val key: String,
    val relativePath: File,
    val filePath: File,
) : ResourceMetadata, HierarchyMetadata {

    init {
        assertKeyValue()
    }

    val pathRelativeToBase: File
        get() = filePath.relativeTo(relativePath)

    override val path: List<String> = getFilePath(filePath, relativePath)

    override fun contentHash(): String = filePath.calculateResourcesHash()
}

interface HierarchyMetadata : ResourceMetadata {
    val path: List<String>
}

private fun getFilePath(filePath: File, relativePath: File): List<String> {
    return filePath.relativeTo(relativePath).path
        .split(File.separatorChar)
        .dropLast(1)
        .map(::normalizePathName)
}
