package org.jetbrains.skia.gpu.graphite

import org.jetbrains.skia.Color
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.Surface
import org.jetbrains.skia.impl.getPtr
import org.jetbrains.skia.impl.use
import org.jetbrains.skiko.ExperimentalSkikoApi
import org.jetbrains.skiko.InternalSkikoApi
import org.jetbrains.skiko.node.RenderNode
import org.jetbrains.skiko.node.RenderNodeContext
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSkikoApi::class, InternalSkikoApi::class)
class RenderNodeGraphiteTest {
    @Test
    fun renderNodeDrawsIntoGraphiteSurface() = withTestGraphiteContext { context ->
        context.makeRecorder().use { recorder ->
            val surface = Surface.makeRenderTarget(recorder, ImageInfo.makeN32Premul(16, 16))!!
            val paint = Paint().apply { color = Color.RED }
            surface.canvas.drawRect(Rect.makeXYWH(0f, 0f, 8f, 16f), paint)
            RenderNodeContext().use { nodes ->
                RenderNode(nodes).use { node ->
                    node.bounds = Rect.makeXYWH(8f, 0f, 8f, 16f)
                    node.beginRecording().drawRect(Rect.makeWH(8f, 16f), paint)
                    node.endRecording()
                    node.drawInto(surface.canvas)
                }
            }
            recorder.snap().use { assertEquals(true, context.insertRecording(it)) }
            val pixels = _nReadPixels(getPtr(context), getPtr(surface), 16, 16)
            assertEquals(Color.RED, pixels[8 * 16 + 2], "direct draw")
            assertEquals(Color.RED, pixels[8 * 16 + 12], "render node")
            surface.close()
        }
    }
}
