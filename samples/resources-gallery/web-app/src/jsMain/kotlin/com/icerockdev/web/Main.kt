/*
 * Copyright 2022 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.web

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.icerockdev.library.MR
import com.icerockdev.library.Testing
import kotlinx.browser.window
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.rgba
import org.jetbrains.compose.web.dom.Br
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Img
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import dev.icerock.moko.graphics.Color as MokoColor

suspend fun main() {
    MR.fonts.addFontsToPage()

    val strings = MR.stringsLoader.getOrLoad()

    val fileText = mutableStateOf("")

    renderComposable(rootElementId = "root") {
        val rememberFileText = remember { fileText }
        Div(
            attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Column)
                }
            }
        ) {
            Span {
                P(
                    attrs = {
                        style {
                            fontFamily(Testing.getFontTtf1().fontFamily)
                        }
                    }
                ) {
                    Text(Testing.getStringDesc().toLocalizedString(strings))
                }
                Br()
                val color by remember(window) {
                    Testing.getGradientColors().first().getColorFlow(window)
                }.collectAsState(initial = MokoColor(0xFFAAFFFF))
                P(
                    attrs = {
                        style {
                            val (r, g, b, a) = color
                            color(rgba(r, g, b, a))
                        }
                    }
                ) {
                    Text("Color Test")
                }
                Br()
                Img(Testing.getDrawable().fileUrl)
                Br()
                Img(Testing.getVectorDrawable().fileUrl)
                Br()
                Text(rememberFileText.value)
            }
        }
    }

    fileText.value = Testing.getTextFile().getText() + "\n" +
            Testing.getTextsFromAssets()[1].getText() + "\n" +
            MR.files.some_json.getText()

    println(Testing.getTextFile().fileUrl)
}
