package org.jetbrains.skiko

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.PATH_MAX
import platform.posix._fullpath

private const val RESOURCES_PATH = "src/commonTest/resources"

actual fun resourcePath(resourceId: String) = run {
    val filePath = "$RESOURCES_PATH/$resourceId"
    val buffer = ByteArray(PATH_MAX)
    val standardized = buffer.usePinned {
        _fullpath(it.addressOf(0), filePath, PATH_MAX.toULong())?.toKString()
    }
    standardized ?: filePath
}
