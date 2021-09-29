package com.idic.basecommon.utils

import android.os.Environment
import java.io.File

/**
 * 文 件 名: FilePath
 * 创 建 人: sineom
 * 创建日期: 2019-09-11 16:07
 * 修改时间：
 * 修改备注：
 */

object FilePath {


    private val ROOT_PATH =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "idic" + File.separator

    //视频存储的路径
    val videoRootPath =
        ROOT_PATH + "video"

    //本地视频路径
    val localVideoPath =
        ROOT_PATH + "localVideo"


    //本地视频路径
    val bgVideo = localVideoPath + File.separator + "bg_video.mp4"

    //待机页视频，从后台下载到本地  初始路径
    val stand_Video = localVideoPath + File.separator + "standVideo"
    //apk 下载路径
    val aliPayApk =
        ROOT_PATH + "apk"

    val HELP_QRCODE_FILE = ROOT_PATH + "QRCODE" + File.separator + "weChat.png"

    //日志目录
    val loggParsent =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "logger"

    //压缩的日志名称
    val zipLogFile = loggParsent + File.separator + "log.rar"

    //图片存储位置
    val imgPath = ROOT_PATH + "img" + File.separator
}