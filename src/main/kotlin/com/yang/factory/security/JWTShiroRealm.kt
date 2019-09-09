package com.yang.factory.security

import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authc.SimpleAuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import com.yang.factory.service.UserService
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.realm.AuthorizingRealm

/**
 * 自定义身份认证
 * 基于HMAC（ 散列消息认证码）的控制域
 */

class JWTShiroRealm(private val userService: UserService) : AuthorizingRealm() {

    init {
        this.credentialsMatcher = JWTCredentialsMatcher()
    }

    override fun supports(token: AuthenticationToken?): Boolean {
        return token is JWTToken
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Throws(AuthenticationException::class)
    override fun doGetAuthenticationInfo(authcToken: AuthenticationToken): AuthenticationInfo {
        val jwtToken = authcToken as JWTToken
        val token = jwtToken.token

        val user = userService.getJwtTokenInfo(JwtUtils.getUsername(token)!!)

        return SimpleAuthenticationInfo(user, user?.salt, "jwtRealm")
    }

    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
        return SimpleAuthorizationInfo()
    }
}