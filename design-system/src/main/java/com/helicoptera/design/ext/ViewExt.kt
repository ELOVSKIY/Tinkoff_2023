package com.helicoptera.design.ext

import android.content.Context

fun Int.toPx(context: Context): Int {
    return (this * context.getResources().getDisplayMetrics().density).toInt()
}
