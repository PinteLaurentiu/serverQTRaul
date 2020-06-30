package com.raul.licenta.model

import javax.persistence.*

@Entity(name = "image")
data class Image(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0,

        @Column
        var width: Long = 0,

        @Column
        var height: Long = 0,

        @Column(unique = true)
        var checksum: String = "",

        @Column(unique = true)
        var name: String = "",

        @Column(length = 10000)
        var description: String = "",

        @Lob
        @Basic(fetch = FetchType.LAZY)
        @Column(length = 1073741824)
        var picture: ByteArray = ByteArray(0),

        @ManyToOne
        var user: User = User(),

        @Column
        var imageType: ImageType = ImageType.RGB
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (checksum != other.checksum) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + width.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + checksum.hashCode()
        return result
    }
}