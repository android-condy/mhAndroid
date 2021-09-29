package com.idic.httpmoudle

import com.idic.httpmoudle.response.BaseResponse
import com.idic.httpmoudle.response.DKResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*


/**
 * 文 件 名: IApi
 * 创 建 人: sineom
 * 创建日期: 2018/11/17 09:51
 * 修改时间：
 * 修改备注：
 */

interface IApi {

    //登录
    @POST(URL.LOGIN)
    fun login(@Body requestBody: RequestBody): Observable<DKResponse>

    //获取token
    @POST(URL.REFREH_TOKEN)
    fun refreshToken(@Body requestBody: RequestBody): Observable<DKResponse>

    //    //获取商品信息
    @POST(URL.GOODS_DATA)
    fun getProducts(@Body requestBody: RequestBody): Observable<DKResponse>

    //获取banner信息
    /**
     * 类型1、首页轮播广告位
    类型2、热推产品A
    类型3、热推产品B
    类型4、取货页产品位
    类型5、取货页轮播广告位
     */
    @POST(URL.NEWBANNER)
    fun getBanner(@Body requestBody: RequestBody): Observable<DKResponse>

    //----------聚合支付订单查询  只有用户扫描了二维码之后才会生成对应的订单信息
    @POST(URL.ORDER_PAY)
    fun queryJuHeMaPay(@Body requestBody: RequestBody): Observable<DKResponse>

    // -----------点击item之后 添加订单-> 生成二维码-> 查询订单 -------------

    //获取添加订单
    @POST(URL.ADD_ORDER)
    fun addOrder(@Body requestBody: RequestBody): Observable<DKResponse>

    //收钱吧退款
    @POST(URL.ADD_EXIT_ORDER)
    fun addRefundSQB(@Body requestBody: RequestBody): Observable<DKResponse>

    //聚合码退款
    @POST(URL.ADD_EXIT_ORDER)
    fun addRefundJHM(@Body requestBody: RequestBody): Observable<DKResponse>

    //获取支付二维码
    @POST(URL.GET_CODE)
    fun getSQBPayCode(@Body requestBody: RequestBody): Observable<DKResponse>

    //查询支付码是否付款
    @POST(URL.ORDER_PAY_ID)
    fun queryShouQianBaIsPay(@Body requestBody: RequestBody): Observable<DKResponse>

    @POST(URL.CANCEL_CODE_ORDER)
    fun cancelShouQianBaOrder(@Body requestBody: RequestBody): Observable<DKResponse>

    /***** 后台调试******/
    //h获取货柜信息
    @POST(URL.GET_DECEIVED_DETAIL)
    fun loadContainerInfo(@Body requestBody: RequestBody): Observable<DKResponse>

    //加载货柜货道信息
    @POST(URL.LOAD_CONTAIN_INFORMATION_NEW)
    fun loadContainerChannelInfo(@Body requestBody: RequestBody): Observable<DKResponse>

    //修改货道信息
    @POST(URL.UPDATE_CONTAIN_INFORMATION)
    fun modifyChannelInfo(@Body requestBody: RequestBody): Observable<DKResponse>

    //整行补满
    @POST(URL.ROWNUM_CARGO)
    fun FillupChannelForRow(@Body requestBody: RequestBody): Observable<DKResponse>

    //加载产品信息
    @POST(URL.LOAD_GOODS_INFORMATION)
    fun loadSpinnerProduct(@Body requestBody: RequestBody): Observable<DKResponse>

    //全部产品补充满
    @POST(URL.ONE_KEY_FULL_CARGO)
    fun supplementFull(@Body requestBody: RequestBody): Observable<DKResponse>

    /***** 后台调试******/
    @POST(URL.UPDATE_DEVICED_STATUS)
    fun uploadDeviceStatus(@Body requestBody: RequestBody): Observable<DKResponse>

    /**货道恢复**/
    @POST(URL.BACK_CONTAIN_INFORMATION)
    fun recoverChannelStatus(@Body requestBody: RequestBody): Observable<DKResponse>

    //验证卡号
    @POST(URL.checkCard)
    fun checkCard(@Body requestBody: RequestBody): Observable<DKResponse>

    //获取类目id
    @POST(URL.getCategory)
    fun loadCategory(@Body requestBody: RequestBody): Observable<DKResponse>

    //根据类目id获取产品
    @POST(URL.getProductFormCategoryId)
    fun loadProductFormCategory(@Body requestBody: RequestBody): Observable<DKResponse>

    //刷脸付后台扣款
    @POST(URL.smilePayUrl)
    fun smailPay(@Body requestBody: RequestBody): Observable<DKResponse>

    @POST(URL.getAliPayConfig)
    fun getAliPayConfig(@Body requestBody: RequestBody): Observable<DKResponse>

    @Streaming //大文件时要加不然会OOM
    @GET
    fun downloadFile(@Url fileUrl: String): Observable<ResponseBody>

    //查询卡号
    @GET(URL.queryCard)
    fun queryCard(@Query("cardCode") cardCode: String): Observable<DKResponse>

    @POST(URL.activeDevice)
    fun initProduct(@Body requestBody: RequestBody): Observable<DKResponse>

    //查询卡号
    @POST(URL.deviceLogin)
    fun checkActive(@Query("key") key: String): Observable<DKResponse>

    //获取蚂蚁零售的付款码
    @POST
    fun antCode(@Url url: String = URL.ants, @Body requestBody: RequestBody): Observable<Any>

    //api改动
//===========================================================================================================================
    // 全部商品分类
    @POST
    @FormUrlEncoded
    fun getGoodsCategory(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/type", @Field("device_id") device_id: String? = URL.device_id): Observable<BaseResponse>

    //首页banners
    @POST
    @FormUrlEncoded
    fun getMainBanners(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/banner/index/lb", @Field("device_id") device_id: String? = URL.device_id): Observable<BaseResponse>

    //首页底部两个banner
    @POST
    @FormUrlEncoded
    fun getButtomBanners(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/banner/index/db", @Field("device_id") device_id: String? = URL.device_id): Observable<BaseResponse>

    //付款取货banner
    @POST
    @FormUrlEncoded
    fun getGoodsSuccessBanners(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/banner/index/goods_success_db", @Field(
            "device_id"
        ) device_id: String? = URL.device_id
    ): Observable<BaseResponse>

    //商品详情  , @Field("sku_code") sku_code: String
    @POST
    @FormUrlEncoded
    fun getGoodsDetail(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/d", @Field("device_id") device_id: String? = URL.device_id, @FieldMap map: Map<String, String>): Observable<Any>

    //全部商品   type_id加这个是分类下的
    @POST
    @FormUrlEncoded
    fun getGoods(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/product", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "type_id"
        ) type_id: String = ""
    ): Observable<BaseResponse>

    //后台登录
    @POST
    @FormUrlEncoded
    fun getBackStageLogin(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/sys/login", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "login_pwd"
        ) login_pwd: String, @Field("login_name") login_name: String
    ): Observable<BaseResponse>

    //创建订单

    @POST
    @FormUrlEncoded
    fun getCreateOrder(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/order/c", @Field("device_id") device_id: String? = URL.device_id, @FieldMap map: Map<String, String>): Observable<BaseResponse>

    //创建订单

    @POST
    @FormUrlEncoded
    fun getUpLoadAisle(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/sell_goods", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "pro_list"
        ) pro_list: String
    ): Observable<Any>

//    //上传货道数量
//    @POST
//    fun getUpLoadAisle(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/sell_goods", @Body requestBody: RequestBody): Observable<Any>

    //订单状态查询
    @POST
    @FormUrlEncoded
    fun getOrderStatus(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/order/s", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "order_sn"
        ) order_sn: String
    ): Observable<Any>

    //获取全部货道商品
    @POST
    @FormUrlEncoded
    fun getAislesGoods(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/all_goods", @Field("device_id") device_id: String? = URL.device_id): Observable<BaseResponse>

    @POST
    @FormUrlEncoded
    fun getUpdataAislesGoods(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/edit_goods", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "user_id"
        ) user_id: Int, @Field(
            "pro_list"
        ) pro_list: String
    ): Observable<Any>

    @POST
    @FormUrlEncoded
    fun getSyncSystemStatus(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/sys/sync", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "version"
        ) version: String
    ): Observable<Any>

    @POST
    @FormUrlEncoded  //error _id  =0  取货中  1 取货成功    2  取货失败  message   取货失败时：传  原因
    fun getTakeStatus(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/product/take_status", @Field("device_id") device_id: String? = URL.device_id, @Field(
            "error_id"
        ) error_id: String
        , @Field("message") message: String
        , @Field("order_sn") order_sn: String
        , @Field("aisles") aisles: String
        , @Field("egg_code_x") egg_code_x: String
        , @Field("egg_code_y") egg_code_y: String
    ): Observable<Any>


    @POST
    @FormUrlEncoded
    fun getCustomerServiceButtomBanner(
        @Url url: String = "http://zfj.api.fmcgbi.com/api/app/banner/index/kf_db", @Field(
            "device_id"
        ) device_id: String? = URL.device_id
    ): Observable<BaseResponse>


    //获取全部货道商品
    @POST
    @FormUrlEncoded
    fun uploadSystemMessage(@Url url: String = "http://zfj.api.fmcgbi.com/api/app/sys/fault", @Field("device_id") device_id: String? = URL.device_id
                            , @Field("status") status: String
                            , @Field("msg") msg: String): Observable<BaseResponse>

    @GET("http://api.kjndj.com/api/mh/update?type=new")
    fun requestVersion(): Observable<Any>

}