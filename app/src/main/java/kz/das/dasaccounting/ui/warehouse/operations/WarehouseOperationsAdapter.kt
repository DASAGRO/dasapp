package kz.das.dasaccounting.ui.warehouse.operations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemSearchActionBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.drivers.TransportInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.ui.drivers.setTsTypeImage

class WarehouseOperationsAdapter(val context: Context, private var operations: ArrayList<OperationAct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var warehouseOperationsAdapterEvent: OnWarehouseOperationsAdapterEvent? = null

    companion object {
        private const val ACTION = 1
        private const val OPERATION = 2
    }

    interface OnWarehouseOperationsAdapterEvent {
        fun onOfficeInventory(officeInventory: OfficeInventory)
        fun onTransportInventory(transportInventory: TransportInventory)
    }

    fun setWarehouseOperationsAdapterEvent(WarehouseOperationsAdapterEvent: OnWarehouseOperationsAdapterEvent) {
        this.warehouseOperationsAdapterEvent = WarehouseOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            ACTION to OperationOfficeInventoryViewHolder(ItemSearchActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationTransportInventoryViewHolder(ItemSearchActionBinding.inflate(layoutInflater, parent, false))
        )[viewType]
            ?: OperationTransportInventoryViewHolder(ItemSearchActionBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        when (holder) {
            is OperationTransportInventoryViewHolder ->
                holder.bind(item as TransportInventory, position)
            is OperationOfficeInventoryViewHolder ->
                holder.bind(item as OfficeInventory, position)
        }
    }

    override fun getItemCount() = operations.size

    fun clearItems(items: List<OperationAct>) {
        this.operations.removeAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(item: OperationAct) {
        this.operations.remove(item)
        notifyDataSetChanged()
    }

    fun putItems(items: ArrayList<OperationAct>) {
        this.operations.clear()
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    fun putItems(items: List<OperationAct>) {
        this.operations.clear()
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is OfficeInventory -> ACTION
            is TransportInventory -> OPERATION
            else -> OPERATION
        }
    }

    inner class OperationTransportInventoryViewHolder internal constructor(private val itemBinding: ItemSearchActionBinding) : BaseViewHolder<TransportInventory>(itemBinding) {
        override fun bind(item: TransportInventory, position: Int) {
            this.itemBinding.run {
                this.tvSearchTitle.text = (item.model + " " + item.stateNumber)
                this.ivItemSearch.setTsTypeImage(item)
                this.llSearchItem.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onTransportInventory(item)
                }
            }
        }
    }

    inner class OperationOfficeInventoryViewHolder internal constructor(private val itemBinding: ItemSearchActionBinding) : BaseViewHolder<OfficeInventory>(itemBinding) {
        override fun bind(item: OfficeInventory, position: Int) {
            this.itemBinding.run {
                this.tvSearchTitle.text = item.name
                this.ivItemSearch.setImageResource(R.drawable.ic_inventory)
                this.llSearchItem.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onOfficeInventory(item)
                }
            }
        }
    }

}