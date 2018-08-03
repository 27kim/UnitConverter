package com.example.a27k.unitconversion

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.DAYS





class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    lateinit var type: String
    var value: Long = 0
    val PREFS_FILENAME = "UnitConverter"

    //NavigationDrawer Toggle
    var drawerToggle: ActionBarDrawerToggle? = null

    val sdf = SimpleDateFormat("yyyy/MM/dd")

    companion object {
        val MMBTU_VALUE: Double = 1000.00000
        val DEKA_VALUE: Double = 1000.23906
        val GKCAL_VALUE: Double = 252.16440
        val TON_VALUE: Double = 19.33776
        val MCF_VALUE: Double = 964.32015
        val GJ_VALUE: Double = 1_055.05600

        val MMBTU_TEXT = "mmbtu"
        val DEKA_TEXT = "decatherm"
        val GCAL_TEXT = "Gcal"
        val TON_TEXT = "ton(HHV basis)"
        val MCF_TEXT = "Mcf"
        val GJ_TEXT = "GJ"

        val IS_FIRST_TIME = "IS_FIRST_TIME"
        val INITIAL_DATE = "INITIAL_DATE"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDrawer()

        checkExpiredDate()

        type = spinner.selectedItem?.toString() ?: "mmbtu"
        value = editText.text.toString().toLong()

        val items = arrayOf(MMBTU_TEXT, GCAL_TEXT, TON_TEXT, MCF_TEXT, GJ_TEXT)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        spinner.setOnItemSelectedListener(this)
        spinner.adapter = spinnerAdapter

        btnChange.setOnClickListener(this)
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
            else -> 0.0
        }
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        mmbtuValue.text = df.format((MMBTU_VALUE / baseType * val2)).toString()
        dekaValue.text = df.format((DEKA_VALUE / baseType * val2)).toString()
        gkcalValue.text = df.format((GKCAL_VALUE / baseType * val2)).toString()
        tonValue.text = df.format((TON_VALUE / baseType * val2)).toString()
        mcfValue.text = df.format((MCF_VALUE / baseType * val2)).toString()
        gjValue.text = df.format((GJ_VALUE / baseType * val2)).toString()
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
        supportActionBar!!.setDisplayShowTitleEnabled(false)

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