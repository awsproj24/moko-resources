/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.resources.plural

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec.Builder
import dev.icerock.gradle.generator.Constants
import dev.icerock.gradle.generator.Constants.Jvm
import dev.icerock.gradle.generator.Constants.PlatformDetails
import dev.icerock.gradle.generator.PlatformResourceGenerator
import dev.icerock.gradle.generator.addJvmPlatformResourceBundleProperty
import dev.icerock.gradle.generator.addJvmPlatformResourceClassLoaderProperty
import dev.icerock.gradle.generator.addValuesFunction
import dev.icerock.gradle.generator.localization.LanguageType
import dev.icerock.gradle.metadata.resource.PluralMetadata
import dev.icerock.gradle.utils.convertXmlStringToLocalization
import java.io.File

internal class JvmPluralResourceGenerator(
    private val flattenClassPackage: String,
    private val resourcesGenerationDir: File,
) : PlatformResourceGenerator<PluralMetadata> {
    override fun imports(): List<ClassName> = emptyList()

    override fun generateInitializer(metadata: PluralMetadata): CodeBlock {
        return CodeBlock.of(
            "PluralsResource(resourcesClassLoader = %L, bundleName = %L, key = %S)",
            "${PlatformDetails.platformDetailsPropertyName}.${Jvm.resourcesClassLoaderPropertyName}",
            pluralsBundlePropertyName,
            metadata.key
        )
    }

    override fun generateResourceFiles(data: List<PluralMetadata>) {
        data.processLanguages().forEach { (lang, strings) ->
            generateLanguageFile(
                language = LanguageType.fromLanguage(lang),
                strings = strings
            )
        }
    }

    override fun generateBeforeProperties(
        builder: Builder,
        metadata: List<PluralMetadata>,
        modifier: KModifier?,
    ) {
        builder.addJvmPlatformResourceClassLoaderProperty(modifier = modifier)

        builder.addJvmPlatformResourceBundleProperty(
            bundlePropertyName = pluralsBundlePropertyName,
            bundlePath = getBundlePath()
        )
    }

    override fun generateAfterProperties(
        builder: Builder,
        metadata: List<PluralMetadata>,
        modifier: KModifier?,
    ) {
        builder.addValuesFunction(
            modifier = modifier,
            metadata = metadata,
            classType = Constants.pluralsResourceName
        )
    }

    private fun generateLanguageFile(
        language: LanguageType,
        strings: Map<String, Map<String, String>>,
    ) {
        val fileDirName = "${getBundlePath()}${language.jvmResourcesSuffix}"

        val localizationDir = File(resourcesGenerationDir, Constants.Jvm.localizationDir)
        localizationDir.mkdirs()

        val stringsFile = File(localizationDir, "$fileDirName.properties")

        val content: String = strings.map { (key, pluralMap) ->
            val keysWithPlurals = pluralMap.map { (quantity, value) ->
                "$key.$quantity" to value
            }
            keysWithPlurals.joinToString("\n") { (key, value) ->
                "$key = ${value.convertXmlStringToLocalization()}"
            }
        }.joinToString("\n")

        stringsFile.writeText(content)
    }

    private fun getBundlePath(): String = "${flattenClassPackage}_$pluralsBundleName"

    private companion object {
        const val pluralsBundlePropertyName = "pluralsBundle"
        const val pluralsBundleName = "mokoPluralsBundle"
    }
}
