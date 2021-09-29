package com.idic.datamoudle.download

import java.io.File

/**
 * 文 件 名: IDownload
 * 创 建 人: sineom
 * 创建日期: 2019-10-14 09:36
 * 修改时间：
 * 修改备注：
 */

interface IDownload {
    /**
     * 文件下载
     * @param url 文件下载地址
     * @return true 下载成功,反之失败
     */
    fun downloadFile(url: String, localFile: File): Boolean

    fun downloadFile(url: String, localFile: String): Boolean

    /**
     * 异步调用,回调通知
     */
    fun downloadFileAsync(url: String, localFile: File, listener: ((Boolean) -> Unit))

    fun downloadFileAsync(url: String, localFile: String, listener: ((Boolean) -> Unit))


}