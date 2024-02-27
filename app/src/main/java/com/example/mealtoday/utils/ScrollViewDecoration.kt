package com.example.mealtoday.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView

class ScrollViewDecoration : ScrollView, ViewTreeObserver.OnGlobalLayoutListener {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
        overScrollMode = OVER_SCROLL_NEVER
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    var isHeader: View? = null
        set(value) {
            field = value
            field?.let {
                it.translationZ = 1f
                it.setOnClickListener { _ ->
                    this.smoothScrollTo(scrollX, it.top)
                    callStickListener()
                }
            }
        }

    var stickListener: (View) -> Unit = {}
    var freeListener: (View) -> Unit = {}

    private var isHeaderSticky = false
    private var headerInitPosition = 0f

    override fun onGlobalLayout() {
        headerInitPosition = isHeader?.top?.toFloat() ?: 0f
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (t > headerInitPosition) {
            stickHeader(t - headerInitPosition)
        } else {
            freeHeader()
        }
    }

//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//        return when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                // Intercept touch event when scrolling for smooth scroll
//                smoothScrollBy(0, 0)
//                super.onTouchEvent(ev)
//            }
//            else -> super.onTouchEvent(ev)
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
//        return when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//                smoothScrollBy(0, 0)
//                super.onInterceptTouchEvent(ev)
//            }
//            else -> super.onInterceptTouchEvent(ev)
//        }
//    }

    private fun stickHeader(position: Float) {
        isHeader?.translationY = position
        callStickListener()
    }

    private fun callStickListener() {
        if (!isHeaderSticky) {
            stickListener(isHeader ?: return)
            isHeaderSticky = true
        }
    }

    private fun freeHeader() {
        isHeader?.translationY = 0f
        callFreeListener()
    }

    private fun callFreeListener() {
        if (isHeaderSticky) {
            freeListener(isHeader ?: return)
            isHeaderSticky = false
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

}