package kz.das.dasaccounting.ui.drivers

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
import kz.das.dasaccounting.domain.data.drivers.*
import kz.das.dasaccounting.domain.data.office.OfficeAcceptedInventory
import kz.das.dasaccounting.domain.data.office.OfficeInventory
import kz.das.dasaccounting.domain.data.office.OfficeSentInventory

class DriverOperationsAdapter(val context: Context, private var operations: ArrayList<OperationAct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent? = null

    companion object {
        private const val HEAD = 0
        private const val ACTION = 1
        private const val OPERATION = 2
        private const val OPERATION_DRIVER = 3
        private const val OPERATION_SENT_AWAIT = 4
        private const val OPERATION_ACCEPTED_AWAIT = 5
        private const val OPERATION_DRIVER_SENT_AWAIT = 6
        private const val OPERATION_DRIVER_ACCEPTED_AWAIT = 7
        private const val OPERATION_DRIVER_FLIGEL_DATA_AWAIT = 8
    }

    interface OnOfficeOperationsAdapterEvent {
        fun onOperationAct(operationAct: OperationAct)
        fun onOperationInit(operationAct: OperationAct)
        fun onInventoryTransfer(officeInventory: OfficeInventory)
        fun onInventoryTransportTransfer(transportInventory: TransportInventory)
    }

    fun setOfficeOperationsAdapterEvent(officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent) {
        this.officeOperationsAdapterEvent = officeOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            HEAD to OperationHeadViewHolder(ItemSegmentBinding.inflate(layoutInflater, parent, false)),
            ACTION to OperationInitViewHolder(ItemOperationInitBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationOfficeInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_ACCEPTED_AWAIT to OperationOfficeAcceptAwaitInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_SENT_AWAIT to OperationOfficeSentAwaitInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER_SENT_AWAIT to OperationDriverSentInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER_ACCEPTED_AWAIT to OperationDriverAcceptedInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER_FLIGEL_DATA_AWAIT to OperationDriverFligelDataAwaitInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER to OperationDriverInventoryViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)))[viewType]
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
            is OperationDriverInventoryViewHolder ->
                holder.bind(item as TransportInventory, position)
            is OperationOfficeAcceptAwaitInventoryViewHolder ->
                holder.bind(item as OfficeAcceptedInventory, position)
            is OperationOfficeSentAwaitInventoryViewHolder ->
                holder.bind(item as OfficeSentInventory, position)
            is OperationDriverSentInventoryViewHolder ->
                holder.bind(item as TransportSentInventory, position)
            is OperationDriverAcceptedInventoryViewHolder ->
                holder.bind(item as TransportAcceptedInventory, position)
            is OperationDriverFligelDataAwaitInventoryViewHolder ->
                holder.bind(item as FligelAwaitProduct, position)
        }
    }

    override fun getItemCount() = operations.size

//    fun putItems(items: ArrayList<OperationAct>) {
//        this.operations.clear()
//        this.operations.addAll(items)
//        notifyDataSetChanged()
//    }

    fun clearItems(items: List<OperationAct>) {
        if (this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearOperations(items: List<OfficeInventory>) {
        if (!items.isNullOrEmpty() && this.operations.containsAll(items)) {
                this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearOperations() {
        this.operations.removeAll { it is OfficeInventory }
        notifyDataSetChanged()
    }

    fun clearTransports(items: List<TransportInventory>) {
        if (!items.isNullOrEmpty() && this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearTransports() {
        this.operations.removeAll { it is TransportInventory }
        notifyDataSetChanged()
    }

    fun removeItem(item: OperationAct) {
        this.operations.remove(item)
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

    fun clearAwaitAcceptedOperations() {
        this.operations.removeAll { it is OfficeAcceptedInventory }
    }

    fun clearAwaitSentOperations() {
        this.operations.removeAll { it is OfficeSentInventory }
    }

    fun clearAwaitAcceptedTransports() {
        this.operations.removeAll { it is TransportAcceptedInventory }
    }

    fun clearAwaitSentTransports() {
        this.operations.removeAll { it is TransportAcceptedInventory }
    }

    fun clearAwaitFligelData() {
        this.operations.removeAll { it is FligelAwaitProduct }
    }

    fun addOperations(items: ArrayList<OperationAct>) {
        if (this.operations.containsAll(items)) {
            this.operations.removeAll(items)
            this.operations.removeAll { (it is OfficeInventory) }
        }
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is OperationHead -> HEAD
            is OperationInit -> ACTION
            is OfficeInventory -> OPERATION
            is OfficeAcceptedInventory -> OPERATION_ACCEPTED_AWAIT
            is OfficeSentInventory -> OPERATION_SENT_AWAIT
            is TransportAcceptedInventory -> OPERATION_DRIVER_ACCEPTED_AWAIT
            is TransportSentInventory -> OPERATION_DRIVER_SENT_AWAIT
            is FligelAwaitProduct -> OPERATION_DRIVER_FLIGEL_DATA_AWAIT
            else -> OPERATION_DRIVER
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
                this.tvName.text = item.name
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

    inner class OperationDriverInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<TransportInventory>(itemBinding) {
        override fun bind(item: TransportInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.model
                this.ivAction.setTsTypeImage(item)
                this.ivStatePending.isVisible = item.isPending
                this.llAction.setOnClickListener {
                    if (!item.isPending) {
                        officeOperationsAdapterEvent?.onInventoryTransportTransfer(item)
                    }
                }
            }
        }
    }

    inner class OperationDriverSentInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<TransportSentInventory>(itemBinding) {
        override fun bind(item: TransportSentInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.model
                this.ivAction.setTsTypeImage(item.tsType)
                this.ivStatePending.isVisible = true
            }
        }
    }


    inner class OperationDriverAcceptedInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<TransportAcceptedInventory>(itemBinding) {
        override fun bind(item: TransportAcceptedInventory, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.model
                this.ivAction.setTsTypeImage(item.tsType)
                this.ivStatePending.isVisible = true
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

    inner class OperationDriverFligelDataAwaitInventoryViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<FligelAwaitProduct>(itemBinding) {
        override fun bind(item: FligelAwaitProduct, position: Int) {
            this.itemBinding.run {
                this.tvName.text = item.name
                this.ivAction.setImageResource(R.drawable.ic_inventory)
                this.ivStatePending.isVisible = true
            }
        }
    }

}