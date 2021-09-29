package com.idic.backstagemoudle.db.entity

import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.os.Parcelable
import com.idic.basecommon.utils.MotorType
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * 文 件 名: AisleEntity
 * 创 建 人: sineom
 * 创建日期: 2019-08-06 14:29
 * 修改时间：
 * 修改备注： 货道设置
 */


@Entity(
    foreignKeys = [
        ForeignKey(//关联产品表
            entity = ContainerConfigEntity::class,
            parentColumns = ["num"],
            childColumns = ["num"],
            onDelete = CASCADE
        )], indices = [Index("num", "channelNumber")]
)
@Parcelize
data class AisleConfigEntity(


    //货柜的地址 本地存储配置使用
    var num: Int,

    //货道编号 本地存储配置使用
    var channelNumber: Int,

    //货柜id
    var containerId: String = "",

    //货道id
    var wayId: String = "",

    //lie编号
    val columnSeial: String = "",

    //行编号
    val rowSeial: String = "",

    //行列说明
    val goodsWay: String = "",

    //产品ID
    val productId: String = "",

    //产品名称
    val productName: String = "",

    //库存
    var stock: Int = 0,

    //锁定库存
    var lockStock: Int = 0,

    //状态
    var status: String = "",

    //容量
    var capacity: Int = 0,

    //价格
    var productPrice: String = "",

    //电机类型
    var motorType: MotorType = MotorType.THREE_WIRE_MOTOR,

    @PrimaryKey
    @ColumnInfo(name = "entityId")
    var id: String = UUID.randomUUID().toString(),
    //记录选择的电机类型
    var tempMotorType: MotorType = MotorType.THREE_WIRE_MOTOR


) : Parcelable {
    //修改的库存变量
    var tempStock: Int = 0

    //修改的锁定库存变量
    var tempLockStock: Int = 0

    //修改的货道容量
    var tempCapacity: Int = 0
}