package org.jetbrains.skia

import kotlin.jvm.JvmInline

@JvmInline
expect value class FontStyle internal constructor(val _value: Int) {
    constructor(weight: FontWeight, width: FontWidth, slant: FontSlant)

    val weight: FontWeight

    fun withWeight(weight: Int): FontStyle

    val width: FontWidth

    fun withWidth(width: Int): FontStyle

    val slant: FontSlant

    fun withSlant(slant: FontSlant): FontStyle

    companion object {
        val NORMAL: FontStyle
        val BOLD: FontStyle
        val ITALIC: FontStyle
        val BOLD_ITALIC: FontStyle
    }
}

internal fun packFontStyle(weight: FontWeight, width: FontWidth, slant: FontSlant): Int =
    weight.value and 65535 or (width.value and 255 shl 16) or (slant.ordinal shl 24)
