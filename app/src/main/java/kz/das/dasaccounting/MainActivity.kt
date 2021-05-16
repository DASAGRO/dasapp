package kz.das.dasaccounting

import kz.das.dasaccounting.core.ui.activities.BaseActivity
import kz.das.dasaccounting.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.android.pure.AppNavigator

class MainActivity: BaseActivity<MainVM, ActivityMainBinding>() {

    private val cicerone = Cicerone.create()
    private val navigator by lazy {
        AppNavigator(this, R.id.container)
    }

    override val mViewModel: MainVM by viewModel()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)


}