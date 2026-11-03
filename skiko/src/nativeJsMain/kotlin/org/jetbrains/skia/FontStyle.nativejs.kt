package org.jetbrains.skia

import kotlin.jvm.JvmInline

@JvmInline
actual value class FontStyle internal actual constructor(actual val _value: Int) {
    actual constructor(weight: FontWeight, width: FontWidth, slant: FontSlant) : this(packFontStyle(weight, width, slant))

    @Deprecated("Use the FontWeight and FontWidth constructor", level = DeprecationLevel.HIDDEN)
    constructor(weight: Int, width: Int, slant: FontSlant) : this(FontWeight(weight), FontWidth(width), slant)

    actual val weight: FontWeight
        get() = FontWeight(_value and 65535)

    actual fun withWeight(weight: Int): FontStyle = FontStyle(FontWeight(weight), width, slant)

    actual val width: FontWidth
        get() = FontWidth(_value shr 16 and 255)

    actual fun withWidth(width: Int): FontStyle = FontStyle(weight, FontWidth(width), slant)

    actual val slant: FontSlant
        get() = FontSlant.entries[_value shr 24 and 255]

    actual fun withSlant(slant: FontSlant): FontStyle = FontStyle(weight, width, slant)

    override fun toString(): String = "FontStyle(weight=$weight, width=$width, slant=$slant)"

    actual companion object {
        actual val NORMAL = FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.UPRIGHT)
        actual val BOLD = FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.UPRIGHT)
        actual val ITALIC = FontStyle(FontWeight.NORMAL, FontWidth.NORMAL, FontSlant.ITALIC)
        actual val BOLD_ITALIC = FontStyle(FontWeight.BOLD, FontWidth.NORMAL, FontSlant.ITALIC)
    }
}
