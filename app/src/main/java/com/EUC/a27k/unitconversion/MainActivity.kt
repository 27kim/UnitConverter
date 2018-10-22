package com.EUC.a27k.unitconversion

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    lateinit var type: String
    var value: Long = 0
    val PREFS_FILENAME = "UnitConverter"

    //NavigationDrawer Toggle
    var drawerToggle: ActionBarDrawerToggle? = null

    val sdf = SimpleDateFormat("yyyy/MM/dd")

    //Admob
    lateinit var mAdView : AdView

    companion object {
        val MMBTU_VALUE: Double = 1_000.00000
        val DEKA_VALUE: Double = 1_000.23906
        val GKCAL_VALUE: Double = 252.16440
        val TON_VALUE: Double = 19.33776
        val MCF_VALUE: Double = 980.39215
        val GJ_VALUE: Double = 1_055.05600
        val KW_VALUE: Double = 293_071.070172

        val MMBTU_TEXT = "mmbtu"
        val DEKA_TEXT = "decatherm"
        val GCAL_TEXT = "Gcal"
        val TON_TEXT = "ton(HHV basis)"
        val MCF_TEXT = "Mcf"
        val GJ_TEXT = "GJ"
        val KW_TEXT = "kWh"

        val IS_FIRST_TIME = "IS_FIRST_TIME"
        val INITIAL_DATE = "INITIAL_DATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Drawer 초기화
        initDrawer()

        //기간 만료 설정
//        checkExpiredDate()

        type = spinner.selectedItem?.toString() ?: "mmbtu"
        value = editText.text.toString().toLong()

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                convertUnit()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                convertUnit()
            }
        })

        val items = arrayOf(MMBTU_TEXT, DEKA_TEXT, GCAL_TEXT, TON_TEXT, MCF_TEXT, GJ_TEXT, KW_TEXT)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        spinner.setOnItemSelectedListener(this)
        spinner.adapter = spinnerAdapter

        btnChange.setOnClickListener(this)
        //Admob 초기화
        initAdmob()

        main_drawer_view.setNavigationItemSelectedListener {

            if(it.title.equals("About")){
                drawer.closeDrawers()

//                toast(it.toString())
                var intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }

            true
        }
    }

    private fun initAdmob() {
        MobileAds.initialize(this, "ca-app-pub-7918453442102652~5030918028")

        //Admob
        val adView = AdView(this)
        adView.adSize = AdSize.BANNER
        adView.adUnitId = "ca-app-pub-7918453442102652/9654357613"

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

//        mAdView.adListener = object: AdListener() {
//            override fun onAdLoaded() {
//                Toast.makeText(applicationContext, "onAdLoaded", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAdFailedToLoad(errorCode : Int) {
//                // Code to be executed when an ad request fails.
//                Toast.makeText(applicationContext, "onAdFailedToLoad ${errorCode}", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//                Toast.makeText(applicationContext, "onAdOpened", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Toast.makeText(applicationContext, "onAdLeftApplication", Toast.LENGTH_LONG).show()
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//                Toast.makeText(applicationContext, "onAdClosed", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    private fun checkExpiredDate() {

        val prefs = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        if(prefs.getBoolean(IS_FIRST_TIME, true)){
            val editor = prefs!!.edit()
            editor.putBoolean(IS_FIRST_TIME,false).apply()

            val currentDate = sdf.format(Date())

            editor.putString(INITIAL_DATE, currentDate).apply()
        }else{
            val sdf = SimpleDateFormat("yyyy/MM/dd")
            val currentDate = sdf.format(Date())
//            val currentDate = "2018/07/31"
            val initialDate = prefs.getString(INITIAL_DATE,"")

            val diffInMillies = Math.abs(sdf.parse(currentDate).time - sdf.parse(initialDate).time )

            val diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
            if(diff>1){
                // build alert dialog
                val dialogBuilder = AlertDialog.Builder(this)

                // set message of alert dialog
                dialogBuilder.setMessage("You can't use over 60 days")
                        // if the dialog is cancelable
                        .setCancelable(false)
                        .setTitle("AlertDialogExample")
                        // positive button text and action
                        .setPositiveButton("OK", DialogInterface.OnClickListener {
                            dialog, id -> finish()
                        })
                        // negative button text and action
//                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
//                            dialog, id -> dialog.cancel()
//                        })
                        .create()
                        .show()
            }

        }
    }

    private fun convertUnit() {
        type = spinner.selectedItem?.toString() ?: MMBTU_TEXT

        var value = editText.text.toString()

        var val2: Double = if (value == "") 0.0 else value.toDouble()

        val baseType = when (type) {
            MMBTU_TEXT -> MMBTU_VALUE
            DEKA_TEXT -> DEKA_VALUE
            GCAL_TEXT -> GKCAL_VALUE
            TON_TEXT -> TON_VALUE
            MCF_TEXT -> MCF_VALUE
            GJ_TEXT -> GJ_VALUE
            KW_TEXT -> KW_VALUE
            else -> 0.0
        }
        val df = DecimalFormat("#,###.##")
        df.roundingMode = RoundingMode.CEILING
        mmbtuValue.text = df.format((MMBTU_VALUE / baseType * val2)).toString()
        dekaValue.text = df.format((DEKA_VALUE / baseType * val2)).toString()
        gkcalValue.text = df.format((GKCAL_VALUE / baseType * val2)).toString()
        tonValue.text = df.format((TON_VALUE / baseType * val2)).toString()
        mcfValue.text = df.format((MCF_VALUE / baseType * val2)).toString()
        gjValue.text = df.format((GJ_VALUE / baseType * val2)).toString()
        kwValue.text = df.format((KW_VALUE / baseType * val2)).toString()
    }

    override fun onClick(p0: View?) {
        convertUnit()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        convertUnit()
    }

    private fun initDrawer() {
        //NavigationDrawer 초기화
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(true)

        supportActionBar!!.title = "ENERGY UNIT CONVERTER"

        drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close)
        drawerToggle!!.syncState()
    }

    //아래 부분 없으면 토글 메뉴 선택해도 아무것도 안나옴
    override fun onOptionsItemSelected(item: android.view.MenuItem?): Boolean {
        if (drawerToggle!!.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }



}