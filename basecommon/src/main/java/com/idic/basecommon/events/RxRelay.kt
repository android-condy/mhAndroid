package com.idic.basecommon.events

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * 文 件 名: RxRelay
 * 创 建 人: sineom
 * 创建日期: 2018/6/5 14:16
 * 修改时间：
 * 修改备注：
 */

class RxRelay {

    private val subjects = HashSet<Disposable>()

    fun addObserver(disposable: Disposable) {
        subjects.add(disposable)
    }

    fun removeObserver(disposable: Disposable) {
        subjects.remove(disposable)
    }

    fun cleanObserver() {
        subjects.filter {
            !it.isDisposed
        }.forEach {
            it.dispose()
        }
    }

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { RxRelay() }
    }

    private val _bus = PublishRelay.create<Any>()

    fun <T> toObserver(clazz: Class<T>): Observable<T> {
        return _bus.ofType(clazz)
    }

    fun post(any: Any) {
        _bus.accept(any)
    }

    fun hasObserver() = _bus.hasObservers()


}