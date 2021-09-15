package kz.das.dasaccounting.ui.parent_bottom.profile.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemHistoryBinding
import kz.das.dasaccounting.domain.data.history.HistoryTransfer
import kz.das.dasaccounting.domain.data.history.OperationType

class UserTransferHistoryAdapter(val context: Context, private var operations: ArrayList<HistoryTransfer>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var historyOperationsAdapterEvent: OnHistoryOperationsAdapterEvent? = null

    interface OnHistoryOperationsAdapterEvent {
        fun onClick(title: String?, descr: String?, type: String?, status: String?, qr: String?)
    }

    fun setHistoryOperationsAdapterEvent(historyOperationsAdapterEvent: OnHistoryOperationsAdapterEvent) {
        this.historyOperationsAdapterEvent = historyOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<HistoryTransfer> {
        val layoutInflater = LayoutInflater.from(context)
        return HistoryTransferViewHolder(ItemHistoryBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        if (holder is HistoryTransferViewHolder) {
            holder.bind(item, position)
        }
    }

    override fun getItemCount() = operations.size

    fun clearItems(items: List<HistoryTransfer>) {
        this.operations.removeAll(items)
        notifyDataSetChanged()
    }

    fun putItems(items: List<HistoryTransfer>) {
        operations.removeAll(items)
        operations.addAll(items)
        notifyDataSetChanged()
    }

    fun putItems(items: ArrayList<HistoryTransfer>) {
        operations.removeAll(items)
        operations.addAll(items)
        notifyDataSetChanged()
    }

    inner class HistoryTransferViewHolder internal constructor(private val itemBinding: ItemHistoryBinding):
        BaseViewHolder<HistoryTransfer>(itemBinding) {
        override fun bind(item: HistoryTransfer, position: Int) {
            this.itemBinding.run {
                historyTransfer = item
                userName = item.senderName
                tvInventoryQuantity.isGone = item.operationType == OperationType.DRIVER_ACCESSORY.status ||
                        item.operationType == OperationType.DRIVER.status
                this.rootHistory.setOnClickListener {
                    historyOperationsAdapterEvent?.onClick(item.title, item.descr, item.operationType, item.status, item.qrData)
                }
                executePendingBindings()
            }
        }
    }


}