package com.idic.login.utils

import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


/**
 * 文 件 名: FileUtilExp
 * 创 建 人: sineom
 * 创建日期: 2020/6/10 10:52
 * 修改时间：
 * 修改备注：
 */
fun copyFilesFassets(
    oldPath: String,
    newPath: String
) {
    try {
        val fileNames: Array<String> =
            Utils.getApp().assets.list(oldPath) as Array<String> //获取assets目录下的所有文件及目录名
        for (fileName in fileNames) {
            if (fileName.endsWith(".mp4")) {
                var file = File(newPath + File.separator + fileName)
                if (file.exists()) {
                    continue
                }
                FileUtils.createOrExistsFile(file)
                val `is`: InputStream = Utils.getApp().getAssets().open(fileName)
                val fos = FileOutputStream(file)
                val buffer = ByteArray(4096)
                var byteCount = 0
                while (`is`.read(buffer)
                        .also { byteCount = it } != -1
                ) { //循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount) //将读取的输入流写入到输出流
                }
                fos.flush() //刷新缓冲区
                `is`.close()
                fos.close()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()

    }
}