package com.idic.basecommon.utils;

import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

/**
 * 文 件 名: CSVFileNameGenerator
 * 创 建 人: sineom
 * 创建日期: 2019-10-25 09:29
 * 修改时间：
 * 修改备注：
 */

public class CSVFileNameGenerator extends DateFileNameGenerator {

    @Override
    public String generateFileName(int logLevel, long timestamp) {

        return super.generateFileName(logLevel, timestamp) + ".txt";
    }
}
