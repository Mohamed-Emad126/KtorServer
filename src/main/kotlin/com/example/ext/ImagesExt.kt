package com.example.ext

import java.io.FileOutputStream
import java.io.IOException
import java.util.*


fun String.decodeToBase64(): ByteArray {
    return Base64.getDecoder().decode(this)
}
@Throws(IOException::class)
fun saveImage(image: String, id: Int) {
    val byteArray = image.decodeToBase64()
    val outImage = FileOutputStream("E:\\Programming\\ktor\\KtorServer\\images\\${id}.JPG")
    outImage.write(byteArray)
    outImage.flush()
    outImage.close()
}
