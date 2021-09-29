package com.idic.blindbox.service

import android.app.Service
import android.content.Intent
import com.idic.backstagemoudle.util.DialogUtil
import com.idic.basecommon.events.LightType
import com.idic.basecommon.events.RxRelay
import com.idic.basecommon.utils.CustomObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class LightService : Service() {

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()

        LightControl.openCOM("/dev/ttyS0")
        LightControl.whiteLight()
//        LightControl.whiteLightn()
        // 20210202 update
        LightControl.openCOM1("/dev/ttyS1")
//        if(DialogUtil.getSharedPreferences(this).equals("1")){
//            LightControl.whiteLightn()
//        }else if (DialogUtil.getSharedPreferences(this).equals("2")){
//            LightControl.purpleLightn()
//        }else if (DialogUtil.getSharedPreferences(this).equals("3")){
//            LightControl.greenLightn()
//        }else if (DialogUtil.getSharedPreferences(this).equals("4")){
//            LightControl.redLightn()
//        }else{
//        LightControl.whiteLight()
        LightControl.whiteLightn()
//        }

        rxListener()
    }

    override fun onBind(intent: Intent?) = null
    private fun rxListener() {
        disposable?.dispose()
        RxRelay.instance.toObserver(LightType::class.java)
            .throttleLast(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(CustomObserver<LightType>().apply {
                _subscribe {
                    this@LightService.disposable = it
                }
                _onNext {
                    when (it) {
//                        LightType.PURPLE_LIGHT -> {
//                            LightControl.purpleLightn()
//                        }
//                        LightType.WHITE_LIGHT -> {
//                            LightControl.whiteLightn()
//                        }
//                        LightType.GREEN_LIGHT -> {
//                            LightControl.greenLightn()
//                        }
//                        LightType.RED_LIGHT -> {
//                            LightControl.redLightn()
//                        }
                    }
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
