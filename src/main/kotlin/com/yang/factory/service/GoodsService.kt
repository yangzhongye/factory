package com.yang.factory.service

import com.yang.factory.dto.GoodsDto
import com.yang.factory.entity.Goods
import com.yang.factory.entity.GoodsInOutDetail
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
    fun updateGoods(goodsDto: GoodsDto)
    fun modifyQuantity(goodsDto: GoodsDto)
    fun deleteGoods(goodsId: String)
    fun queryGoodsInOutDetail(goodsId: String): List<GoodsInOutDetail>
    fun uploadGoodsImg(file: MultipartFile, goodsId: String)
    fun queryGoodsImgIdList(goodsId: String): List<String?>
    fun queryGoodsImgContent(imgId: String): ByteArray
    fun deleteGoodsImg(imgId: String)
}