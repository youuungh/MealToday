package com.ninezero.mealtoday.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

fun Context.openUrl(url: String) {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = url.toUri()
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(i)
}


fun Fragment.openUrl(url: String) {
    requireContext().openUrl(url)
}