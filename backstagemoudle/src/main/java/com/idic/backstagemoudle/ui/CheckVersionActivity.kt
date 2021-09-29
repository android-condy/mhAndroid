package com.idic.backstagemoudle.ui

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.launcher.ARouter
import com.idic.backstagemoudle.R
import com.idic.basecommon.BaseFullActivity
import com.idic.basecommon.utils.ARouterConfig
import kotlinx.android.synthetic.main.activity_check_version.*
import kotlinx.android.synthetic.main.back_start_toolbar.*

class CheckVersionActivity : BaseFullActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_version)
        version1.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.CATEGORY_HOME).navigation(this)
        }
        version2.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.HALF_PRODUCT_1).navigation(this)
        }
        version3.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.LUCK_BAG_HOME).navigation(this)
        }
        version4.setOnClickListener {
            ARouter.getInstance().build(ARouterConfig.BLUE_HOME).navigation(this)
        }
        tvSave.visibility = View.GONE
        tvBack.setOnClickListener {
            finish()
        }
    }
}
