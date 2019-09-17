package com.yang.factory.repository

import com.yang.factory.entity.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * auther:yyy
 * date:2019/9/17-10:31
 * project:factory
 */
interface UserRepository: JpaRepository<User, String>