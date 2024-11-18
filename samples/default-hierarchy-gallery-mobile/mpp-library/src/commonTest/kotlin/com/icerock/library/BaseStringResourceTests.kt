/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerock.library

import BaseUnitTest
import dev.icerock.moko.resources.desc.StringDesc
import getString
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

abstract class BaseStringResourceTests(
    private val locale: String
) : BaseUnitTest() {

    @BeforeTest
    fun setup() {
        StringDesc.localeType = StringDesc.LocaleType.Custom(locale)
    }

    protected fun stringTest(expected: String, actual: StringDesc) = runTest {
        assertEquals(
            expected = expected,
            actual = actual.getString()
        )
    }

    protected fun pluralTest(expected: String, actual: StringDesc) = runTest {
        assertEquals(
            expected = expected,
            actual = actual.getString()
        )
    }
}
