package com.raul.licenta.service

import com.raul.licenta.exception.ServerBaseException
import com.raul.licenta.model.ExceptionMessage
import com.raul.licenta.model.Image
import com.raul.licenta.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.MessageDigest
import javax.transaction.Transactional

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
            throw ServerBaseException(ExceptionMessage.UploadCorrupted)
        }
        if (imageRepository.findByChecksum(image.checksum).isPresent) {
            throw ServerBaseException(ExceptionMessage.ImageExists)
        }
        if (imageRepository.findByName(image.name).isPresent) {
            throw ServerBaseException(ExceptionMessage.ImageWithNameExists)
        }
        imageRepository.save(image)
    }

    fun getAll(): List<Image> {
        return imageRepository.findAll()
    }

    fun getImageData(id: Long): ByteArray {
        val image = imageRepository.findById(id).orElseThrow {
            ServerBaseException(ExceptionMessage.NoImageWithThatId)
        }
        return image.picture
    }

    @Transactional
    fun delete(id: Long) {
        imageRepository.findById(id).orElseThrow {
            ServerBaseException(ExceptionMessage.NoImageWithThatId)
        }
        imageRepository.deleteById(id)
    }
}