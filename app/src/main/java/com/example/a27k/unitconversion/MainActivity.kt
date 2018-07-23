package com.example.a27k.unitconversion

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.math.RoundingMode
import java.text.DecimalFormat


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {
    override fun onClick(p0: View?) {
        convertUnit()
    }

    lateinit var type: String
    var value: Long = 0



    companion object {
        val MMBTU_VALUE : Double = 1000.00000
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        type = spinner.selectedItem?.toString() ?: "mmbtu"
        value = editText.text.toString().toLong()

        val items = arrayOf(MMBTU_TEXT, GCAL_TEXT, TON_TEXT, MCF_TEXT, GJ_TEXT)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        spinner.setOnItemSelectedListener(this)
        spinner.adapter = spinnerAdapter

        btnChange.setOnClickListener(this)
    }

    private fun convertUnit() {
        type = spinner.selectedItem?.toString() ?: MMBTU_TEXT
        
        var value = editText.text.toString()

        var val2 : Double = if (value == "") 0.0 else value.toDouble()

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

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        convertUnit()
    }
}