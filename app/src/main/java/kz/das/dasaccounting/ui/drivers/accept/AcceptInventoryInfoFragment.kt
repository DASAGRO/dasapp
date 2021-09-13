package kz.das.dasaccounting.ui.drivers.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptBinding
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.ui.drivers.setTsTypeImage
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptInventoryInfoFragment: BaseFragment<AcceptInventoryInfoVM, FragmentInventoryAcceptBinding>() {

    companion object {

        private const val DRIVER_INVENTORY = "inventory"

        fun getScreen(transportInventory: TransportInventory) = DasAppScreen(AcceptInventoryInfoFragment())
            .apply {
                val args = Bundle()
                args.putParcelable(DRIVER_INVENTORY, transportInventory)
                this.setArgs(args)
            }
    }

    override val mViewModel: AcceptInventoryInfoVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptBinding.inflate(layoutInflater)

    override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setTransportInventory(getOfficeInventory())

        mViewBinding.apply {
            toolbar.setNavigationOnClickListener {
                requireRouter().exit()
            }

            btnAccept.setOnClickListener {
                getOfficeInventory()?.let {
                    it.dateTime = System.currentTimeMillis()
                    it.latitude = mViewModel.getLocation().lat
                    it.longitude = mViewModel.getLocation().long
                    requireRouter().replaceScreen(AcceptTransportCheckFragment.getScreen(it))
                }
            }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getTransportInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setTsTypeImage(it)
                mViewBinding.tvInventoryTitle.text = it.model
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.gov_number) +
                            " " + it.stateNumber)
            }
        })
    }

    private fun getOfficeInventory(): TransportInventory? {
        return arguments?.getParcelable(DRIVER_INVENTORY)
    }

}