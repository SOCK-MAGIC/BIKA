package com.shizq.bika.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Base64Util {

    fun getBase64(path: String): String {
        val baos = ByteArrayOutputStream()
        BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.JPEG, 100, baos)
        baos.flush()
        baos.close()
        return "data:image/jpeg;base64," + Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
    }
}