package com.yang.factory.service.impl

import com.yang.factory.repository.GoodsRepository
import com.yang.factory.dto.GoodsDto
import com.yang.factory.dto.UserDto
import com.yang.factory.entity.Goods
import com.yang.factory.entity.GoodsImg
import com.yang.factory.repository.GoodsImgRepository
import com.yang.factory.service.GoodsService
import org.apache.commons.lang3.StringUtils
import org.apache.shiro.SecurityUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
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
    override fun uploadGoodsImg(file: MultipartFile, goodsId: String) {
        val goodsImgEntity = GoodsImg(UUID.randomUUID().toString(), goodsId, file.bytes, file.originalFilename)
        goodsImgRepository.save(goodsImgEntity)
    }
}