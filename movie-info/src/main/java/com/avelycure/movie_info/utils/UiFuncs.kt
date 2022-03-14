package com.avelycure.movie_info.utils

import kotlin.math.roundToInt

fun Double.setPrecision(prec: Int): Double {
    var p = 1.0

    for (i in 1..prec)
        p *= 10

    return ((this * p).roundToInt()) / p
}

fun getMoney(budget: Int): String {
    if (budget < 0)
        return ""
    return when (budget) {
        in 1..1000001 -> "${budget / 1000} thousand dollars"
        else -> "${(budget / 1000000.0).toInt()} million dollars"
    }
}