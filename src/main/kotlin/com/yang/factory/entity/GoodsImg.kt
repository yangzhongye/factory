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
        val id: String?,
        val goodsId: String?,
        val content: ByteArray?,
        val originalName: String?
)