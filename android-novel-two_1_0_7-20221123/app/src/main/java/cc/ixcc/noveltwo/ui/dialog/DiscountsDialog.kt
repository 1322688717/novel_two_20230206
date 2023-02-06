package rrxsxs.xsbookhaiwai.xsdtxs.ui.dialog

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.frag_weal.*
import cc.ixcc.noveltwo.R
import cc.ixcc.noveltwo.bean.BuyCoinInfo
import cc.ixcc.noveltwo.bean.UserBean
import cc.ixcc.noveltwo.http.AllApi
import cc.ixcc.noveltwo.http.HttpCallback
import cc.ixcc.noveltwo.http.HttpClient
import cc.ixcc.noveltwo.pay.ali.GooglePay
import cc.ixcc.noveltwo.treader.AppContext
import cc.ixcc.noveltwo.ui.activity.my.ThirdLoginActivity
import cc.ixcc.noveltwo.ui.activity.my.TopUpActivity
import cc.ixcc.noveltwo.utils.SpUtil

class DiscountsDialog : DialogFragment() {

    var beandata: BuyCoinInfo? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DiscountsDialog)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutInflater.from(context).inflate(R.layout.discount_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            it.attributes.gravity = Gravity.BOTTOM
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HttpClient.getInstance().post(AllApi.chargereqconfig, AllApi.chargereqconfig)
            .execute(object : HttpCallback() {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(code: Int, msg: String?, info: Array<String>) {
                    beandata = Gson().fromJson(info[0], BuyCoinInfo::class.java)
                    Log.d("xcs beandata", beandata.toString())

                    val coinBean = beandata?.coin_list?.filter { coinsBaen ->
                        coinsBaen.getpayid() == "coins09"
                    }
                    coinBean?.run {
                        this[0].apply {
                            view.findViewById<TextView>(R.id.coins).text = "${coin.toInt()}"
                            view.findViewById<TextView>(R.id.buy).text = "$${getRmb()}"
                            view.findViewById<TextView>(R.id.num_coins).text = "+${gethandsel()* coin}"
                        }
                    }
                }

                override fun onError() {
                    super.onError()
                    Log.d("Xcs", "topup error")
                }
            })


        view.run {
            view.findViewById<TextView>(R.id.buy).setOnClickListener {
                if (beandata != null) {
                    val tempBean = MMKV.defaultMMKV().decodeParcelable(
                        SpUtil.USER_INFO,
                        UserBean::class.java
                    )
                    if (tempBean.bindStatus == "0") {
                        ThirdLoginActivity.start(context, ThirdLoginActivity.EnterIndex.PAY)
                    } else {
                        beandata?.run {
                            for (i in coin_list.indices) {
                                if (coin_list[i].chargeIndex != -99) {
                                    if (coin_list[i].getpayid() == "coins09") {
                                        if (SpUtil.CUR_PAYTYPE == SpUtil.PayType.GOOGLE) {
                                            GooglePay.GetInstance().PayForCoin(
                                                activity,
                                                coin_list[i].getpayid(),
                                                coin_list[i].chargeIndex,
                                                coin_list[i].rmb
                                            )
                                            AppContext.sInstance.once = false
                                            dismiss()
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
            findViewById<TextView>(R.id.more).setOnClickListener {
                TopUpActivity.start(activity)
            }
        }
    }

}