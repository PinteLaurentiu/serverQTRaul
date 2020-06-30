package com.raul.licenta.mapper

import com.raul.licenta.dto.ImageDto
import com.raul.licenta.model.*
import com.raul.licenta.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ImageMapper(
        @Autowired
        val userService: UserService
) {
    fun getImage(dto: ImageDto, data: ByteArray): Image {
        return Image(dto.id,
                dto.width,
                dto.height,
                dto.checksum,
                dto.name,
                dto.description,
                data,
                userService.currentUser(),
                dto.imageType)
    }

    fun getImage(model: List<Image>): List<ImageDto> = model.map { getImage(it) }

    fun getImage(model: Image) : ImageDto {
       return ImageDto(model.id,
               model.name,
               model.description,
               model.width,
               model.height,
               model.checksum,
               model.imageType)
    }
}