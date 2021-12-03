package kz.das.dasaccounting.ui.office.accept

import android.os.Bundle
import androidx.lifecycle.Observer
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.navigation.DasAppScreen
import kz.das.dasaccounting.core.navigation.requireRouter
import kz.das.dasaccounting.core.ui.fragments.BaseFragment
import kz.das.dasaccounting.databinding.FragmentInventoryAcceptBinding
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptInventoryInfoFragment: BaseFragment<AcceptInventoryInfoVM, FragmentInventoryAcceptBinding>() {

    companion object {

        private const val OFFICE_INVENTORY = "inventory"

        fun getScreen(officeInventoryAccept: OfficeInventory) = DasAppScreen(AcceptInventoryInfoFragment())
            .apply {
                val args = Bundle()
                args.putParcelable(OFFICE_INVENTORY, officeInventoryAccept)
                this.setArgs(args)
            }
    }

    override val mViewModel: AcceptInventoryInfoVM by viewModel()

    override fun getViewBinding() = FragmentInventoryAcceptBinding.inflate(layoutInflater)

        override fun setupUI(savedInstanceState: Bundle?) {
        mViewModel.setOfficeInventory(getOfficeInventory())

        mViewBinding.apply {
            toolbar.setNavigationOnClickListener { requireRouter().exit() }

            btnAccept.setOnClickListener { mViewModel.acceptInventory("") }
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        mViewModel.getOfficeInventory().observe(viewLifecycleOwner, Observer {
            it?.let {
                mViewBinding.ivInventory.setImageResource(R.drawable.ic_inventory)
                mViewBinding.tvInventoryTitle.text = it.name
                mViewBinding.tvInventoryDesc.text =
                    (getString(R.string.inventory_total_quantity) +
                            " " + it.quantity +
                            " " + it.type + "\n" +
                            String.format(getString(R.string.inventory_sender_name), it.senderName))
            }
        })

        mViewModel.isOfficeInventoryAccepted().observe(viewLifecycleOwner, Observer {
            if (it) { openInventoryCheck() }
        })

        mViewModel.isOnAwait().observe(viewLifecycleOwner, Observer {
            if (it) { openInventoryCheck() }
        })
    }

    private fun openInventoryCheck() {
        getOfficeInventory()?.let {
            it.date = System.currentTimeMillis()
            it.latitude = mViewModel.getLocation().lat
            it.longitude = mViewModel.getLocation().long
            requireRouter().replaceScreen(AcceptInventoryCheckFragment.getScreen(it))
        }
    }

    private fun getOfficeInventory(): OfficeInventory? {
        return arguments?.getParcelable(OFFICE_INVENTORY)
    }

}