package com.idic.backstagemoudle.data

import com.idic.backstagemoudle.db.entity.AisleConfigEntity
import com.idic.backstagemoudle.db.entity.ContainerConfigEntity

/**
 * 文 件 名: ContainerConfigDataSource
 * 创 建 人: sineom
 * 创建日期: 2019-08-22 09:55
 * 修改时间：
 * 修改备注：
 */

interface ContainerConfigDataSource {

    interface GetContainers {
        //有数据
        fun onContainersLoad(containers: List<ContainerConfigEntity>)

        //无数据
        fun onContainersNotAvaliable()
    }

    interface GetAisles {
        //有数据
        fun onAislesLoad(containers: List<AisleConfigEntity>)

        //无数据
        fun onAislesNotAvaliable()
    }
}