package org.example.application.service

import java.io.InputStream

class ImageService(

) {
    fun sanitize(
        input: InputStream,
        format: String = "jpg",
        maxWidth: Int = 320,
        maxHeight: Int = 320
    ): ByteArray {
        TODO("Not yet implemented")
    }
}