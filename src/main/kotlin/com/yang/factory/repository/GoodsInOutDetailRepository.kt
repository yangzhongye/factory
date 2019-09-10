package com.yang.factory.repository

import com.yang.factory.entity.GoodsInOutDetail
import org.springframework.data.jpa.repository.JpaRepository

/**
 * auther:yyy
 * date:2019/9/10-11:11
 * project:factory
 */
interface GoodsInOutDetailRepository: JpaRepository<GoodsInOutDetail, String>