package com.yang.factory.controller

import org.springframework.http.ResponseEntity
import org.apache.shiro.SecurityUtils
import com.yang.factory.dto.UserDto
import org.springframework.web.bind.annotation.GetMapping
import org.apache.shiro.authc.UsernamePasswordToken
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PostMapping
import com.yang.factory.service.UserService
import org.apache.shiro.authc.AuthenticationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController



/**
 * auther:yyy
 * date:2019/9/6-11:44
 * project:factory
 */

@RestController
class LoginController(private val userService: UserService) {

    /**
     * 用户名密码登录
     * @param request
     * @return token
     */
    @PostMapping("/login")
    fun login(@RequestBody loginInfo: UserDto, request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Void> {
        val subject = SecurityUtils.getSubject()
        try {
            val token = UsernamePasswordToken(loginInfo.username, loginInfo.password)
            subject.login(token)

            val user = subject.principal as UserDto
            val newToken = userService.generateJwtToken(user.username)
            response.setHeader("x-auth-token", newToken)

            return ResponseEntity.ok().build()
        } catch (e: AuthenticationException) {
            //logger.error("User {} login fail, Reason:{}", loginInfo.username, e.message)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    }

    /**
     * 退出登录
     * @return
     */
    @GetMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        val subject = SecurityUtils.getSubject()
        if (subject.principals != null) {
            val user = subject.principals.primaryPrincipal as UserDto
            userService.deleteLoginInfo(user.username)
        }
        SecurityUtils.getSubject().logout()
        return ResponseEntity.ok().build()
    }

}