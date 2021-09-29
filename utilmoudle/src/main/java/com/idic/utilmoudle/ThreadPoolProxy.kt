package com.idic.logger


/**
 * Created by bruce on 2017/12/25.
 */

import java.util.concurrent.*

/**
 * 线程池代理类
 */
internal class ThreadPoolProxy(
    private val corePoolSize: Int,
    private val maximumPoolSize: Int,
    private val keepAliveTime: Long
) {

    private var mThreadPoolExecutor: ThreadPoolExecutor? = null

    private fun initExecutor() =
        mThreadPoolExecutor ?: synchronized(this) {
            val unit = TimeUnit.MILLISECONDS
            val threadFactory = Executors.defaultThreadFactory()
            val handler = ThreadPoolExecutor.AbortPolicy()
            val workQueue = LinkedBlockingQueue<Runnable>()
            mThreadPoolExecutor = ThreadPoolExecutor(
                corePoolSize, //核心线程数
                maximumPoolSize, //最大线程数
                keepAliveTime, //保持时间
                unit, //保持时间对应的单位
                workQueue,
                threadFactory, //线程工厂
                handler//异常捕获器
            ).also { mThreadPoolExecutor = it }
        }

    /**
     * 执行任务
     */
    fun executeTask(r: Runnable) {
        initExecutor()
        mThreadPoolExecutor!!.execute(r)
    }

    /**
     * 提交任务
     */
    fun commitTask(r: Runnable): Future<*> {
        initExecutor()
        return mThreadPoolExecutor!!.submit(r)
    }

    /**
     * 删除任务
     */
    fun removeTask(r: Runnable) {
        initExecutor()
        mThreadPoolExecutor!!.remove(r)
    }

}
