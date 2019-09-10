package com.yang.factory.service

import org.apache.shiro.crypto.hash.Sha256Hash
import com.yang.factory.dto.UserDto
import com.yang.factory.security.JwtUtils
import org.springframework.stereotype.Service


/**
 * auther:yyy
 * date:2019/9/6-11:29
 * project:factory
 */
/**
 * 用户信息接口
 */
@Service
class UserService {

    /*@Autowired
    private val redisTemplate: StringRedisTemplate? = null*/

    /**
     * 保存user登录信息，返回token
     * @param userDto
     */
    fun generateJwtToken(username: String): String? {
        val salt = "12345"//JwtUtils.generateSalt();
        /**
         * @todo 将salt保存到数据库或者缓存中
         * redisTemplate.opsForValue().set("token:"+username, salt, 3600, TimeUnit.SECONDS);
         */
        return JwtUtils.sign(username, salt, 3600) //生成jwt token，设置过期时间为1小时
    }

    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    fun getJwtTokenInfo(username: String): UserDto? {
        val salt = "12345"
        /**
         * @todo 从数据库或者缓存中取出jwt token生成时用的salt
         * salt = redisTemplate.opsForValue().get("token:"+username);
         */
        val user = getUserInfo(username)
        user?.salt = salt
        return user
    }

    /**
     * 清除token信息
     * @param userName 登录用户名
     * @param terminal 登录终端
     */
    fun deleteLoginInfo(username: String) {
        /**
         * @todo 删除数据库或者缓存中保存的salt
         * redisTemplate.delete("token:"+username);
         */

    }

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    fun getUserInfo(userName: String): UserDto? {
        val user = UserDto()
        user.userId = "1"
        user.username = "admin"
        //userId 做盐粒
        user.encryptPwd = Sha256Hash("123456", user.userId).toHex()
        return user
    }

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    fun getUserRoles(userId: String?): List<String> {
        return listOf("admin")
    }

    /*companion object {

        private val encryptSalt = "F12839WhsnnEV$#23b"
    }*/

}