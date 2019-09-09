package com.yang.factory.security

import com.yang.factory.dto.UserDto
import com.yang.factory.service.UserService
import org.apache.shiro.authc.*
import org.apache.shiro.util.ByteSource
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.authc.credential.HashedCredentialsMatcher
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.authz.SimpleAuthorizationInfo

/**
 * auther:yyy
 * date:2019/9/6-9:56
 * project:factory
 */
class DbShiroRealm(private val userService: UserService) : AuthorizingRealm() {

    override fun doGetAuthorizationInfo(principals: PrincipalCollection): AuthorizationInfo {
        val simpleAuthorizationInfo = SimpleAuthorizationInfo()
        val user = principals.primaryPrincipal as UserDto
        var roles = user.roles
        if (roles == null) {
            roles = userService.getUserRoles(user.userId)
            user.roles = roles
        }
        if (roles != null)
            simpleAuthorizationInfo.addRoles(roles)

        return simpleAuthorizationInfo
    }

    init {
        //因为数据库中的密码做了散列，所以使用shiro的散列Matcher
        this.credentialsMatcher = HashedCredentialsMatcher(Sha256Hash.ALGORITHM_NAME)
    }

    /**
     * 找它的原因是这个方法返回true
     */
    override fun supports(token: AuthenticationToken?): Boolean {
        return token is UsernamePasswordToken
    }

    /**
     * 这一步我们根据token给的用户名，去数据库查出加密过用户密码，然后把加密后的密码和盐值一起发给shiro，让它做比对
     */
    @Throws(AuthenticationException::class)
    override fun doGetAuthenticationInfo(token: AuthenticationToken): AuthenticationInfo {
        val userpasswordToken = token as UsernamePasswordToken
        val username = userpasswordToken.username
        val user = userService.getUserInfo(username) ?: throw AuthenticationException("用户名或者密码错误")

        //userId 做盐粒
        return SimpleAuthenticationInfo(user, user.encryptPwd, ByteSource.Util.bytes(user.userId), "dbRealm")
    }

    /*companion object {
        //数据库存储的用户密码的加密salt，正式环境不能放在源代码里
        private val encryptSalt = "F12839WhsnnEV$#23b"
    }*/

}