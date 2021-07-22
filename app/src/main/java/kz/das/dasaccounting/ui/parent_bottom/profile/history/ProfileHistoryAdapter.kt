package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemHistoryBinding
import kz.das.dasaccounting.domain.common.TransportType
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.drivers.TransportAcceptedInventory
import kz.das.dasaccounting.domain.data.drivers.TransportSentInventory
import kz.das.dasaccounting.domain.data.history.*
import kz.das.dasaccounting.domain.data.office.OfficeAcceptedInventory
import kz.das.dasaccounting.domain.data.office.OfficeSentInventory


class ProfileHistoryAdapter(val context: Context, private var operations: ArrayList<OperationAct>, val userName: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var historyOperationsAdapterEvent: OnHistoryOperationsAdapterEvent? = null

    companion object {
        private const val HISTORY_WAREHOUSE = 1
        private const val HISTORY_OFFICE = 2
        private const val HISTORY_TRANSPORT = 3
        private const val OPERATION_SENT_AWAIT = 4
        private const val OPERATION_ACCEPTED_AWAIT = 5
        private const val OPERATION_DRIVER_SENT_AWAIT = 6
        private const val OPERATION_DRIVER_ACCEPTED_AWAIT = 7
    }

    interface OnHistoryOperationsAdapterEvent {
        fun onClick(title: String?, descr: String?, type: String?, status: String?)
    }

    fun setHistoryOperationsAdapterEvent(historyOperationsAdapterEvent: OnHistoryOperationsAdapterEvent) {
        this.historyOperationsAdapterEvent = historyOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            HISTORY_WAREHOUSE to OperationHistoryWarehouseInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            HISTORY_OFFICE to OperationHistoryOfficeInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            HISTORY_TRANSPORT to OperationHistoryTransportInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            OPERATION_ACCEPTED_AWAIT to OperationOfficeAcceptAwaitInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            OPERATION_SENT_AWAIT to OperationOfficeSentAwaitInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER_SENT_AWAIT to OperationDriverSentInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)),
            OPERATION_DRIVER_ACCEPTED_AWAIT to OperationDriverAcceptedInventoryViewHolder(
                ItemHistoryBinding.inflate(layoutInflater, parent, false)))[viewType]
            ?: OperationHistoryOfficeInventoryViewHolder(ItemHistoryBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        when (holder) {
            is OperationHistoryTransportInventoryViewHolder ->
                holder.bind(item as HistoryTransportInventory, position)
            is OperationHistoryWarehouseInventoryViewHolder ->
                holder.bind(item as HistoryWarehouseInventory, position)
            is OperationHistoryOfficeInventoryViewHolder ->
                holder.bind(item as HistoryOfficeInventory, position)
            is OperationOfficeAcceptAwaitInventoryViewHolder ->
                holder.bind(item as OfficeAcceptedInventory, position)
            is OperationOfficeSentAwaitInventoryViewHolder ->
                holder.bind(item as OfficeSentInventory, position)
            is OperationDriverSentInventoryViewHolder ->
                holder.bind(item as TransportSentInventory, position)
            is OperationDriverAcceptedInventoryViewHolder ->
                holder.bind(item as TransportAcceptedInventory, position)

        }
    }

    override fun getItemCount() = operations.size

    fun clearItems(items: List<OperationAct>) {
        if (this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearWarehouseOperations(items: List<HistoryWarehouseInventory>) {
        if (!items.isNullOrEmpty() && this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearWarehouseOperations() {
        this.operations.removeAll { it is HistoryWarehouseInventory }
        notifyDataSetChanged()
    }

    fun clearOperations(items: List<HistoryOfficeInventory>) {
        if (!items.isNullOrEmpty() && this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearOperations() {
        this.operations.removeAll { it is HistoryOfficeInventory }
        notifyDataSetChanged()
    }

    fun clearTransports(items: List<HistoryTransportInventory>) {
        if (!items.isNullOrEmpty() && this.operations.containsAll(items)) {
            this.operations.removeAll(items)
        }
        notifyDataSetChanged()
    }

    fun clearTransports() {
        this.operations.removeAll { it is HistoryTransportInventory }
        notifyDataSetChanged()
    }

    fun removeItem(item: OperationAct) {
        this.operations.remove(item)
        notifyDataSetChanged()
    }

    fun addAll(operations: ArrayList<OperationAct>) {
        this.operations.addAll(operations)
        notifyDataSetChanged()
    }

    fun clearAwaitAcceptedOperations() {
        this.operations.removeAll { it is OfficeAcceptedInventory }
        notifyDataSetChanged()
    }

    fun clearAwaitSentOperations() {
        this.operations.removeAll { it is OfficeSentInventory }
        notifyDataSetChanged()
    }

    fun clearAwaitAcceptedTransports() {
        this.operations.removeAll { it is TransportAcceptedInventory }
        notifyDataSetChanged()
    }

    fun clearAwaitSentTransports() {
        this.operations.removeAll { it is TransportSentInventory }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is HistoryOfficeInventory -> HISTORY_OFFICE
            is HistoryWarehouseInventory -> HISTORY_WAREHOUSE
            is HistoryTransportInventory -> HISTORY_TRANSPORT
            is OfficeAcceptedInventory -> OPERATION_ACCEPTED_AWAIT
            is OfficeSentInventory -> OPERATION_SENT_AWAIT
            is TransportAcceptedInventory -> OPERATION_DRIVER_ACCEPTED_AWAIT
            is TransportSentInventory -> OPERATION_DRIVER_SENT_AWAIT
            else -> HISTORY_OFFICE
        }
    }


    inner class OperationHistoryTransportInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<HistoryTransportInventory>(itemBinding) {
        override fun bind(item: HistoryTransportInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.name
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), item.fullName)
                this.tvInventoryDate.text = item.dateTime
                this.tvInventoryQuantity.visibility = View.GONE
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.name,
                        (context.getString(R.string.gov_number) +
                                " " + item.stateNumber), if (item.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status, item.status
                    )
                }
            }
        }
    }


    inner class OperationHistoryWarehouseInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<HistoryWarehouseInventory>(itemBinding) {
        override fun bind(item: HistoryWarehouseInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.name
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), item.fullName)
                this.tvInventoryDate.text = item.dateTime
                this.tvInventoryQuantity.visibility = View.GONE
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.name,
                        (context.getString(R.string.seal_number) +
                                " " + item.sealNumber), OperationType.WAREHOUSE.status, item.status
                    )
                }
            }
        }
    }

    inner class OperationHistoryOfficeInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<HistoryOfficeInventory>(itemBinding) {
        override fun bind(item: HistoryOfficeInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.name
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), item.fullName)
                this.tvInventoryDate.text = item.dateTime
                this.tvInventoryQuantity.text = String.format(context.getString(R.string.total_quantity), item.quantity)
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.name,
                        (context.getString(R.string.inventory_total_quantity) +
                                " " + item.quantity +
                                " " + item.measurement + "\n" +
                                String.format(context.getString(R.string.inventory_sender_name), item.fullName)),
                        OperationType.OFFICE.status, item.status
                    )
                }
            }
        }
    }


    inner class OperationDriverSentInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<TransportSentInventory>(itemBinding) {
        override fun bind(item: TransportSentInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.model
                this.ivStatus.setImageResource(R.drawable.ic_banner_waiting_oval)
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), userName)
                this.tvInventoryDate.visibility = View.GONE
                this.tvInventoryQuantity.visibility = View.GONE
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.model,
                        (context.getString(R.string.gov_number) +
                                " " + item.stateNumber), if (item.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status, HistoryEnum.AWAIT.status
                    )
                }
            }
        }
    }


    inner class OperationDriverAcceptedInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<TransportAcceptedInventory>(itemBinding) {
        override fun bind(item: TransportAcceptedInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.model
                this.ivStatus.setImageResource(R.drawable.ic_banner_waiting_oval)
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), item.senderName)
                this.tvInventoryDate.visibility = View.GONE
                this.tvInventoryQuantity.visibility = View.GONE
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.model,
                        (context.getString(R.string.gov_number) +
                                " " + item.stateNumber), if (item.tsType == TransportType.TRAILED.type) OperationType.DRIVER_ACCESSORY.status else OperationType.DRIVER.status, HistoryEnum.AWAIT.status
                    )
                }
            }
        }
    }

    inner class OperationOfficeAcceptAwaitInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<OfficeAcceptedInventory>(itemBinding) {
        override fun bind(item: OfficeAcceptedInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.name
                this.ivStatus.setImageResource(R.drawable.ic_banner_waiting_oval)
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), item.senderName)
                this.tvInventoryDate.visibility = View.GONE
                this.tvInventoryQuantity.text = String.format(context.getString(R.string.total_quantity), item.quantity)
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.name,
                        (context.getString(R.string.inventory_total_quantity) +
                                " " + item.quantity +
                                " " + item.type + "\n" +
                                String.format(context.getString(R.string.inventory_sender_name), item.senderName)),
                        OperationType.OFFICE.status, HistoryEnum.AWAIT.status
                    )
                }
            }
        }
    }

    inner class OperationOfficeSentAwaitInventoryViewHolder internal constructor(private val itemBinding: ItemHistoryBinding) : BaseViewHolder<OfficeSentInventory>(itemBinding) {
        override fun bind(item: OfficeSentInventory, position: Int) {
            this.itemBinding.run {
                this.tvInventoryTitle.text = item.name
                this.ivStatus.setImageResource(R.drawable.ic_banner_waiting_oval)
                this.tvInventedUser.text = String.format(context.getString(R.string.inventory_sender_name), userName)
                this.tvInventoryDate.visibility = View.GONE
                this.tvInventoryQuantity.text = String.format(context.getString(R.string.total_quantity), item.quantity)
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.name,
                        (context.getString(R.string.inventory_total_quantity) +
                                " " + item.quantity +
                                " " + item.type + "\n" +
                                String.format(context.getString(R.string.inventory_sender_name), item.senderName)),
                        OperationType.OFFICE.status, HistoryEnum.AWAIT.status
                    )
                }
            }
        }
    }


}