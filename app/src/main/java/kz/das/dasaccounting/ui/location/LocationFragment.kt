package kz.das.dasaccounting.ui.location

import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentLocationBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LocationFragment: BaseFragment<LocationVM, FragmentLocationBinding>() {

    companion object {
        fun getScreen() = DasAppScreen(LocationFragment())
    }

    override val mViewModel: LocationVM by viewModel()

    override fun getViewBinding() = FragmentLocationBinding.inflate(layoutInflater)

    override fun setupUI() {

    }


}