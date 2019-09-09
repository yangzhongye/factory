package com.yang.factory.security

import org.apache.shiro.authc.HostAuthenticationToken

/**
 * auther:yyy
 * date:2019/9/6-11:16
 * project:factory
 */
class JWTToken @JvmOverloads constructor(val token: String, private val host: String? = null) : HostAuthenticationToken {

    override fun getHost(): String? {
        return host
    }

    override fun getPrincipal(): Any {
        return token
    }

    override fun getCredentials(): Any {
        return token
    }

    override fun toString(): String {
        return "$token:$host"
    }
}