package com.yang.factory.service.impl

import com.yang.factory.repository.GoodsRepository
import com.yang.factory.dto.GoodsDto
import com.yang.factory.dto.UserDto
import com.yang.factory.entity.Goods
import com.yang.factory.entity.GoodsImg
import com.yang.factory.entity.GoodsInOutDetail
import com.yang.factory.repository.GoodsImgRepository
import com.yang.factory.repository.GoodsInOutDetailRepository
import com.yang.factory.service.GoodsService
import org.apache.commons.lang3.StringUtils
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.lang.RuntimeException
import java.math.BigDecimal
import java.util.*

/**
 * auther:yyy
 * date:2019/9/9-10:00
 * project:factory
 */
@Service
class GoodsServiceImpl: GoodsService {

    @Autowired
    lateinit var goodsRepository: GoodsRepository
    @Autowired
    lateinit var goodsImgRepository: GoodsImgRepository
    @Autowired
    lateinit var goodsInOutDetailRepository: GoodsInOutDetailRepository

    override fun queryGoodsPage(goodsParams: GoodsDto): Page<Goods> {
        val goods = Goods(null)
        goods.seqNo = goodsParams.seqNo
        goods.number = if(StringUtils.isBlank(goodsParams.number)) null else goodsParams.number
        goods.name = if(StringUtils.isBlank(goodsParams.name)) null else goodsParams.name
        val matcher = ExampleMatcher.matching()
                .withMatcher("seqNo", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("number", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase().contains())
        val example = Example.of(goods, matcher)
        val pageable = PageRequest.of(goodsParams.page, goodsParams.pageSize, Sort.Direction.DESC, "seqNo")
        return goodsRepository.findAll(example, pageable)
    }

    @Transactional
    override fun addGoods(goodsDto: GoodsDto): Goods {
        val user = SecurityUtils.getSubject().principal as UserDto
        val goodsEntity = Goods(UUID.randomUUID().toString())
        goodsEntity.number = goodsDto.number
        goodsEntity.name = goodsDto.name
        goodsEntity.quantity = goodsDto.quantity
        goodsEntity.note = goodsDto.note
        goodsEntity.createTime = Date()
        goodsEntity.createUserId = user.userId
        goodsEntity.createUserName = user.username
        goodsRepository.save(goodsEntity)
        return goodsEntity
    }

    @Transactional
    override fun updateGoods(goodsDto: GoodsDto) {
        val user = SecurityUtils.getSubject().principal as UserDto
        val goodsOptional = goodsRepository.findById(goodsDto.id ?: throw RuntimeException("Id can't be null"))
        goodsOptional.orElseThrow { RuntimeException("GoodsInfo not found") }
        goodsOptional
                .ifPresent {
                    it.name = goodsDto.name
                    it.number = goodsDto.number
                    it.note = goodsDto.note
                    it.updateTime = Date()
                    it.updateUserId = user.userId
                    it.updateUserName = user.username
                    goodsRepository.save(it)
                }
    }

    @Transactional
    override fun modifyQuantity(goodsDto: GoodsDto) {
        val changeQuantity = goodsDto.quantity ?: BigDecimal.ZERO
        val user = SecurityUtils.getSubject().principal as UserDto
        val goodsOptional = goodsRepository.findById(goodsDto.id ?: throw RuntimeException("Id can't be null"))
        goodsOptional.orElseThrow { RuntimeException("GoodsInfo not found") }
        goodsOptional
                .ifPresent{
                    //修改更新数量
                    it.quantity = it.quantity?.add(changeQuantity) ?: changeQuantity
                    goodsRepository.save(it)

                    //插入详情
                    val goodsInOutDetail = GoodsInOutDetail(UUID.randomUUID().toString())
                    goodsInOutDetail.goodsId = it.id
                    goodsInOutDetail.type = if(changeQuantity >= BigDecimal.ZERO) "1" else "0"
                    goodsInOutDetail.changeQuantity = changeQuantity.abs()
                    goodsInOutDetail.currentQuantity = it.quantity
                    goodsInOutDetail.createTime = Date()
                    goodsInOutDetail.operUserId = user.userId
                    goodsInOutDetail.operUserName = user.username
                    goodsInOutDetailRepository.save(goodsInOutDetail)
                }
    }

    @Transactional
    override fun deleteGoods(goodsId: String) {
        val goodsOptional = goodsRepository.findById(goodsId)
        goodsOptional.orElseThrow { RuntimeException("GoodsInfo not found") }
        goodsOptional.ifPresent { goodsRepository.deleteById(goodsId) }
    }

    override fun queryGoodsInOutDetail(goodsId: String): List<GoodsInOutDetail> {
        val goodsInOutDetail = GoodsInOutDetail(null)
        goodsInOutDetail.goodsId = goodsId
        val goodsInOutDetailEx = Example.of(goodsInOutDetail)
        return goodsInOutDetailRepository.findAll(goodsInOutDetailEx)
    }

    @Transactional
    override fun uploadGoodsImg(file: MultipartFile, goodsId: String) {
        val goodsImgEntity = GoodsImg(UUID.randomUUID().toString(), goodsId, file.bytes, file.originalFilename)
        goodsImgRepository.save(goodsImgEntity)
    }

    override fun queryGoodsImgIdList(goodsId: String): List<String?> {
        val goodsImgEx = Example.of(GoodsImg(null, goodsId, null, null))
        return goodsImgRepository.findAll(goodsImgEx).map { it.id }
    }

    override fun queryGoodsImgContent(imgId: String): ByteArray {
        val goodsImgOptional = goodsImgRepository.findById(imgId)
        goodsImgOptional.orElseThrow{ RuntimeException("GoodsImg not found") }
        return goodsImgOptional.map { it.content }.get()
    }

    @Transactional
    override fun deleteGoodsImg(imgId: String) {
        val goodsImgOptional = goodsImgRepository.findById(imgId)
        goodsImgOptional.orElseThrow { RuntimeException("GoodsImg not found") }
        goodsImgOptional.ifPresent { goodsImgRepository.deleteById(imgId) }
    }
}