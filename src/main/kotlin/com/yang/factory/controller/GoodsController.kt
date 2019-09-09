package com.yang.factory.controller

import com.yang.factory.dto.GoodsDto
import com.yang.factory.entity.Goods
import com.yang.factory.service.GoodsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * auther:yyy
 * date:2019/9/4-15:07
 * project:factory
 */
@RestController
@RequestMapping("/goods")
class GoodsController {

    @Autowired
    lateinit var goodsService: GoodsService

    /**
     * 分页查询列表
     */
    @GetMapping("/page")
    fun queryGoodsPage(@RequestBody goodsParams: GoodsDto): ResponseEntity<Page<Goods>> {
        val page = goodsService.queryGoodsPage(goodsParams)
        return ResponseEntity.ok(page)
    }

    /**
     * 添加物品
     */
    @PostMapping
    fun addGoods(@RequestBody goodsDto: GoodsDto): ResponseEntity<Goods> {
        val goodsEntity = goodsService.addGoods(goodsDto)
        return ResponseEntity.ok(goodsEntity)
    }

    /**
     * 上传图片
     */
    @PostMapping("/img")
    fun uploadGoodsImg(file: MultipartFile, goodsId: String): ResponseEntity<Void> {
        goodsService.uploadGoodsImg(file, goodsId)
        return ResponseEntity.ok().build()
    }
}