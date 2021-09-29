package com.idic.login.db.dao

import com.idic.login.db.DBHelper
import com.idic.login.db.entity.DeviceInfo

/**
 * 文 件 名: DeviceHelper
 * 创 建 人: sineom
 * 创建日期: 2019/2/1 10:21
 * 修改时间：
 * 修改备注：
 */

object DeviceHelper {



    fun getDevice(): DeviceInfo? {
        return DBHelper.instance.getDeviceBox().query().build().findFirst()
    }

}