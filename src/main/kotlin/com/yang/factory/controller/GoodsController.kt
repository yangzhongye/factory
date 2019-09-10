package com.yang.factory.controller

import com.yang.factory.dto.GoodsDto
import com.yang.factory.entity.Goods
import com.yang.factory.entity.GoodsInOutDetail
import com.yang.factory.service.GoodsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

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
     * 修改物品
     */
    @PutMapping
    fun updateGoods(@RequestBody goodsDto: GoodsDto): ResponseEntity<Void> {
        goodsService.updateGoods(goodsDto)
        return ResponseEntity.ok().build()
    }

    /**
     * 调整数量
     */
    @PostMapping("/quantity")
    fun modifyQuantity(@RequestBody goodsDto: GoodsDto): ResponseEntity<Void> {
        goodsService.modifyQuantity(goodsDto)
        return ResponseEntity.ok().build()
    }

    /**
     * 删除物品
     */
    @DeleteMapping("/{goodsId}")
    fun deleteGoods(@PathVariable goodsId: String): ResponseEntity<Void> {
        goodsService.deleteGoods(goodsId)
        return ResponseEntity.ok().build()
    }

    /**
     * 查询物品进出详情
     */
    @GetMapping("/inOutDetail/{goodsId}")
    fun queryGoodsInOutDetail(@PathVariable goodsId: String): ResponseEntity<List<GoodsInOutDetail>> {
        val goodsInOutDetailList = goodsService.queryGoodsInOutDetail(goodsId)
        return ResponseEntity.ok(goodsInOutDetailList)
    }

    /**
     * 上传图片
     */
    @PostMapping("/img")
    fun uploadGoodsImg(file: MultipartFile, goodsId: String): ResponseEntity<Void> {
        goodsService.uploadGoodsImg(file, goodsId)
        return ResponseEntity.ok().build()
    }

    /**
     * 通过goodsId查询对应图片id列表
     */
    @GetMapping("/img/list/{goodsId}")
    fun queryGoodsImgIdList(@PathVariable goodsId: String): ResponseEntity<List<String?>> {
        val imgIdList = goodsService.queryGoodsImgIdList(goodsId)
        return ResponseEntity.ok(imgIdList)
    }

    /**
     * 通过imgId查询返回图片内容
     */
    @GetMapping("/img/content/{imgId}")
    fun queryGoodsImgContent(@PathVariable imgId: String, response: HttpServletResponse) {
        val imgContent = goodsService.queryGoodsImgContent(imgId)
        val out = response.outputStream
        out.write(imgContent)
        out.flush()
        out.close()
    }

    /**
     * 删除图片
     */
    @DeleteMapping("/img/{imgId}")
    fun deleteGoodsImg(@PathVariable imgId: String): ResponseEntity<Void> {
        goodsService.deleteGoodsImg(imgId)
        return ResponseEntity.ok().build()
    }
}