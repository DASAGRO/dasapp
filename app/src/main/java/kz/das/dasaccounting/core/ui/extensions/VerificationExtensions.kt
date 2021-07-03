package kz.das.dasaccounting.core.ui.extensions

import androidx.fragment.app.Fragment
import co.infinum.goldfinger.Goldfinger
import kz.das.dasaccounting.R


fun Fragment.verifyToInit(funcOnSuccess: () -> Unit, funOnFail: () -> Unit, funcOnError: () -> Unit) {
    val goldFinger = Goldfinger.Builder(requireContext()).build()
    if (goldFinger.canAuthenticate()) {
        val params = Goldfinger.PromptParams.Builder(requireActivity())
            .title(getString(R.string.confirm_with_finger))
            .negativeButtonText(getString(R.string.cancel))
            .description("")
            .subtitle("")
            .build()
        goldFinger.authenticate(params, object : Goldfinger.Callback {
            override fun onError(e: Exception) {
                funcOnError()
            }
            override fun onResult(result: Goldfinger.Result) {
                if (result.reason() == Goldfinger.Reason.AUTHENTICATION_SUCCESS) {
                    funcOnSuccess()
                } else if (result.reason() == Goldfinger.Reason.AUTHENTICATION_FAIL) {
                    funOnFail()
                }
            }
        })
    } else {
        funcOnSuccess()
    }
}