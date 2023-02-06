package cc.ixcc.noveltwo.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.ixcc.noveltwo.R
import kotlinx.android.synthetic.main.dialog_reword_bottom.*

class RewordBottomDialog( context: Context, var dataList: List<cc.ixcc.noveltwo.bean.RewordBean>) : Dialog(context,R.style.RewordDialogStyle) {

    var rewordNumber=0;
    //private var mRewordBean:RewordBean? = null
    private var mAdapter: cc.ixcc.noveltwo.ui.adapter.RewordRuleAdapter? = null
    var mDialogCallBack: RewordBottomDialog.RewordDialogCallBack? = null
    //private var mRewordList:List<RewordBean>?=null;
    private var mrecyclerView_reword:RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_reword_bottom)
        window?.attributes?.run {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.BOTTOM
        }
        val layoutManager = GridLayoutManager(context, 3)
        recyclerView_reword.setLayoutManager(layoutManager)
        mAdapter= cc.ixcc.noveltwo.ui.adapter.RewordRuleAdapter(context, dataList);
        mAdapter!!.setActionListener(cc.ixcc.noveltwo.ui.adapter.RewordRuleAdapter.ActionListener { rewordBean ->
            et_reword.setText(rewordBean.coin)
        })
        recyclerView_reword.setAdapter(mAdapter)
        mAdapter!!.SetSelect(0)
        tv_reword_sure.setOnClickListener {
            if (mDialogCallBack != null) {
                mDialogCallBack!!.rewordClick(et_reword.text.toString()!!);
                dismiss()
            }
        }
    }

    override fun show() {
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
    }

    interface RewordDialogCallBack {
        /**
         * 事件点击
         */
        fun rewordClick(coin:String)
    }

    /**
     * 接口回调
     */
    fun setDialogCallBack(rewordBottomDialog: RewordBottomDialog.RewordDialogCallBack) {
        this.mDialogCallBack = rewordBottomDialog
    }
}