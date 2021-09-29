package com.idic.httpmoudle


/**
 * 请求网址
 */
object URL {
    //设备id
    val device_id: String? = android.os.Build.SERIAL
        get() =
            when {
                field.equals("unknown") || field.isNullOrEmpty() ->
                    "FS2101550012"
//                    "FS2101550060"
                else -> android.os.Build.SERIAL
            }

    // 服务器IP地址
    var API_IP = "http://mh.api.kjndj.com"
//    var API_IP = "http://zfj.api.fmcgbi.com"
//    var API_IP = "http://47.99.74.155"

    var API_PORT = "80"

    //图片的域名
    var IMG_DOMIAN = "http://mh.api.kjndj.com"

    var IMG_PORT = "80"


    // 设备登录接口
    const val LOGIN = "api/login/checkDeviceInfo.do"

    // 设备获取AccessToken接口
    const val REFREH_TOKEN = "api/token/getAccessToken.do"

    //    // 商品列表接口
    const val GOODS_DATA = "api/product/loadProductSerial.do"
    /**
     * device_id=FS1912550188&type_id=1
     */
    const val GOODS_DATA1 = "http://zfj.api.fmcgbi.com/api/app/product"

    // 查询banner信息接口
    const val NEWBANNER = "api/banner/loadvideobanner.do"

    // 查询订单是否支付接口
    const val ORDER_PAY = "api/order/queryorderbycode.do"

    // 订单异常更换货道
    const val ORDER_EX_CHANGE_CONTAIN = "api/order/changedeviceway.do"

    // 添加退款订单
    const val ADD_EXIT_ORDER = "api/order/addrefundorder.do"

    // 获取设备详情（货柜数量，货道行数、列数）
    const val GET_DECEIVED_DETAIL = "api/device/getdevice.do"

    // 加载货柜货道信息
    const val LOAD_CONTAIN_INFORMATION = "api/device/loadDeviceContain.do"
    const val LOAD_CONTAIN_INFORMATION_NEW = "api/device/loadDeviceContainNew.do"

    // 修改货道信息
    const val UPDATE_CONTAIN_INFORMATION = "api/device/updateDeviceWayNew.do"

    // 恢复货道状态
    const val BACK_CONTAIN_INFORMATION = "api/device/handlerUnusual.do"

    // 加载产品列表信息
    const val LOAD_GOODS_INFORMATION = "api/device/productList.do"

    const val LOAD_GOODS_INFORMATION_NEW = "api/device/productListNew.do"

    // 加载设备交易流水列表
    const val LOAD_DEVICED_SERIAL = "api/device/loadSerial.do"

    // 报告设备状态
    const val UPDATE_DEVICED_STATUS = "api/device/updateDeviceStatus.do"

    // app版本更新
    const val UPDATE_CHECK_APP = "api/getversion/getnew.do"

    // 后台一键补满货道库存
    const val ONE_KEY_FULL_CARGO = "api/device/deviceOneKeyFillUp.do"

    // 后台按照行补满整行库存
    const val ROWNUM_CARGO = "api/device/deviceRowFillUp.do"

    // 添加订单
    const val ADD_ORDER = "api/order/addorder.do"

    // 获取支付二维码接口
    const val GET_CODE = "api/pay/qrcode.do"

    // 查询订单是否支付接口
    const val ORDER_PAY_ID = "api/order/queryorderbyid.do"

    // 取消订单接口
    const val CANCEL_CODE_ORDER = "api/order/cancelorder.do"

    //刷脸付支付接口
    const val smilePayUrl = "api/pay/aliscanpay.do"

    //获取支付宝刷脸付的配置
    const val getAliPayConfig = "api/login/alipayuserinfo.do"

    //获取类目id
//    const val getCategory = "api/product/loadCateNew.do"
    const val getCategory = "api/product/loadCate.do"

    //根据类目获取对应的产品
    const val getProductFormCategoryId = "api/product/loadproductbycategoryid.do"

    //校验卡号
    const val checkCard = "api/sqpay/integralpay.do"


    const val queryCard = "/api/member/selectByCardCode.do"

    //设备激活
    const val activeDevice = "/api/login/deviceInit.do"

    const val deviceLogin = "/api/login/validate.do"

    const val ants = "http://payment.t.antretail.cn/open/payment/code"
}

