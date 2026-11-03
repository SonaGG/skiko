#include <jni.h>

#include "include/core/SkColorSpace.h"
#include "include/core/SkImage.h"
#include "include/gpu/graphite/BackendTexture.h"
#include "include/gpu/graphite/Image.h"
#include "include/gpu/graphite/Recorder.h"

extern "C" JNIEXPORT jlong JNICALL
Java_org_jetbrains_skia_gpu_graphite_ImageFactoryKt__1nWrapBackendTexture(
        JNIEnv*,
        jclass,
        jlong recorderPtr,
        jlong backendTexturePtr,
        jint colorType,
        jint alphaType,
        jlong colorSpacePtr,
        jint origin) {
    auto recorder = reinterpret_cast<skgpu::graphite::Recorder*>(
            static_cast<uintptr_t>(recorderPtr));
    auto backendTexture = reinterpret_cast<skgpu::graphite::BackendTexture*>(
            static_cast<uintptr_t>(backendTexturePtr));
    auto colorSpace = sk_ref_sp(reinterpret_cast<SkColorSpace*>(
            static_cast<uintptr_t>(colorSpacePtr)));
    return reinterpret_cast<jlong>(SkImages::WrapTexture(
            recorder,
            *backendTexture,
            static_cast<SkColorType>(colorType),
            static_cast<SkAlphaType>(alphaType),
            std::move(colorSpace),
            static_cast<skgpu::Origin>(origin)).release());
}
