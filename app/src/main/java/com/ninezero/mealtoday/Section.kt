package com.ninezero.mealtoday

import androidx.annotation.IntDef

@IntDef(
    DEFAULT,
    HOT_MEAL,
    DRINK
)

@Retention(AnnotationRetention.SOURCE)
annotation class Section

const val DEFAULT = 0
const val HOT_MEAL = 1
const val DRINK = 2

