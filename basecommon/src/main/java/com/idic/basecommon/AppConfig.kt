package com.idic.basecommon

/**
 * 文 件 名: AppConfig
 * 创 建 人: sineom
 * 创建日期: 2019-06-13 11:53
 * 修改时间：
 * 修改备注：
 */

class AppConfig {

    companion object {


        private val utilsApp = "com.idic.utilmoudle.UtilsApp"
        private val dataApp = "com.idic.datamoudle.DataApp"
        private val loginApp = "com.idic.login.LoginApp"
        private val backStageApp = "com.idic.backstagemoudle.BackstageApp"
        private val BlindboxApp = "com.idic.blindbox.BlindboxApp"

        val moduleApps = ArrayList<String>().apply {
            add(utilsApp)
            add(dataApp)
            add(loginApp)
            add(backStageApp)
            add(BlindboxApp)
        }
    }

}