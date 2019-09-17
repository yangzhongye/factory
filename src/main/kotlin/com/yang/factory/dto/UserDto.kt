package com.yang.factory.dto

import com.fasterxml.jackson.annotation.JsonIgnore
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

    var userId: String? = null
    var username: String? = ""
    var nickname: String? = ""
    var password: CharArray? = null
    @JsonIgnore
    var encryptPwd: String? = null
    @JsonIgnore
    var salt: String? = null
    var roles: List<String>? = null

}