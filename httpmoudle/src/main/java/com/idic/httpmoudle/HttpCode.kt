package com.idic.httpmoudle

/**
 * 文 件 名: ErrorCode
 * 创 建 人: sineom
 * 创建日期: 2018/8/15 10:47
 * 修改时间：
 * 修改备注： 错误嘛
 */

object HttpCode {

    const val OK = 1

    const val ERROR = 0

    //系统未知错误
    const val UN_SYS_ERROR = 999

    //参数错误
    const val PARAMETER_ERROR = 1000

    //账号或密码错误
    const val NAMEORPW_ERROR = 1001
    const val NAMEORPW_ERROR1 = 20200

    //请求超时
    const val CONNECTTIMEOUT = 9001

    //token 失效
    const val TOKEN_INVALID = 9002

    const val TOKEN_INVALID1 = 20311

    //签名错误
    const val SIGN_ERROR = 9003

    //参数格式错误
    const val PARAM_FORMAT_ERROR = 9004

    //系统错误
    const val SYS_ERROR = 9009
    const val SYS_ERROR1 = 20000

    //服务正常
    const val SERVICE_SUCESS = 20100

    //绑定设备无数据
    const val NOPRODUCT = 20103

    //返回数据库的具体异常
    const val DBERROR = 20104

    //数据异常
    const val DATAERROR = 20106

    //参数异常
    const val PARAMERROR = 20107

    //账号异常
    const val ACCOUNTERROR = 20201

    //商户状态正在审核，不可登录
    const val AUDITING = 20202

    //请使用商户账号登录
    const val ACCOUNTTYPEERROR = 20203

    //商户状态审核异常
    const val ACCOUNR_AUDIT_ERROR = 20204

    //商户下没有此设备数据
    const val NO_PRODUCT = 20205

    //设备数据异常
    const val DEVICE_DATA_ERROR = 20206
    const val DEVICE_DATA_ERROR1 = 20218

    //设备未激活
    const val UN_ACTIVATION = 20207

    //交易流水异常
    const val DEAL_FLOW_ERROR = 20208

    //交易流水状态异常
    const val DEAL_FLOW_STATUS_ERROR = 20209

    //商户账户不存在或异常
    const val unACCOUNT = 20210
    const val unACCOUNT1 = 20217

    //退款失败
    const val REFUND_ERROR = 20211

    //订单异常
    const val ORDER_ERROR = 20212

    //订单已支付
    const val ORDER_PAYED = 20213

    //获取支付二维码失败
    const val PAYQRCODE_ERROR = 20214

    //订单金额异常
    const val ORDER_money = 20215

    //优惠金额异常
    const val PREFERENTIAL_ERROR = 20216

    //获取运营商信息失败
    const val OPERATOR_ERROR = 20219

    // 订单未支付
    const val ORDER_UN_PAY = 20220

    //订单已关闭
    const val ORDER_CLOSED = 20221

    //获取退款交易错误
    const val GET_REFUND_ERROR = 20222

    //产品价格异常
    const val PROUCT_PRICE_ERROR = 20301

    //数据类型转换异常
    const val DATA_TYPE_ERROR = 20302

    //订单数据为空异常
    const val ORDER_NULL_ERROR = 20303L

    //订单产品异常
    const val ORDER_PRODUCT_ERROR = 20304

    //订单产品总价异常
    const val OEDER_MONEY_ERROR = 20305

    //设备信息异常
    const val DEVICE_INFO_ERROR = 20306

    //设备已到有效期
    const val DEVICE_INVALID = 20307

    //购买商品数量超过货道最大库存
    const val GET_PEODUCT_COUNT_ERROR = 20308

    //购买数量超过最大库存锁数量
    const val GET_PEODUCT_COUNT_ERROR1 = 20309

    //系统token异常
    const val SYS_TOKEN_ERROR = 20310

    //服务端允许客户端请求最大时间误差为5分钟
    const val TIME_INVALID = 20312

    //无效的Key码
    const val KEY_INVALID = 20313

    //货柜没有该产品的货道
    const val NO_PRODUCT_CHANNEL = 20314

    //货道信息与货道数量不匹配
    const val CHANNEL_ERROR = 20315

    //产品货道信息不匹配
    const val PRODUCT_CHANNEL_ERROR = 20316

    //产品货道行列数异常
    const val CHANNEL_LINE_ERROR = 20317
    const val CHANNEL_LINE_ERROR1 = 20318

    //异常货道信息不能为空
    const val CHANNEL_LINE_ERROR3 = 20319

    //货道产品数量不足
    const val CHANNEL_PRODUCT_INSUFFICIENT = 20320

    //设备到期时间异常
    const val DEVICE_INVALID_TIME_ERROR = 20321

    //订单产品已下架
    const val ORDER_PRODUCT_CLOSED = 20322

    // 没有找到设备货柜数据
    const val NO_DEVICE_DATA = 20325


}