package com.yang.factory.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * auther:yyy
 * date:2019/9/9-9:50
 * project:factory
 */
@Entity
data class GoodsImg(
        @Id
        val id: String,
        val goodsId: String?,
        val content: ByteArray?,
        val originalName: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GoodsImg

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}