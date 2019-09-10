package com.yang.factory.dto

import java.io.Serializable
import java.math.BigDecimal

/**
 * auther:yyy
 * date:2019/9/6-15:36
 * project:factory
 */
class GoodsDto: Serializable {

    var id: String? = null
    var seqNo: Long? = null
    var number: String? = ""
    var name: String? = ""
    var quantity: BigDecimal? = BigDecimal.ZERO
    var note: String? = ""

    var page: Int = 0 //jpa 分页默认从0开始
    var pageSize: Int = 10
}