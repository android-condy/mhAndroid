package com.idic.basecommon.service

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

/**
 * 文 件 名: BaseJsonServiceImpl
 * 创 建 人: sineom
 * 创建日期: 2019-08-08 16:05
 * 修改时间：
 * 修改备注：
 */

@Route(path = "/drink/json")
class JsonServiceImpl : SerializationService {
    override fun <T : Any?> json2Object(input: String?, clazz: Class<T>?): T {
        return JSON.parseObject<T>(input, clazz)
    }

    override fun init(context: Context?) {
    }

    override fun object2Json(instance: Any?): String {
        return JSON.toJSONString(instance)
    }

    override fun <T : Any?> parseObject(input: String?, clazz: Type?): T {
        return JSON.parseObject(input, clazz)
    }
}
