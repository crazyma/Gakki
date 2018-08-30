package com.dcard.gakki.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dcard.gakki.R
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {

    var view: View? = null
//    var imageView: ImageView? = null

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        view = LayoutInflater.from(this)
                .inflate(R.layout.layout_marker,null)
//        imageView = view!!.findViewById(R.id.imageView)
//        baseLayout.addView(view, 100,100)

    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            sampleImageView.setImageResource(R.mipmap.ic_launcher)
            sampleTextView.text = "3"
        },2000)


        Handler().postDelayed({
//            view?.run {
                loadBitmapFromView(sampleLayout).also {
                    testImageView.setImageBitmap(it)
                }
//            }
        },4000)
    }

    private fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(
                v.layoutParams.width, v.layoutParams.height, Bitmap.Config.ARGB_8888)

        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

}