package com.example.mealtoday.utils

import android.animation.ValueAnimator
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.animation.doOnEnd
import androidx.core.content.getSystemService
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.progressindicator.BaseProgressIndicator

const val ANIM_DURATION_SHOW = 400L
const val ANIM_DURATION_HIDE = 300L

fun View.doOnApplyWindowInsets(windowInsetsListener: (insetView: View, windowInsets: WindowInsetsCompat,
                                                      initialPadding: Insets, initialMargins: Insets
) -> Unit) {
    val initialPadding = Insets.of(paddingLeft, paddingTop, paddingRight, paddingBottom)
    val initialMargins = Insets.of(marginLeft, marginTop, marginRight, marginBottom)

    ViewCompat.setOnApplyWindowInsetsListener(this) { insetView, windowInsets ->
        windowInsets.also {
            windowInsetsListener(insetView, windowInsets, initialPadding, initialMargins)
        }
    }

    addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            v.requestApplyInsets()
        }

        override fun onViewDetachedFromWindow(v: View) = Unit
    })

    if (isAttachedToWindow) {
        requestApplyInsets()
    }
}

fun NavigationBarView.show() {
    if (this is NavigationRailView) return
    if (isVisible) return

    val parent = parent as ViewGroup
    if (!isLaidOut) {
        measure(
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.AT_MOST)
        )
        layout(parent.left, parent.height - measuredHeight, parent.right, parent.height)
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    drawable.setBounds(left, parent.height, right, parent.height + height)
    parent.overlay.add(drawable)
    ValueAnimator.ofInt(parent.height, top).apply {
        duration = ANIM_DURATION_SHOW
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.accelerate_decelerate
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
            isVisible = true
        }
        start()
    }
}

fun NavigationBarView.hide() {
    if (this is NavigationRailView) return
    if (isGone) return

    if (!isLaidOut) {
        isGone = true
        return
    }

    val drawable = BitmapDrawable(context.resources, drawToBitmap())
    val parent = parent as ViewGroup
    drawable.setBounds(left, top, right, bottom)
    parent.overlay.add(drawable)
    isGone = true
    ValueAnimator.ofInt(top, parent.height).apply {
        duration = ANIM_DURATION_HIDE
        interpolator = AnimationUtils.loadInterpolator(
            context,
            android.R.interpolator.accelerate_decelerate
        )
        addUpdateListener {
            val newTop = it.animatedValue as Int
            drawable.setBounds(left, newTop, right, newTop + height)
        }
        doOnEnd {
            parent.overlay.remove(drawable)
        }
        start()
    }
}

fun View.focusAndShowKeyboard() {

    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                val imm = context.getSystemService<InputMethodManager>()
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()

    if (hasWindowFocus()) {
        showTheKeyboardNow()
    } else {
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}