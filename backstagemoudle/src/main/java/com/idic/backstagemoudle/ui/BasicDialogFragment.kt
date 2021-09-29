package com.idic.backstagemoudle.ui

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import com.idic.backstagemoudle.R

/**
 * 文 件 名: BasicDialogFragment
 * 创 建 人: sineom
 * 创建日期: 2018/11/28 14:43
 * 修改时间：
 * 修改备注：
 */

open class BasicDialogFragment : DialogFragment() {

    var dismiss: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog.window!!.setGravity(Gravity.TOP)
        val lp = dialog.window!!.attributes
        lp.width = activity!!.resources.getDimension(R.dimen.x800).toInt()
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun setDismissListener(listener: () -> Unit) {
        dismiss = listener
    }

    override fun dismiss() {
        super.dismiss()
        dismiss?.invoke()
    }
}
