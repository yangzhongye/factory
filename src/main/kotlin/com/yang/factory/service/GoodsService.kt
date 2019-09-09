package com.yang.factory.service

import com.yang.factory.dto.GoodsDto
import com.yang.factory.entity.Goods
import org.springframework.data.domain.Page
import org.springframework.web.multipart.MultipartFile

/**
 * auther:yyy
 * date:2019/9/9-10:38
 * project:factory
 */
interface GoodsService {

    fun queryGoodsPage(goodsParams: GoodsDto): Page<Goods>
    fun addGoods(goodsDto: GoodsDto): Goods
    fun uploadGoodsImg(file: MultipartFile, goodsId: String)
}