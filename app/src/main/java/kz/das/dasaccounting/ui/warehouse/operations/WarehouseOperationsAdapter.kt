package kz.das.dasaccounting.ui.warehouse.operations

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemOperationActionBinding
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
            ACTION to OperationOfficeInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationTransportInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false))
        )[viewType]
            ?: OperationTransportInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false))
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
        this.operations.removeAll(items)
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    fun putItems(items: List<OperationAct>) {
        this.operations.removeAll(items)
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

    inner class OperationTransportInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<TransportInventory>(itemBinding) {
        override fun bind(item: TransportInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.model
                this.ivAction.setTsTypeImage(item)
                this.ivStatePending.setImageResource(R.drawable.ic_arrow_next_mini)
                this.ivStatePending.visibility = View.VISIBLE
                this.llAction.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onTransportInventory(item)
                }
            }
        }
    }

    inner class OperationOfficeInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<OfficeInventory>(itemBinding) {
        override fun bind(item: OfficeInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.name
                this.ivAction.setImageResource(R.drawable.ic_inventory)
                this.ivStatePending.setImageResource(R.drawable.ic_arrow_next_mini)
                this.ivStatePending.visibility = View.VISIBLE
                this.llAction.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onOfficeInventory(item)
                }
            }
        }
    }

}