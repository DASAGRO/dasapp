package kz.das.dasaccounting.core.ui.fragments

interface ViewCallback : BackendCallback {

    fun observeLiveData()

    fun restartActivity()

    fun onLogout()

    fun showLoading()

    fun hideLoading()

    fun navigateBack()
}