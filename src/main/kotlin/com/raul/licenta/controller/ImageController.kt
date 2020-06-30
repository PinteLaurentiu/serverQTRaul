package com.raul.licenta.controller

import com.raul.licenta.dto.ImageDto
import com.raul.licenta.mapper.ImageMapper
import com.raul.licenta.model.Image
import com.raul.licenta.service.ImageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class ImageController(
        @Autowired
        val imageMapper: ImageMapper,

        @Autowired
        val imageService: ImageService
) {
    @PostMapping("/authenticated/image")
    fun add(@RequestPart imageDto: ImageDto, @RequestPart imageData: ByteArray) {
        imageService.save(imageMapper.getImage(imageDto, imageData))
    }

    @GetMapping("/authenticated/image/all")
    fun getAll(): List<ImageDto> {
        return imageMapper.getImage(imageService.getAll())
    }

    @GetMapping("/authenticated/image/{id}", produces = ["application/octet-stream"])
    fun get(@PathVariable id: Long): ByteArray {
        return imageService.getImageData(id)
    }

    @GetMapping("/authenticated/image/{id}/delete")
    fun delete(@PathVariable id: Long) {
        return imageService.delete(id)
    }
}