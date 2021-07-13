package kz.das.dasaccounting.ui.warehouse

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemOperationActionBinding
import kz.das.dasaccounting.databinding.ItemOperationInitBinding
import kz.das.dasaccounting.databinding.ItemSegmentBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.warehouse.WarehouseInventory

class WarehousesAdapter(val context: Context, private var operations: ArrayList<OperationAct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var warehouseOperationsAdapterEvent: OnWarehouseOperationsAdapterEvent? = null

    companion object {
        private const val HEAD = 0
        private const val ACTION = 1
        private const val OPERATION = 2
    }

    interface OnWarehouseOperationsAdapterEvent {
        fun onOperationInit(operationAct: OperationAct)
        fun onInventoryTransfer(warehouseInventory: WarehouseInventory)
    }

    fun setWarehouseOperationsAdapterEvent(WarehouseOperationsAdapterEvent: OnWarehouseOperationsAdapterEvent) {
        this.warehouseOperationsAdapterEvent = WarehouseOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            HEAD to OperationHeadViewHolder(ItemSegmentBinding.inflate(layoutInflater, parent, false)),
            ACTION to OperationInitViewHolder(ItemOperationInitBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationWarehouseInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)))[viewType]
            ?: OperationWarehouseInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        when (holder) {
            is OperationHeadViewHolder ->
                holder.bind(item as OperationHead, position)
            is OperationInitViewHolder ->
                holder.bind(item as OperationInit, position)
            is OperationWarehouseInventoryViewHolder ->
                holder.bind(item as WarehouseInventory, position)
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

    fun clearOperations() {
        this.operations.removeAll { it is WarehouseInventory }
        notifyDataSetChanged()
    }

    fun removeHead(head: OperationHead) {
        val operationHeads: ArrayList<OperationHead> = arrayListOf()
        this.operations.forEach {
            if (it is OperationHead && it.name == head.name) {
                operationHeads.add(it)
            }
        }
        this.operations.removeAll(operationHeads)
        notifyDataSetChanged()
    }

    fun putItems(items: ArrayList<OperationAct>) {
        this.operations.removeAll(items)
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is OperationHead -> HEAD
            is OperationInit -> ACTION
            is WarehouseInventory -> OPERATION
            else -> OPERATION
        }
    }

    inner class OperationHeadViewHolder internal constructor(private val itemBinding: ItemSegmentBinding) : BaseViewHolder<OperationHead>(itemBinding) {
        override fun bind(item: OperationHead, position: Int) {
            this.itemBinding.run {
                this.tvSegment.text = item.name
            }
        }
    }

    inner class OperationInitViewHolder internal constructor(private val itemBinding: ItemOperationInitBinding) : BaseViewHolder<OperationInit>(itemBinding) {
        override fun bind(item: OperationInit, position: Int) {
            this.itemBinding.run {
                this.actionName = item.name
                this.llAction.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onOperationInit(item)
                }
            }
        }
    }

    inner class OperationWarehouseInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<WarehouseInventory>(itemBinding) {
        override fun bind(item: WarehouseInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.name
                this.ivAction.setImageResource(R.drawable.ic_warehouse)
                this.ivStatePending.isVisible = false
                this.llAction.setOnClickListener {
                    warehouseOperationsAdapterEvent?.onInventoryTransfer(item)
                }
            }
        }
    }

    
}