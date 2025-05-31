package dev.deftu.componency.android.platform.input

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import dev.deftu.componency.image.Image
import dev.deftu.componency.image.ImageType
import dev.deftu.componency.platform.input.ClipboardAccess
import java.io.ByteArrayOutputStream

public class AndroidClipboardAccess(
    private val view: View,
    private val context: Context
) : ClipboardAccess {

    private val clipboardManager: ClipboardManager
        get() = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override var clipboardString: String?
        get() {
            val clip = clipboardManager.primaryClip
            if (clip != null && clip.itemCount > 0) {
                return clip.getItemAt(0).coerceToText(context)?.toString()
            }
            return null
        }
        set(value) {
            if (value != null) {
                val clip = ClipData.newPlainText("text", value)
                clipboardManager.setPrimaryClip(clip)
            } else {
                clearClipboard()
            }
        }

    override var clipboardImage: Image?
        get() {
            val clip = clipboardManager.primaryClip
            if (clip != null && clip.itemCount > 0) {
                val item = clip.getItemAt(0)
                val uri = item.uri
                if (uri != null) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                        if (bitmap != null) {
                            val outputStream = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                            val bytes = outputStream.toByteArray()
                            val image = Image(ImageType.RASTER, bytes)
                            image.width = bitmap.width.toFloat()
                            image.height = bitmap.height.toFloat()
                            return image
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return null
        }
        set(value) {
            if (value != null) {
                val bitmap = decodeImage(value)
                if (bitmap != null) {
                    val uri = saveBitmapToCache(bitmap)
                    if (uri != null) {
                        val clip = ClipData.newUri(context.contentResolver, "image", uri)
                        clipboardManager.setPrimaryClip(clip)
                    }
                }
            } else {
                clearClipboard()
            }
        }

    override fun clearClipboard() {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", ""))
    }

    private fun decodeImage(image: Image): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(image.data, 0, image.data.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri? {
        return try {
            val path = MediaStore.Images.Media.insertImage(
                context.contentResolver, bitmap, "clipboard_image", null
            )
            if (path != null) Uri.parse(path) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
