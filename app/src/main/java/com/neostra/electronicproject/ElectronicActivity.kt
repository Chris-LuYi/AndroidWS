package com.neostra.electronicproject

import android.content.Context
import android.os.PowerManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.neostra.electronic.Electronic
import com.neostra.electronic.ElectronicCallback
import kotlinx.android.synthetic.main.activity_electronic_test.*
import java.math.BigDecimal
import java.util.*

/**
 * @author njmsir
 * Created by njmsir on 2019/5/16.
 */
class ElectronicActivity : BaseActivity(), View.OnClickListener, OnClickListener, ElectronicCallback {
    private lateinit var mTvWeight: TextView
    private lateinit var mTvWeightSteady: TextView
    private lateinit var mBtnZero: Button
    private lateinit var mBtnPeel: Button
    private lateinit var lock: PowerManager.WakeLock
    private lateinit var rvMenu: RecyclerView
    private lateinit var layoutManager: GridLayoutManager
    private val TAG = this::class.java.simpleName
    /**
     * 称重接收缓存
     */
    private var mWeightState: String? = null
    private val menuList = ArrayList<MenuBean>()
    private lateinit var mTvPrice: TextView
    private var mElectronic: Electronic? = null
    private var devicePath: String = "/dev/ttyS4"

    override fun getNavigation(): View? {
        return null
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_electronic_test
    }

    override fun onActivityCreate() {
        initView()
        initSerialPort()
        initProductData()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_peel -> {
                mElectronic?.removePeel()
                updatePrice("0.00")
            }
            R.id.btn_zero -> {
                mElectronic?.turnZero()
                updatePrice("0.00")
            }
            R.id.btn_manual_peel -> {
                val peel = et_peel_weight.text.toString()
                if (TextUtils.isEmpty(peel)) {
                    Toast.makeText(this, "请输入正确的去皮值", Toast.LENGTH_SHORT).show()
                } else {
                    val p = peel.toInt()
                    if (0 == p) {
                        Toast.makeText(this, "请输入正确的去皮值", Toast.LENGTH_SHORT).show()
                    } else {
                        mElectronic?.manualPeel(p)
                    }
                }
            }
        }
    }

    override fun electronicStatus(weight: String, weightStatus: String) {
        runOnUiThread {
            when (weightStatus) {
                "46" -> {
                    //超重
                    mTvWeightSteady.text = "(超重)"
                    mTvWeight.text = weight
                }

                "53" -> {
                    //稳定
                    mTvWeightSteady.text = "(稳定)"
                    mTvWeight.text = weight
                }

                "55" -> {
                    //未稳定
                    mTvWeightSteady.text = "(未稳定)"
                    mTvWeight.text = weight
                }

                "56" -> {
                    //手动去皮成功
                    Log.i("njm", "手动去皮成功")
                }

                "57" -> {
                    //手动去皮失败
                    Log.i("njm", "手动去皮失败")
                }
            }
            mWeightState = weightStatus
        }
    }

    override fun onClick(view: View?, pos: Int, o: Any?) {
        if ("46" == mWeightState) {
            Toast.makeText(this, "已超载请重新测量", Toast.LENGTH_SHORT).show()
        } else if ("53" == mWeightState) {
            if (menuList.size > 0) {
                val bean = menuList[pos]
                val v = BigDecimal(bean.price * java.lang.Double.parseDouble(mTvWeight.text.toString()) * 2)
                    .setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toDouble()

                updatePrice(v.toString())
            }
        } else if ("55" == mWeightState) {
            Toast.makeText(this, "称重未稳定请等待", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePrice(price: String) {
        val p = price
        val str = String.format(getString(R.string.total_price), p)
        val start = str.indexOf(":") + 2
        val end = start + p.length

        val style = SpannableStringBuilder(str)
        style.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.red)),
            start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        style.setSpan(AbsoluteSizeSpan(52, true), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        mTvPrice.text = style
    }

    private fun initSerialPort() {
        mElectronic = Electronic.Builder()
            .setDevicePath(devicePath)
            .setReceiveCallback(this)
            .builder()
    }

    private fun initView() {
        mTvWeight = tv_weight_value
        mTvWeightSteady = tv_weight_steady
        mBtnZero = btn_zero
        mBtnPeel = btn_peel
        mTvPrice = tv_price

        rvMenu = rv_menu
        layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        val adapter = ElectronicMenuAdapter(menuList, rvMenu.layoutParams.width).apply {
            setOnClickListener(this@ElectronicActivity)
        }
        rvMenu.apply {
            isNestedScrollingEnabled = true
            setHasFixedSize(true)
            layoutManager = this@ElectronicActivity.layoutManager
            setAdapter(adapter)
        }
        mBtnPeel.setOnClickListener(this)
        mBtnZero.setOnClickListener(this)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            false
        } else super.dispatchKeyEvent(event)
    }

    private fun initProductData() {
        val product = intArrayOf(
            R.drawable.ic_apple,
            R.drawable.ic_corn,
            R.drawable.ic_grape,
            R.drawable.ic_mango,
            R.drawable.ic_orange,
            R.drawable.ic_watermelon
        )

        val productName = arrayOf(
            getString(R.string.apple),
            getString(R.string.corn),
            getString(R.string.grape),
            getString(R.string.mango),
            getString(R.string.orange),
            getString(R.string.watermelon)
        )

        val productPrice = doubleArrayOf(5.5, 6.7, 9.2, 2.6, 3.3, 10.9)

        for (i in product.indices) {
            val bean = MenuBean()
            bean.name = productName[i]
            bean.resId = product[i]
            bean.price = productPrice[i]
            menuList.add(bean)
        }
        val pm = applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager
        lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG)
    }

    private fun destroy() {
        mElectronic?.closeElectronic()
    }
}