package com.example.a27k.unitconversion

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.toast
import android.os.Looper.loop
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_about.*


class AboutActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        toast("AboutActivity")

        animation_view.setAnimation("done.json")
        animation_view.playAnimation()

        infoText.text = "If you have any questions, \nfeel free to contact me at \ndoohyeonkim@gmail.com"


        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }
}