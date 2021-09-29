package com.idic.utilmoudle

import android.app.Application
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.blankj.utilcode.util.Utils
import com.idic.basecommon.BaseApp

/**
 * 文 件 名: UtilsApp
 * 创 建 人: sineom
 * 创建日期: 2019-08-21 16:30
 * 修改时间：
 * 修改备注：
 */

class UtilsApp : BaseApp() {

    override fun initModuleApp(application: Application) {
        Utils.init(application)
    }

    override fun initModuleData(application: Application) {
        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
        DKToastUtils.setGravity(Gravity.CENTER, 0, 0)
    }

}