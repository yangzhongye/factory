package com.yang.factory.entity

import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * auther:yyy
 * date:2019/9/6-14:48
 * project:factory
 */
@Entity
data class Goods(@Id var id: String?) {
        var seqNo: Long? = null
        var number: String? = null
        var name:String? = null
        var quantity: BigDecimal? = null
        var note: String? = null
        var createTime: Date? = null
        var createUserId: String? = null
        var createUserName: String? = null
        var updateTime: Date? = null
        var updateUserId: String? = null
        var updateUserName: String? = null
}