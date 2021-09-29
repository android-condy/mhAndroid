package com.idic.backstagemoudle

import com.elvishew.xlog.XLog
import com.idic.backstagemoudle.boxtest.ShipmentManager
import com.idic.backstagemoudle.boxtest.common.outage

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
fun main() {
    for (index in 13 until 29 step 2) {
        val msg = ""
        break
        println("系统故障---->$msg")
        if (!msg.isEmpty() && msg.contains("货斗有货物")) {
            println("系统故障---->货斗有货,开始执行回收")
            return
        } else {
            println("不可恢复故障")
            return
        }
    }
    println("机器无故障并且货斗无货,开始出货")
}


data class Person(val name: String) {
    var age: Int = 10
}