/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.resources.desc

import dev.icerock.moko.resources.PluralsResource
import platform.Foundation.NSBundle
import kotlin.test.Test
import kotlin.test.assertEquals

class PluralFormattedStringDescIntTests {
    @Test
    fun testZeroCase() {
        assertEquals(
            expected = "0 found",
            actual = createPluralFormatted(0).localized()
        )
    }

    @Test
    fun testOneCase() {
        assertEquals(
            expected = "1 item",
            actual = createPluralFormatted(1).localized()
        )
    }

    @Test
    fun testFewCase() {
        assertEquals(
            expected = "3 items",
            actual = createPluralFormatted(3).localized()
        )
    }

    @Test
    fun testManyCase() {
        assertEquals(
            expected = "6 items",
            actual = createPluralFormatted(6).localized()
        )
    }

    @Test
    fun testOtherCase() {
        assertEquals(
            expected = "130 items",
            actual = createPluralFormatted(130).localized()
        )
    }

    private fun createPluralFormatted(number: Int): PluralFormattedStringDesc {
        val pluralResource = PluralsResource(
            resourceId = "intFormatted",
            bundle = NSBundle.bundleWithPath(NSBundle.mainBundle.bundlePath + "/tests.bundle")!!
        )
        return PluralFormattedStringDesc(
            pluralsRes = pluralResource,
            number = number,
            args = listOf(number)
        )
    }
}
