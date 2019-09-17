package com.yang.factory

import com.yang.factory.security.JwtUtils
import org.apache.shiro.crypto.hash.Sha256Hash
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class FactoryApplicationTests {

    @Test
    fun generatePwd() {
        val pwd = Sha256Hash("123456", "69f952f6-fcd3-46f7-aae7-00f23e2363e0").toHex()
        println(pwd)

        val salt = JwtUtils.generateSalt()
        println(salt)
    }
}
