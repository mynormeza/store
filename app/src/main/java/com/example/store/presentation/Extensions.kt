package com.example.store.presentation

import android.content.Context
import android.widget.Toast

fun Context.myToast(text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, length).show()
}
