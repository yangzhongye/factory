package com.yang.factory.repository

import com.yang.factory.entity.Goods
import org.springframework.data.jpa.repository.JpaRepository

/**
 * auther:yyy
 * date:2019/9/9-10:05
 * project:factory
 */
interface GoodsRepository: JpaRepository<Goods, String>