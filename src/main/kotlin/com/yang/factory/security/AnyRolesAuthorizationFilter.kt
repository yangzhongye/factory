package com.yang.factory.security

import java.io.IOException
import javax.servlet.ServletResponse
import javax.servlet.ServletRequest
import org.apache.commons.lang3.BooleanUtils
import org.apache.http.HttpStatus
import org.apache.shiro.web.filter.authz.AuthorizationFilter
import org.apache.shiro.web.util.WebUtils


/**
 * auther:yyy
 * date:2019/9/6-11:32
 * project:factory
 */
class AnyRolesAuthorizationFilter : AuthorizationFilter() {

    override fun postHandle(request: ServletRequest?, response: ServletResponse?) {
        request!!.setAttribute("anyRolesAuthFilter.FILTERED", true)
    }

    @Throws(Exception::class)
    override fun isAccessAllowed(servletRequest: ServletRequest, servletResponse: ServletResponse, mappedValue: Any): Boolean {
        val afterFiltered = servletRequest.getAttribute("anyRolesAuthFilter.FILTERED") as Boolean
        if (BooleanUtils.isTrue(afterFiltered))
            return true

        val subject = getSubject(servletRequest, servletResponse)
        val rolesArray = mappedValue as Array<String>
        if (rolesArray == null || rolesArray.size == 0) { //没有角色限制，有权限访问
            return true
        }
        for (role in rolesArray) {
            if (subject.hasRole(role))
            //若当前用户是rolesArray中的任何一个，则有权限访问
                return true
        }
        return false
    }

    @Throws(IOException::class)
    override fun onAccessDenied(request: ServletRequest, response: ServletResponse): Boolean {
        val httpResponse = WebUtils.toHttp(response)
        httpResponse.setCharacterEncoding("UTF-8")
        httpResponse.setContentType("application/json;charset=utf-8")
        httpResponse.setStatus(HttpStatus.SC_UNAUTHORIZED)
        return false
    }

}