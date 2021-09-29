package com.idic.backstagemoudle.db

import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity
import java.util.*

/**
 * 文 件 名: DataGenerator
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 16:14
 * 修改时间：
 * 修改备注：
 */

object DataGenerator {

    //获取默认的货道配置数据
    fun generateAisles(containers: List<ContainerConfigEntity>): List<AisleConfigEntity> {
        val aisles = ArrayList<AisleConfigEntity>()
        for (container in containers) {
            (1..100).forEach {
                val num = container.num
                val aisle = AisleConfigEntity(num, it)
                aisles.add(aisle)
            }
        }
        return aisles
    }

    //获取默认的货柜配置
    fun generateContainers(): List<ContainerConfigEntity> {
        val containers = ArrayList<ContainerConfigEntity>()

        (1..10).forEach {
            val container = ContainerConfigEntity(it, containerName = "$it 号货柜")
            containers.add(container)
        }

        return containers
    }
}