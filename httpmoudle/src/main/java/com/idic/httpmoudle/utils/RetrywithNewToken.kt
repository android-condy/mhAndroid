package com.idic.httpmoudle.utils

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.fastjson.JSONObject
import com.idic.basecommon.utils.ARouterConfig
import com.idic.httpmoudle.exception.TokenInvalidException
import com.idic.httpmoudle.service.HttpConfigService
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.util.concurrent.TimeUnit

/**
 * 文 件 名: RetrywithNewToken
 * 创 建 人: sineom
 * 创建日期: 2019-08-02 16:16
 * 修改时间：
 * 修改备注：
 */

class RetrywithNewToken(private val userName: String, private val pwd: String) :
    Function<Observable<Throwable>, ObservableSource<*>> {

    override fun apply(t: Observable<Throwable>): ObservableSource<*> {
        return t.flatMap { throwable ->
            with(throwable.javaClass) {
                when {
                    isAssignableFrom(TokenInvalidException::class.java) -> {
                        var updateSuccess = false
                        RetrofitUtil.instance.getDefautlRetrofit().login(
                            HttpUtils.createBody("{\"loginName\":\"$userName\",\"pwd\":\"$pwd\"}")
                        ).filter {
                            it.isSuccess() && it.data != null
                        }.map {
                            it.data!!
                        }.subscribe({
                            val token = JSONObject.parseObject(JSONObject.toJSONString(it)).getString("accesstoken")
                            (ARouter.getInstance()
                                .build(ARouterConfig.HTTP_ACCECC_TOKEN).navigation() as HttpConfigService)
                                .updateToken(token)
                            updateSuccess = true
                        }, {

                        })
                        if (updateSuccess) {
                            Observable.timer(500, TimeUnit.MICROSECONDS)
                        } else {
                            t
                        }
                    }
                    else -> {
                        t
                    }
                }
            }
        }
    }
}

