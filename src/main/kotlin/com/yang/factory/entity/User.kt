package com.yang.factory.entity

import java.util.Date
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * auther:yyy
 * date:2019/9/6-10:09
 * project:factory
 */
@Entity
data class User(@Id val id: String?){
        var name: String? = null
        var loginName: String? = null
        var loginPwd: String? = null
        var createTime: Date? = null
        var status: String? = null
        var updateTime: Date? = null
        var mngStatus: String? = null
}