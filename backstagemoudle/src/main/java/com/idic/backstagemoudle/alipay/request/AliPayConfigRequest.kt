package com.idic.backstagemoudle.alipay.request

import com.idic.basecommon.DeviceInfo
import com.idic.httpmoudle.request.DKRequest

class AliPayConfigRequest : DKRequest() {

    var deviceKey = DeviceInfo.getInstance().device_key

    init {

        ininKey_token_time()
    }
}
