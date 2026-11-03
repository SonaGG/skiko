package org.jetbrains.skia.gpu.graphite

import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ExternalSymbolName
import org.jetbrains.skia.ForceNativeThreadState
import org.jetbrains.skia.Image
import org.jetbrains.skia.SurfaceOrigin
import org.jetbrains.skia.impl.Native.Companion.NullPointer
import org.jetbrains.skia.impl.NativePointer
import org.jetbrains.skia.impl.Stats
import org.jetbrains.skia.impl.getPtr
import org.jetbrains.skia.impl.reachabilityBarrier
import org.jetbrains.skiko.ExperimentalSkikoApi

@ExperimentalSkikoApi
fun Image.Companion.wrapBackendTexture(
    recorder: Recorder,
    backendTexture: BackendTexture,
    colorType: ColorType,
    alphaType: ColorAlphaType,
    colorSpace: ColorSpace? = null,
    origin: SurfaceOrigin = SurfaceOrigin.TOP_LEFT,
): Image? {
    return try {
        Stats.onNativeCall()
        val ptr = _nWrapBackendTexture(
            recorder.nativePtr,
            backendTexture.nativePtr,
            colorType.ordinal,
            alphaType.ordinal,
            getPtr(colorSpace),
            origin.ordinal,
        )
        if (ptr == NullPointer) null else Image(ptr)
    } finally {
        reachabilityBarrier(recorder)
        reachabilityBarrier(backendTexture)
        reachabilityBarrier(colorSpace)
    }
}

@ExternalSymbolName("org_jetbrains_skia_gpu_graphite_ImageFactory__1nWrapBackendTexture")
@ForceNativeThreadState
private external fun _nWrapBackendTexture(
    recorderPtr: NativePointer,
    backendTexturePtr: NativePointer,
    colorType: Int,
    alphaType: Int,
    colorSpacePtr: NativePointer,
    origin: Int,
): NativePointer
