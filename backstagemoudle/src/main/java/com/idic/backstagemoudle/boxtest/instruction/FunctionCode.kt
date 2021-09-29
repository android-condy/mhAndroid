package com.idic.backstagemoudle.boxtest.instruction

import java.math.BigInteger

/**
 * 文 件 名: FunctionCode
 * 创 建 人: sineom
 * 创建日期: 2020/6/13 12:11
 * 修改时间：
 * 修改备注：
 */
 object FunctionCode {

    val license = byteArrayOf(
        0xA8.toByte(), 0x00.toByte(), 0x09.toByte(), 0x01.toByte(),
        0x01.toByte(), 0x01.toByte(), 0x00.toByte(), 0x01.toByte(),
        0x01.toByte(), 0x00.toByte(), 0x01.toByte(), 0x02.toByte(),
        0xDF.toByte(), 0xFD.toByte(), 0x0D.toByte()
    )

    //开始出货
    val start = byteArrayOf(
        0xa8.toByte(),
        0x00.toByte(),
        0x31.toByte(),
        0x01.toByte(),
        0x04.toByte(),
        0x27.toByte(),
        //内容
        0x00.toByte(),//6
        0x03.toByte(),

        0x01.toByte(),//8
        0x00.toByte(),
        0x01.toByte(),
        //货道编号
        0x01.toByte(),//11
        0x04.toByte(),
        0x00.toByte(),
        0x20.toByte(),

        0x31.toByte(),
        0x35.toByte(),
        0x39.toByte(),
        0x32.toByte(),
        0x30.toByte(),
        0x32.toByte(),
        0x35.toByte(),
        0x37.toByte(),
        0x38.toByte(),
        0x30.toByte(),
        0x39.toByte(),
        0x31.toByte(),
        0x30.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),

        0x02.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x00.toByte(),
        0x19.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //进入维护模式
    val toMaintenanceMode = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x01.toByte(),
        0x12.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x02.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //退出维护模式
    val quitMaintenanceMode = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x01.toByte(),
        0x12.toByte(),

        0x00.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //更新货道
    val updateChannel = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x16.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //全部补满
    val FullFill = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x03.toByte(),
        0x13.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),

        0x01.toByte(),
        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //再次开门
    val openDoorAgain = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x04.toByte(),
        0x2D.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //货道是否可用
    val aisleAvailable = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x02.toByte(),
        0x44.toByte(),
        0x01.toByte(), //货道编号
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //机器故障查询
    val faultQuery = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x2A.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )
    //A80009 01 0111 000101000100 校验值 0D
    var systemReset = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x11.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //查询出货结果
    val queryShipmentResult = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x28.toByte(),

        0x01.toByte(),

        0x04.toByte(),
        0x2A.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x04.toByte(),
        0x00.toByte(),
        0x20.toByte(),//10

        0x31.toByte(),
        0x35.toByte(),
        0x39.toByte(),
        0x32.toByte(),
        0x30.toByte(),
        0x32.toByte(),
        0x35.toByte(),
        0x37.toByte(),
        0x38.toByte(),
        0x30.toByte(),
        0x39.toByte(),
        0x31.toByte(),
        0x30.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //获取货道的总数
    val getChannelCount = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),

        0x01.toByte(),
        0x24.toByte(),

        0x00.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //心跳
    val heartbeat = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),

        0x09.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x02.toByte(),

        0x00.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),

        0x01.toByte(),

        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    val ProductSpecifications = byteArrayOf(
        0xa8.toByte(),
        0x00.toByte(),
        0x32.toByte(),
        0x01.toByte(),
        0x03.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x2a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x0a.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //商品类型设定
    val typeSet = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),

        0x20.toByte(),

        0x01.toByte(),

        0x03.toByte(),

        0x01.toByte(),

        0x00.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x00.toByte(),

        0x18.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x02.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x01.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x03.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //获取所有货道所在的层数
    val getAllAisle = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x02.toByte(),
        0x42.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //所有货道解禁
    val allAisleUse = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x20.toByte(),

        0x01.toByte(),

        0x02.toByte(),
        0x43.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x18.toByte(),

        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),

        0x02.toByte(),
        0x02.toByte(),
        0x02.toByte(),
        0x02.toByte(),

        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),

        0x02.toByte(),
        0x02.toByte(),
        0x02.toByte(),
        0x02.toByte(),

        0x01.toByte(),
        0x01.toByte(),
        0x01.toByte(),

        0x02.toByte(), //01 禁用  02 解禁

        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //回收
    val recycling = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x04.toByte(),
        0x2E.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //系统状态的获取
    val systemStatus = byteArrayOf(
        0xA8.toByte(),
        0x00.toByte(),
        0x09.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x27.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x01.toByte(),
        0x0d.toByte()
    )

    //传感器状态取得
    val sensor = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x01.toByte(),
        0x2D.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),

        0x01.toByte(),
        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x0d.toByte()
    )


    //查询货物是否取走
    val query_get_good = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x04.toByte(),
        0x30.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),

        0x01.toByte(),
        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x0d.toByte()
    )

    //查询回收
    val query_recycling = byteArrayOf(
        0xA8.toByte(),

        0x00.toByte(),
        0x09.toByte(),

        0x01.toByte(),

        0x04.toByte(),
        0x2F.toByte(),

        0x00.toByte(),
        0x01.toByte(),
        0x01.toByte(),
        0x00.toByte(),

        0x01.toByte(),
        0x00.toByte(),

        0x00.toByte(),
        0x01.toByte(),

        0x0d.toByte()
    )
}