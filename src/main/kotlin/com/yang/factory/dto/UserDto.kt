package com.yang.factory.dto

import java.io.Serializable



/**
 * auther:yyy
 * date:2019/9/6-10:17
 * project:factory
 */
/**
 * 用户对象
 */
class UserDto: Serializable {

    var username: String = ""
    var password: CharArray? = null
    var encryptPwd: String? = null
    var userId: String? = null
    var salt: String? = null
    var roles: List<String>? = null

}