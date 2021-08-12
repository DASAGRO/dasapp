package kz.das.dasaccounting.ui.office

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
import kz.das.dasaccounting.domain.data.office.OfficeAcceptedInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeSentInventory

class OfficeOperationsAdapter(val context: Context, private var operations: ArrayList<OperationAct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent? = null

    companion object {
        private const val HEAD = 0
        private const val ACTION = 1
        private const val OPERATION = 2
        private const val OPERATION_SENT_AWAIT = 3
        private const val OPERATION_ACCEPTED_AWAIT = 4
    }

    interface OnOfficeOperationsAdapterEvent {
        fun onOperationAct(operationAct: OperationAct)
        fun onOperationInit(operationAct: OperationAct)
        fun onInventoryTransfer(officeInventory: OfficeInventory)
    }

    fun setOfficeOperationsAdapterEvent(officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent) {
        this.officeOperationsAdapterEvent = officeOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            HEAD to OperationHeadViewHolder(ItemSegmentBinding.inflate(layoutInflater, parent, false)),
            ACTION to OperationInitViewHolder(ItemOperationInitBinding.inflate(layoutInflater, parent, false)),
            OPERATION_ACCEPTED_AWAIT to OperationOfficeAcceptAwaitInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_SENT_AWAIT to OperationOfficeSentAwaitInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationOfficeInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)))[viewType]
            ?: OperationOfficeInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        when (holder) {
            is OperationHeadViewHolder ->
                holder.bind(item as OperationHead, position)
            is OperationInitViewHolder ->
                holder.bind(item as OperationInit, position)
            is OperationOfficeInventoryViewHolder ->
                holder.bind(item as OfficeInventory, position)
            is OperationOfficeAcceptAwaitInventoryViewHolder ->
                holder.bind(item as OfficeAcceptedInventory, position)
            is OperationOfficeSentAwaitInventoryViewHolder ->
                holder.bind(item as OfficeSentInventory, position)
        }
    }

    override fun getItemCount() = operations.size

    fun putItems(items: ArrayList<OperationAct>) {
        this.operations.clear()
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    fun clearOperationItems(items: List<OfficeInventory>) {
        if (this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearOperationItems() {
        this.operations.removeAll { it is OfficeInventory }
    }

    fun clearItems(items: List<OperationAct>) {
        if (this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun removeItem(item: OperationAct) {
        this.operations.remove(item)
        notifyDataSetChanged()
    }

    fun removeHead(item: OperationHead) {
        this.operations.removeAll { (it is OperationHead && it.name == item.name) }
        notifyDataSetChanged()
    }

    fun addItems(items: ArrayList<OperationAct>) {
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    fun clearAwaitAcceptedOperations() {
        this.operations.removeAll { it is OfficeAcceptedInventory }
    }
    fun clearAwaitSentOperations() {
        this.operations.removeAll { it is OfficeSentInventory }
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is OperationHead -> HEAD
            is OperationInit -> ACTION
            is OfficeAcceptedInventory -> OPERATION_ACCEPTED_AWAIT
            is OfficeSentInventory -> OPERATION_SENT_AWAIT
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
                    officeOperationsAdapterEvent?.onOperationInit(item)
                }
            }
        }
    }

    inner class OperationOfficeInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<OfficeInventory>(itemBinding) {
        override fun bind(item: OfficeInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = (item.name + "\n" + String.format(context.getString(R.string.total_quantity), (item.quantity.toString() + " " + item.type)))
                this.ivAction.setImageResource(R.drawable.ic_inventory)
                this.ivStatePending.isVisible = item.syncRequire == 1
                this.llAction.setOnClickListener {
                    if (item.syncRequire != 1) {
                        officeOperationsAdapterEvent?.onInventoryTransfer(item)
                    }
                }
            }
        }
    }

    inner class OperationOfficeAcceptAwaitInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<OfficeAcceptedInventory>(itemBinding) {
        override fun bind(item: OfficeAcceptedInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.name
                this.ivAction.setImageResource(R.drawable.ic_inventory)
                this.ivStatePending.isVisible = true
            }
        }
    }

    inner class OperationOfficeSentAwaitInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<OfficeSentInventory>(itemBinding) {
        override fun bind(item: OfficeSentInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.name
                this.ivAction.setImageResource(R.drawable.ic_inventory)
                this.ivStatePending.isVisible = true
            }
        }
    }

}