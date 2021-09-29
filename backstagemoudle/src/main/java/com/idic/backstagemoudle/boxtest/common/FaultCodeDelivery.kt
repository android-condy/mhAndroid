package com.idic.backstagemoudle.boxtest.common

/**
 * 文 件 名: FaultCodeDelivery
 * 创 建 人: sineom
 * 创建日期: 2020/6/19 14:35
 * 修改时间：
 * 修改备注：
 */
val outage = HashMap<Int, String>().apply {
    put(0x01.toByte() + 0xE0.toByte(), "X 轴无刷电机动作执行超时")
    put(0x01.toByte() + 0xE1.toByte(), "X 轴无刷电机过压故障")
    put(0x01.toByte() + 0xE2.toByte(), "X 轴无刷电机欠压故障")
    put(0x01.toByte() + 0xE3.toByte(), "X 轴无刷电机过热故障")
    put(0x01.toByte() + 0xE4.toByte(), "X 轴无刷电机过流故障")
    put(0x01.toByte() + 0xE5.toByte(), "X 轴无刷电机参数保存故障")
    put(0x01.toByte() + 0xE6.toByte(), "X 轴无刷电机霍尔线插接故障")
    put(0x01.toByte() + 0xE7.toByte(), "X 轴无刷电机运行中霍尔换相 故障")
    put(0x01.toByte() + 0xE8.toByte(), "X 轴无刷电机过载")
    put(0x01.toByte() + 0xE9.toByte(), "X 轴原点传感器错误")

    put(0x02.toByte() + 0x02.toByte(), "主控板与子板(Y 轴)指令参数长 度不符")
    put(0x02.toByte() + 0x04.toByte(), "主控板发送的指令,子板(Y 轴)不 支持")
    put(0x02.toByte() + 0x06.toByte(), "主控板与子板(Y 轴)间的通讯超 时")
    put(0x02.toByte() + 0x07.toByte(), "主控板发送到子板(Y 轴)的指令数据域参数异常")
    put(0x02.toByte() + 0xE0.toByte(), "Y 轴无刷电机动作执行超时")
    put(0x02.toByte() + 0xE1.toByte(), "Y 轴无刷电机过压故障")
    put(0x02.toByte() + 0xE2.toByte(), "Y 轴无刷电机欠压故障")
    put(0x02.toByte() + 0xE3.toByte(), "Y 轴无刷电机过热故障")
    put(0x02.toByte() + 0xE4.toByte(), "Y 轴无刷电机过流故障")
    put(0x02.toByte() + 0xE5.toByte(), "Y 轴无刷电机参数保存故障")
    put(0x02.toByte() + 0xE6.toByte(), "Y 轴无刷电机霍尔线插接故障")
    put(0x02.toByte() + 0xE7.toByte(), "Y 轴无刷电机运行中霍尔换相故障")
    put(0x02.toByte() + 0xE8.toByte(), "Y 轴无刷电机过载")
    put(0x02.toByte() + 0xE9.toByte(), "Y 轴原点传感器错误")

    put(0x03.toByte() + 0x02.toByte(), "主控板与子板(取货斗)指令参数长度不符")
    put(0x03.toByte() + 0x04.toByte(), "主控板发送的指令,子板(取货 斗)不支持")
    put(0x03.toByte() + 0x05.toByte(), "主控板发送到子板(取货斗)的指 令头尾或者地址异常")
    put(0x03.toByte() + 0x06.toByte(), "主控板与子板(取货斗)间的通讯 超时")
    put(0x03.toByte() + 0x07.toByte(), "主控板发送到子板(取货斗)的指 令数据域参数异常")
    put(0x03.toByte() + 0xA0.toByte(), "取货斗入口传感器被遮挡(取货前)")
    put(0x03.toByte() + 0xA1.toByte(), "啮合齿轮超时(未离位)")
    put(0x03.toByte() + 0xA4.toByte(), "啮合齿轮超时(未归位)")

    put(0x03.toByte() + 0xB8.toByte(), "啮合电机过载，无法转动")
    put(0x03.toByte() + 0xB9.toByte(), "三角辊电机过载，无法转动")
    put(0x03.toByte() + 0xBA.toByte(), "啮合齿轮超时(未归位)")

    put(0x03.toByte() + 0xAB.toByte(), "翻斗打开超时(未离位)")
    put(0x03.toByte() + 0xAC.toByte(), "翻斗打开超时(未归位)")
    put(0x03.toByte() + 0xAD.toByte(), "翻斗关闭超时(未离位)")

    put(0x03.toByte() + 0xF7.toByte(), "翻斗关闭超时(光幕遮挡)")
    put(0x03.toByte() + 0xAE.toByte(), "翻斗关闭超时(未归位)")
    put(0x03.toByte() + 0xBC.toByte(), "激光测距模块异常")
    put(0x03.toByte() + 0xC0.toByte(), "货斗传感电压异常")

//    put(0x00.toByte() + 0x30.toByte(), "顶部灯箱坏")
//    put(0x00.toByte() + 0x31.toByte(), "展示仓灯条 1 坏")
//    put(0x00.toByte() + 0x32.toByte(), "展示仓灯条 2 坏")
//    put(0x00.toByte() + 0x33.toByte(), "出货口指示灯坏")
    put(0x00.toByte() + 0x3F.toByte(), "上电自检时，光幕传感器电压异常")
    put(0x00.toByte() + 0x40.toByte(), "闸门打开超时(未离位)")
    put(0x00.toByte() + 0x41.toByte(), "闸门打开超时(未到位)")
    put(0x00.toByte() + 0x42.toByte(), "闸门打开过流")
    put(0x00.toByte() + 0x43.toByte(), "闸门关闭超时(未离位)")
    put(0x00.toByte() + 0x44.toByte(), "闸门关闭超时(未到位)")
    put(0x00.toByte() + 0x45.toByte(), "闸门关闭过流")
    put(0x00.toByte() + 0x46.toByte(), "闸门关闭超时(光幕遮挡)")

    put(0x00.toByte() + 0x50.toByte(), "闸门销未安装")
    put(0x00.toByte() + 0x47.toByte(), "货斗销未安装")

    put(0x00.toByte() + 0xFA.toByte(), "货斗有货物")
//    put(0x00.toByte() + 0xFB.toByte(), "主柜门未关闭")
//    put(0x00.toByte() + 0xD0.toByte(), "所有层均异常")

//    put(0x40.toByte() + 0xFA.toByte(), "无该货道")
//    put(0x40.toByte() + 0x20.toByte(), "除霜温度传感器异常")
//    put(0x40.toByte() + 0x21.toByte(), "箱体温度传感器异常")
//    put(0x40.toByte() + 0x22.toByte(), "环境湿度传感器异常")
//    put(0x40.toByte() + 0x23.toByte(), "环境温度传感器异常")
//    put(0x40.toByte() + 0x30.toByte(), "压机组工作异常")
//    put(0x40.toByte() + 0x31.toByte(), "加热管工作异常")
}


//出货错误码
val faultCodeDelivery = HashMap<Byte, String>().apply {
    put(0xA2.toByte(), "空货道")
    put(0xFD.toByte(), "坏货道")
    put(0x08.toByte(), "子板(X 轴)通讯失败")
    put(0x09.toByte(), "子板(X 轴)电机故障")
    put(0x11.toByte(), "子板(Y 轴)通讯失败")
    put(0x12.toByte(), "子板(X 轴)电机故障")
    put(0x14.toByte(), "子板(取货斗)通讯失败")
    put(0x18.toByte(), "子板(电源板)通讯失败")
    put(0xA0.toByte(), "取货斗入口传感器被遮挡(取货前)")
    put(0xA1.toByte(), "啮合齿轮超时(未离位)")
    put(0xA4.toByte(), "啮合齿轮超时(未归位)")
    put(0xB8.toByte(), "啮合电机过载，无法转动")
    put(0xB9.toByte(), "三角辊电机过载，无法转动")
    put(0xBA.toByte(), "翻板电机过载，无法转动")
    put(0xAB.toByte(), "翻斗打开超时(未离位)")
    put(0xAC.toByte(), "翻斗打开超时(未归位")
    put(0xAD.toByte(), "翻斗关闭超时(未离位)")
    put(0xAE.toByte(), "翻斗关闭超时(未归位)")
    put(0xF7.toByte(), "翻斗关闭超时(光幕遮挡)")
    put(0xC0.toByte(), "货斗传感器电压异常")
    put(0x3F.toByte(), "光幕传感器电压异常")
    put(0x40.toByte(), "闸门打开超时(未离位)")
    put(0x41.toByte(), "闸门打开超时(未到位)")
    put(0x42.toByte(), "闸门打开过流")
    put(0x43.toByte(), "闸门关闭超时(未离位)")
    put(0x44.toByte(), "闸门关闭超时(未到位)")
    put(0x45.toByte(), "闸门关闭过流")
    put(0x46.toByte(), "闸门关闭超时(光幕遮挡)")
    put(0xFB.toByte(), "主柜门未关闭")
    put(0xFC.toByte(), "副柜门未关闭")
}