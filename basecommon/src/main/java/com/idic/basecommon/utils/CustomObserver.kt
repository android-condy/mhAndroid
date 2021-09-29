package com.idic.basecommon.utils

import com.elvishew.xlog.XLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * 文 件 名: CustomConsumer
 * 创 建 人: sineom
 * 创建日期: 2018/4/27 09:48
 * 修改时间：
 * 修改备注： kotlin式RX回调
 */

class CustomObserver<T> : Observer<T> {

    var disposable: Disposable? = null
        private set

    private var next: ((t: T) -> Unit)? = null

    fun _onNext(listener: (t: T) -> Unit) {
        next = listener
    }

    private var error: ((e: Throwable) -> Unit)? = null

    fun _onError(listener: (e: Throwable) -> Unit) {
        error = listener
    }


    private var complete: (() -> Unit)? = null

    fun _onComplete(listener: () -> Unit) {
        complete = listener
    }

    private var subscribe: ((d: Disposable) -> Unit)? = null

    fun _subscribe(listener: (d: Disposable) -> Unit) {
        subscribe = listener
    }


    override fun onNext(t: T) {
        next?.invoke(t)
    }

    override fun onComplete() {
        complete?.invoke()
        disposable?.dispose()
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
        subscribe?.invoke(d)
    }

    override fun onError(e: Throwable) {
        XLog.e("error---->$e", e)
        disposable?.dispose()
        error?.invoke(e)
    }

    fun setCallBack(listener: CustomObserver<T>.() -> Unit) {
        listener()
    }
}