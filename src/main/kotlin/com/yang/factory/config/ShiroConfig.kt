package com.yang.factory.config

import com.yang.factory.security.AnyRolesAuthorizationFilter
import com.yang.factory.security.DbShiroRealm
import com.yang.factory.security.JWTShiroRealm
import com.yang.factory.security.JwtAuthFilter
import com.yang.factory.service.UserService
import org.apache.shiro.authc.Authenticator
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition
import org.apache.shiro.spring.web.ShiroFilterFactoryBean
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator
import org.apache.shiro.mgt.SessionStorageEvaluator
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy
import org.apache.shiro.authc.pam.ModularRealmAuthenticator
import org.apache.shiro.realm.Realm
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*
import javax.servlet.DispatcherType
import javax.servlet.Filter
import org.apache.shiro.mgt.SecurityManager


/**
 * auther:yyy
 * date:2019/9/6-9:38
 * project:factory
 */
@Configuration
class ShiroConfig {

    /**
     * 注册shiro的Filter，拦截请求
     */
    @Bean
    @Throws(Exception::class)
    fun filterRegistrationBean(securityManager: SecurityManager, userService: UserService): FilterRegistrationBean<Filter> {
        val filterRegistration = FilterRegistrationBean<Filter>()
        filterRegistration.setFilter((shiroFilter(securityManager, userService).getObject() as Filter?)!!)
        filterRegistration.addInitParameter("targetFilterLifecycle", "true")
        filterRegistration.setAsyncSupported(true)
        filterRegistration.setEnabled(true)
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST)

        return filterRegistration
    }

    /**
     * 初始化Authenticator
     */
    @Bean
    fun authenticator(userService: UserService): Authenticator {
        val authenticator = ModularRealmAuthenticator()
        //设置两个Realm，一个用于用户登录验证和访问权限获取；一个用于jwt token的认证
        authenticator.setRealms(Arrays.asList(jwtShiroRealm(userService), dbShiroRealm(userService)))
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.authenticationStrategy = FirstSuccessfulStrategy()
        return authenticator
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，如果要完全禁用，要配合下面的noSessionCreation的Filter来实现
     */
    @Bean
    protected fun sessionStorageEvaluator(): SessionStorageEvaluator {
        val sessionStorageEvaluator = DefaultWebSessionStorageEvaluator()
        sessionStorageEvaluator.isSessionStorageEnabled = false
        return sessionStorageEvaluator
    }

    /**
     * 用于用户名密码登录时认证的realm
     */
    @Bean("dbRealm")
    fun dbShiroRealm(userService: UserService): Realm {
        return DbShiroRealm(userService)
    }

    /**
     * 用于JWT token认证的realm
     */
    @Bean("jwtRealm")
    fun jwtShiroRealm(userService: UserService): Realm {
        return JWTShiroRealm(userService)
    }

    /**
     * 设置过滤器，将自定义的Filter加入
     */
    @Bean("shiroFilter")
    fun shiroFilter(securityManager: SecurityManager, userService: UserService): ShiroFilterFactoryBean {
        val factoryBean = ShiroFilterFactoryBean()
        factoryBean.setSecurityManager(securityManager)
        val filterMap = factoryBean.filters
        filterMap["authcToken"] = createAuthFilter(userService)
        filterMap["anyRole"] = createRolesFilter()
        factoryBean.filters = filterMap
        factoryBean.filterChainDefinitionMap = shiroFilterChainDefinition().filterChainMap

        return factoryBean
    }

    @Bean
    protected fun shiroFilterChainDefinition(): ShiroFilterChainDefinition {
        val chainDefinition = DefaultShiroFilterChainDefinition()
        chainDefinition.addPathDefinition("/login", "noSessionCreation,anon")  //login不做认证，noSessionCreation的作用是用户在操作session时会抛异常
        chainDefinition.addPathDefinition("/logout", "noSessionCreation,authcToken[permissive]") //做用户认证，permissive参数的作用是当token无效时也允许请求访问，不会返回鉴权未通过的错误
        chainDefinition.addPathDefinition("/image/**", "anon")
        chainDefinition.addPathDefinition("/admin/**", "noSessionCreation,authcToken,anyRole[admin,manager]") //只允许admin或manager角色的用户访问
        chainDefinition.addPathDefinition("/goods/**", "noSessionCreation,authcToken")
        chainDefinition.addPathDefinition("/**", "noSessionCreation,authcToken") // 默认进行用户鉴权
        return chainDefinition
    }

    //注意不要加@Bean注解，不然spring会自动注册成filter
    protected fun createAuthFilter(userService: UserService): JwtAuthFilter {
        return JwtAuthFilter(userService)
    }

    //注意不要加@Bean注解，不然spring会自动注册成filter
    protected fun createRolesFilter(): AnyRolesAuthorizationFilter {
        return AnyRolesAuthorizationFilter()
    }
}