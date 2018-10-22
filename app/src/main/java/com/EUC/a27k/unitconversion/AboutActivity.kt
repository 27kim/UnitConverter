package com.EUC.a27k.unitconversion

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_about.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton


class AboutActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        alert("would you like to email developer?", "Energy Unit Converter") {
            yesButton { val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "EnergyUnitConverter@gmail.com", null))
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "regarding Energy Unit Converter app")
                startActivity(Intent.createChooser(emailIntent, "Send email.. ."))}
        }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
//        toast("AboutActivity")

        animation_view.setAnimation("done.json")
        animation_view.playAnimation()
        animation_view.setOnClickListener(this)

        val pInfo = packageManager.getPackageInfo(packageName, 0)
        val version = pInfo.versionName

        vesionText.text = "Energy Unit Converter v ${version}"

        infoText.text = "If you have any questions, \nclick on the icon above!"

        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }
}