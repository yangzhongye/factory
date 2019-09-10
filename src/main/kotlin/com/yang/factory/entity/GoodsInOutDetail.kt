package com.yang.factory.entity

import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * auther:yyy
 * date:2019/9/6-15:21
 * project:factory
 */
@Entity
data class GoodsInOutDetail(@Id val id: String?) {

        var goodsId: String? = null
        var type: String? = null
        var changeQuantity: BigDecimal? = null
        var currentQuantity: BigDecimal? = null
        var createTime: Date? = null
        var operUserId: String? = null
        var operUserName: String? = null
}