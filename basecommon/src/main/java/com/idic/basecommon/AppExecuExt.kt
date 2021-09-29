package com.idic.basecommon

import com.idic.basecommon.AppExecutors

/**
 * 文 件 名: AppExecuExt
 * 创 建 人: sineom
 * 创建日期: 2019-08-07 13:50
 * 修改时间：
 * 修改备注：
 */
//IO线程
fun AppExecutors.transactIO(execu: () -> Unit) {
    diskIO.execute {
        execu()
    }
}

//网络线程执行
fun AppExecutors.transcatNetworkIO(execu: () -> Unit) {
    networkIO.execute {
        execu()
    }
}

//主线程执行
fun AppExecutors.transcatMain(execu: () -> Unit) {
    mainThread.execute {
        execu()
    }
}