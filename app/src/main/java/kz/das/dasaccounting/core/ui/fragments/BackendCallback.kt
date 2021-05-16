package kz.das.dasaccounting.core.ui.fragments

interface BackendCallback {

    fun showSuccess(title: String?, message: String?)

    fun showError(title: String?, message: String?)

    fun showAwait(title: String?, message: String?)

    fun onSaveRequire(title: String?, message: String?, data: Any?)
}