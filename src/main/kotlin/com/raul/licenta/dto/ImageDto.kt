package com.raul.licenta.dto

import com.raul.licenta.model.ImageType

data class ImageDto(
        var id: Long = 0,
        var name: String = "",
        var description: String = "",
        var width: Long = 0,
        var height: Long = 0,
        var checksum: String = "",
        var imageType: ImageType = ImageType.RGB
)
