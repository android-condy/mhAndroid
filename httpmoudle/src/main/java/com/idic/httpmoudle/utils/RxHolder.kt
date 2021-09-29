package com.idic.httpmoudle.utils

import com.google.gson.Gson
import com.idic.httpmoudle.response.BaseResponse
import com.idic.httpmoudle.response.DKResponse
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 文 件 名: RxHolder
 * 创 建 人: sineom
 * 创建日期: 2019/3/6 16:22
 * 修改时间：
 * 修改备注：
 */

object RxHolder {
    fun <T> IO2Main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    inline fun <reified T> getResponseData(): ObservableTransformer<DKResponse, T> {
        return ObservableTransformer { upstream ->
            upstream.map {
                val gson = Gson()
                gson.fromJson(gson.toJson(it.data), T::class.java)
            }
        }
    }
    inline fun <reified T> getResponseData2(): ObservableTransformer<BaseResponse, T> {
        return ObservableTransformer { upstream ->
            upstream.map {
                val gson = Gson()
                gson.fromJson(gson.toJson(it.data), T::class.java)
            }
        }
    }


    inline fun <reified T> getResponseListData(): ObservableTransformer<DKResponse, ArrayList<T>> {
        return ObservableTransformer { upstream ->
            upstream.map {
                val gson = Gson()
                val type = ListParameterizedType(T::class.java)
                gson.fromJson(gson.toJson(it.data), type) as ArrayList<T>
            }
        }
    }
    inline fun <reified T> getResponseListData2(): ObservableTransformer<BaseResponse, ArrayList<T>> {
        return ObservableTransformer { upstream ->
            upstream.map {
                val gson = Gson()
                val type = ListParameterizedType(T::class.java)
                gson.fromJson(gson.toJson(it.data), type) as ArrayList<T>
            }
        }
    }


    class ListParameterizedType(private val type: Type) : ParameterizedType {

        override fun getActualTypeArguments(): Array<Type> {
            return arrayOf(type)
        }

        override fun getRawType(): Type {
            return java.util.ArrayList::class.java
        }

        override fun getOwnerType(): Type? {
            return null
        }
    }

}