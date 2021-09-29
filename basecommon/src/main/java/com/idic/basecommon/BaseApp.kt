package com.idic.basecommon

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.dueeeke.videoplayer.exo.ExoMediaPlayerFactory
import com.dueeeke.videoplayer.player.VideoViewConfig
import com.dueeeke.videoplayer.player.VideoViewManager
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.flattener.ClassicFlattener
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter
import com.elvishew.xlog.printer.AndroidPrinter
import com.elvishew.xlog.printer.file.FilePrinter
import com.elvishew.xlog.printer.file.backup.FileSizeBackupStrategy
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy
import com.idic.basecommon.utils.CSVFileNameGenerator
import com.idic.basecommon.utils.FilePath.loggParsent


/**
 * 文 件 名: BaseApp
 * 创 建 人: sineom
 * 创建日期: 2019-06-13 11:36
 * 修改时间：
 * 修改备注：
 */

abstract class BaseApp : MultiDexApplication() {


    /**
     * Application 初始化
     */
    abstract fun initModuleApp(application: Application)

    /**
     * 所有 Application 初始化后的自定义操作
     */
    abstract fun initModuleData(application: Application)


    fun initMoudle() {

        AppConfig.moduleApps.forEach {
            try {
                val clazz = Class.forName(it)
                val baseApp = clazz.newInstance() as BaseApp
                baseApp.initModuleApp(this)
                baseApp.initModuleData(this)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun initLog() {
        val config = LogConfiguration.Builder()
            .logLevel(LogLevel.ALL)
            .tag("idic")
            .t()                                                   // 允许打印线程信息，默认禁止
            .st(2) // 允许打印深度为2的调用栈信息，默认禁止
            .b()
            .build()
        val filePrinter = if (BuildConfig.DEBUG) {
            AndroidPrinter()
        } else {
            FilePrinter                     // 打印日志到文件的打印器
                .Builder(loggParsent)                              // 指定保存日志文件的路径
                .fileNameGenerator(CSVFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(
                    FileSizeBackupStrategy(2 * 1024 * 1024)
                )
                .cleanStrategy(FileLastModifiedCleanStrategy(7 * 24 * 60 * 60 * 1000))// 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .flattener(ClassicFlattener())
                .build()
        }
        // 初始化 XLog
        // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
        XLog.init(config, filePrinter)
    }
}
