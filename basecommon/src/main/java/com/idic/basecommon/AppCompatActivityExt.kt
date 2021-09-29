package com.idic.basecommon

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity

/**
 * 文 件 名: ActivityExt
 * 创 建 人: sineom
 * 创建日期: 2019/4/15 11:14
 * 修改时间：
 * 修改备注：
 */


fun <T : ViewModel, T1 : BaseModelFactory> AppCompatActivity.obtainViewModel(
    viewModelClass: Class<T>,
    modelFactory: T1
) =
    ViewModelProviders.of(this, modelFactory).get(viewModelClass)

/**
 * The `fragment` is added to the container view with id `frameId`. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, frameId: Int) {
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

/**
 * The `fragment` is added to the container view with tag. The operation is
 * performed by the `fragmentManager`.
 */
fun AppCompatActivity.addShowFragmentToActivity(
    frameId: Int,
    showFragment: Fragment,
    hideFragment: Fragment,
    tag: String
) {
    supportFragmentManager.transact {
        hide(hideFragment)
        add(frameId, showFragment, tag)
        show(showFragment)
    }
}

fun AppCompatActivity.addShowFragmentStackToActivity(
    frameId: Int,
    showFragment: Fragment,
    hideFragment: Fragment,
    tag: String,
    stackTag: String
) {
    supportFragmentManager.transact {
        hide(hideFragment)
        add(frameId, showFragment, tag)
        show(showFragment)
        addToBackStack(stackTag)
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}

//隐藏底部状态栏
fun Activity.hideBottomBar() {
    val intent1 = Intent()
    intent1.action = "ACTION_HIDE_STATUSBAR"
    sendBroadcast(intent1)
}

//显示底部状态栏
fun Activity.showBottomBar() {
    val intent1 = Intent()
    intent1.action = "ACTION_SHOW_STATUSBAR"
    sendBroadcast(intent1)
}