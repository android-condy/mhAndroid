package com.idic.basecommon.service

import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.OSS
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.OSSLog
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest
import com.alibaba.sdk.android.oss.model.OSSRequest
import com.elvishew.xlog.XLog


/**
 * 文 件 名: OssService
 * 创 建 人: sineom
 * 创建日期: 2019-09-24 09:51
 * 修改时间：
 * 修改备注：
 */

internal class OssService constructor(private var mOss: OSS, private var mBucket: String) {



    fun asyncMultipartUpload(
        uploadKey: String,
        uploadFilePath: String,
        iUploadLog: IUploadLog? = null
    ) {
        val request = MultipartUploadRequest<MultipartUploadRequest<*>>(
            mBucket,
            uploadKey,
            uploadFilePath
        )
        request.crC64 = OSSRequest.CRC64Config.YES
        request.progressCallback =
            OSSProgressCallback<MultipartUploadRequest<*>> { request, currentSize, totalSize ->
                OSSLog.logDebug(
                    "[testMultipartUpload] - $currentSize $totalSize",
                    false
                )
            }
        mOss.asyncMultipartUpload(
            request,
            object :
                OSSCompletedCallback<MultipartUploadRequest<*>, CompleteMultipartUploadResult> {
                override fun onSuccess(
                    request: MultipartUploadRequest<*>,
                    result: CompleteMultipartUploadResult
                ) {
                    XLog.d("日志上传成功")
                    iUploadLog?.success()
                }

                override fun onFailure(
                    request: MultipartUploadRequest<*>,
                    clientException: ClientException?,
                    serviceException: ServiceException?
                ) {
                    clientException?.printStackTrace()
                    serviceException?.printStackTrace()
                    XLog.d("日志上传失败")
                    XLog.d("日志上传失败")
                    iUploadLog?.fail()
                }
            })
    }


}