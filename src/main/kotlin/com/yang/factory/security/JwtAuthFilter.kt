package com.yang.factory.security

import java.time.ZoneId
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import org.apache.shiro.authc.AuthenticationToken
import com.yang.factory.dto.UserDto
import com.yang.factory.service.UserService
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpStatus
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.subject.Subject
import org.springframework.web.bind.annotation.RequestMethod
import org.apache.shiro.web.filter.authc.AuthenticatingFilter
import org.apache.shiro.web.util.WebUtils
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*


/**
 * auther:yyy
 * date:2019/9/6-11:00
 * project:factory
 */
class JwtAuthFilter(private val userService: UserService) : AuthenticatingFilter() {

    init {
        this.loginUrl = "/login"
    }

    @Throws(Exception::class)
    override fun preHandle(request: ServletRequest?, response: ServletResponse?): Boolean {
        val httpServletRequest = WebUtils.toHttp(request)
        return if (httpServletRequest.getMethod() == RequestMethod.OPTIONS.name) false else super.preHandle(request, response)

    }

    override fun postHandle(request: ServletRequest?, response: ServletResponse?) {
       // this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response))
        request!!.setAttribute("jwtShiroFilter.FILTERED", true)
    }

    override fun isAccessAllowed(request: ServletRequest, response: ServletResponse, mappedValue: Any?): Boolean {
        if (this.isLoginRequest(request, response))
            return true
        val afterFiltered = request.getAttribute("jwtShiroFilter.FILTERED") as Boolean?
        if (BooleanUtils.isTrue(afterFiltered))
            return true

        var allowed = false
        try {
            allowed = executeLogin(request, response)
        } catch (e: IllegalStateException) { //not found any token
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return allowed || super.isPermissive(mappedValue)
    }

    override fun createToken(servletRequest: ServletRequest, servletResponse: ServletResponse): AuthenticationToken? {
        val jwtToken = getAuthzHeader(servletRequest)
        return if (StringUtils.isNotBlank(jwtToken) && !JwtUtils.isTokenExpired(jwtToken)) JWTToken(jwtToken) else throw RuntimeException("token is null")
    }

    @Throws(Exception::class)
    override fun onAccessDenied(servletRequest: ServletRequest, servletResponse: ServletResponse): Boolean {
        val httpResponse = WebUtils.toHttp(servletResponse)
        httpResponse.setCharacterEncoding("UTF-8")
        httpResponse.setContentType("application/json;charset=UTF-8")
        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION)
        //fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse)
        return false
    }

    @Throws(Exception::class)
    override fun onLoginSuccess(token: AuthenticationToken, subject: Subject, request: ServletRequest, response: ServletResponse): Boolean {
        val httpResponse = WebUtils.toHttp(response)
        var newToken: String? = null
        if (token is JWTToken) {
            val user = subject.principal as UserDto
            val shouldRefresh = shouldTokenRefresh(JwtUtils.getIssuedAt(token.token))
            if (shouldRefresh) {
                newToken = userService.generateJwtToken(user.username)
            }
        }
        if (StringUtils.isNotBlank(newToken))
            httpResponse.setHeader("x-auth-token", newToken)

        return true
    }

    override fun onLoginFailure(token: AuthenticationToken, e: AuthenticationException, request: ServletRequest, response: ServletResponse): Boolean {
        //log.error("Validate token fail, token:{}, error:{}", token.toString(), e.message)
        e.printStackTrace()
        return false
    }

    private fun getAuthzHeader(request: ServletRequest): String {
        val httpRequest = WebUtils.toHttp(request)
        val header = httpRequest.getHeader("x-auth-token") ?: ""
        return StringUtils.removeStart(header, "Bearer ")
    }

    private fun shouldTokenRefresh(issueAt: Date?): Boolean {
        if(issueAt == null)
            return true
        val issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault())
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval.toLong()).isAfter(issueTime)
    }

    /*protected fun fillCorsHeader(httpServletRequest: HttpServletRequest, httpServletResponse: HttpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"))
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD")
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"))
    }*/

    companion object {

        private val tokenRefreshInterval = 300
    }
}