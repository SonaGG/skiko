package org.jetbrains.skiko

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.*

actual suspend fun loadBytesFromPath(path: String): ByteArray {
    val file = fopen(path, "rb") ?: run {
        val error = strerror(errno)?.toKString() ?: "Unknown error"
        throw Error("Can not open file '$path': $error")
    }

    var bytes = ByteArray(64 * 1024)
    var size = 0
    while (true) {
        if (size == bytes.size) bytes = bytes.copyOf(size * 2)
        val read = bytes.usePinned {
            fread(it.addressOf(size), 1UL, (bytes.size - size).toULong(), file)
        }.toInt()
        if (read == 0) break
        size += read
    }
    val failed = ferror(file) != 0
    fclose(file)

    if (failed) {
        throw Error("Can not read file '$path'")
    }

    return bytes.copyOf(size)
}
