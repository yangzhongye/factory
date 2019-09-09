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
data class User(
        @Id
        val id: String,
        val name: String?,
        val loginName: String?,
        val loginPwd: String?,
        val createTime: Date?,
        val status: String?,
        val updateTime: Date?,
        val mngStatus: String?
)