package com.raul.licenta.repository

import com.raul.licenta.model.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ImageRepository : JpaRepository<Image, Long> {
    fun findByChecksum(checksum: String): Optional<Image>
    fun findByName(name: String): Optional<Image>
}