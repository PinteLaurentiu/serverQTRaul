package com.raul.licenta.service

import com.raul.licenta.model.Image
import com.raul.licenta.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class ImageService(
        @Autowired
        val imageRepository: ImageRepository
) {
    fun save(image: Image) {
        val hexString = StringBuffer()
        for (byteValue in MessageDigest.getInstance("SHA-1").digest(image.picture)) {
            val hex = Integer.toHexString(0xff and byteValue.toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        if (image.checksum != hexString.toString()) {
            throw Exception("Upload failed")
        }
        imageRepository.save(image)
    }

    fun getAll(): List<Image> {
        return imageRepository.findAll()
    }

    fun getImageData(id: Long): ByteArray {
        val image = imageRepository.findById(id).orElseThrow {
            Exception("Image not found")
        }
        return image.picture
    }

    fun delete(id: Long) {
        imageRepository.deleteById(id)
    }
}