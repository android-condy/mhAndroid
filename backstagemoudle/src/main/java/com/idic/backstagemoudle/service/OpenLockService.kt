package com.idic.backstagemoudle.service

import android.app.Service
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.basecommon.events.JpushMessage
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.service.AisleService
import com.idic.basecommon.utils.CustomObserver
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 开锁的service
 */
class OpenLockService : Service() {

    private var disposable: Disposable? = null

    override fun onBind(intent: Intent) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

//        disposable?.dispose()
//        RxRelay.instance.toObserver(JpushMessage::class.java)
//            .observeOn(Schedulers.io())
//            .throttleLast(10, TimeUnit.SECONDS)
//            .filter {
//                it.jPushType == JpushMessage.JPushType.OPENLOCK
//            }.subscribe(CustomObserver<JpushMessage>().apply {
//                _subscribe {
//                    this@OpenLockService.disposable = it
//                }
//                _onNext {
//                    try {
//                        val container = it.container.toInt();
//                        val row = it.row.toInt();
//                        val colum = it.column.toInt();
//                        val number = (row - 1) * 10 + colum - 1
//                        val aisleService =
//                            ARouter.getInstance().navigation(AisleService::class.java)
//                        val motorType = aisleService.getMotorType(container, number)
//                        val screenType = aisleService.getScreenType(container)
////                        ShipmentHolper.launchMotor(number, motorType, container, screenType)
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            })
        return super.onStartCommand(intent, flags, startId)
    }
}
