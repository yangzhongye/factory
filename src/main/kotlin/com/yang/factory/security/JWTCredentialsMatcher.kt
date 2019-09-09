package com.yang.factory.security

import com.auth0.jwt.exceptions.JWTVerificationException
import java.io.UnsupportedEncodingException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.yang.factory.dto.UserDto
import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.credential.CredentialsMatcher

/**
 * auther:yyy
 * date:2019/9/6-11:42
 * project:factory
 */
class JWTCredentialsMatcher : CredentialsMatcher {

    override fun doCredentialsMatch(authenticationToken: AuthenticationToken, authenticationInfo: AuthenticationInfo): Boolean {
        val token = authenticationToken.credentials as String
        val stored = authenticationInfo.getCredentials()
        val salt = stored.toString()

        val user = authenticationInfo.getPrincipals().getPrimaryPrincipal() as UserDto
        try {
            val algorithm = Algorithm.HMAC256(salt)
            val verifier = JWT.require(algorithm)
                    .withClaim("username", user.username)
                    .build()
            verifier.verify(token)
            return true
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } catch (e: JWTVerificationException) {
            e.printStackTrace()
        }

        return false
    }

}