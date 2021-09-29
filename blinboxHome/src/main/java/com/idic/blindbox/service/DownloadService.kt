package com.idic.blindbox.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.idic.datamoudle.Injection
import com.idic.datamoudle.db.entity.BannerEntity
import com.idic.datamoudle.source.AdDataSource
import com.idic.httpmoudle.URL

class DownloadService : Service(), AdDataSource.LoadAdDatasCallBack {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val provideAdRepository = Injection.provideAdRepository()
//        provideAdRepository.removeAllData()
//        provideAdRepository.getAds("1", this)
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onBannerLoaded(banners: List<BannerEntity>) {

    }

    override fun onAdDataNotAvaliad() {
    }
}
