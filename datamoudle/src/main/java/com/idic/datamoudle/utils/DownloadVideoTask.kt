package com.idic.datamoudle.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import com.idic.basecommon.utils.CustomObserver
import com.idic.basecommon.utils.FilePath
import com.idic.basecommon.utils.FilePath.localVideoPath
import com.idic.basecommon.utils.FilePath.stand_Video
import com.idic.basecommon.utils.FilePath.videoRootPath
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.download.DownloadImpl
import com.idic.datamoudle.source.AdDataSource
import com.idic.utilmoudle.UtilsApp
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * 文 件 名: DownloadVideoTask
 * 创 建 人: sineom
 * 创建日期: 2019/2/28 16:59
 * 修改时间：
 * 修改备注：
 */

object DownloadVideoTask {

    private val download = DownloadImpl()


    fun downloadVideo(
        bannerData: List<BannerEntity>,
        callBack: AdDataSource.LoadAdDatasCallBack
    ) {
        FileUtils.createOrExistsDir(videoRootPath)
        FileUtils.createOrExistsDir(localVideoPath)
//        File(videoRootPath).listFiles().forEach loop@{ file ->
//            var del = false
//            for (data in bannerData) {
//                val name = file.name
//                del = data.image.endsWith(name)
//                if (del) break
//            }
//            if (!del) {
//                FileUtils.deleteFile(file)
//            }
//        }

        Observable.fromIterable(bannerData)
            .subscribeOn(Schedulers.io())
            .filter {
                val split = it.image.split(File.separator)
                val videoName = split.last()
                val videoPath = videoRootPath + File.separator + videoName
                it.localPath = videoPath
                !FileUtils.isFileExists(it.localPath)
            }.map {
                if (it.isVideo()) {
                    download.downloadFile(it.image, "${it.localPath}")
                } else {
                    val url = it.image
                    //创建下载任务,downloadUrl就是下载链接
                    val request = DownloadManager.Request(Uri.parse(url))
                    //指定下载路径和下载文件名
                    request.setDestinationUri(Uri.fromFile(File("${it.localPath}")))
                    //获取下载管理器
                    val downloadManager =
                        Utils.getApp().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    //将下载任务加入下载队列，否则不会进行下载
                    downloadManager.enqueue(request)
                }
                it
            }.subscribe(CustomObserver<BannerEntity>().apply {
                _onComplete {
                    callBack.onBannerLoaded(bannerData)
                }
                _onError {
                    callBack.onBannerLoaded(bannerData)
                }
            })


    }


    fun downloadVideo2(
        url: String

    ) {
        if (url?.isNullOrEmpty()) return
        Observable.create<String> {
            val split = url.split(File.separator)
            val videoName = split.last()
            val videoPath = stand_Video + File.separator + videoName
            ConfigSharedPreferences.saveSharedPreferences(Utils.getApp(), videoPath)
            download.downloadFile(url, videoPath)
        }.subscribeOn(Schedulers.io()).subscribe {

        }

    }


}