package com.example.store.presentation.base

import android.view.View
import androidx.fragment.app.Fragment
import com.example.store.presentation.MainActivity
import com.example.store.R
import com.example.store.core.Failure
import com.example.store.presentation.myToast

abstract class BaseFragment : Fragment() {
    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) =
        with(activity) { if (this is MainActivity) this.findViewById<View>(R.id.fl_progress)?.visibility = viewStatus }

    fun showError(failure: Failure) {
        // Here you could show spefici message froma  failure, in the future
        requireContext().myToast(getString(R.string.general_error))
    }
}
