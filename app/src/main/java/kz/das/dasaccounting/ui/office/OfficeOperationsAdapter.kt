package kz.das.dasaccounting.ui.office

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.R
import kz.das.dasaccounting.core.ui.recyclerview.BaseViewHolder
import kz.das.dasaccounting.databinding.ItemOperationActionBinding
import kz.das.dasaccounting.databinding.ItemOperationInitBinding
import kz.das.dasaccounting.databinding.ItemSegmentBinding
import kz.das.dasaccounting.domain.data.action.OperationAct
import kz.das.dasaccounting.domain.data.action.OperationHead
import kz.das.dasaccounting.domain.data.action.OperationInit

class OfficeOperationsAdapter(val context: Context, var operations: ArrayList<OperationAct>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent? = null

    companion object {
        private const val HEAD = 0
        private const val ACTION = 1
        private const val OPERATION = 2
    }

    interface OnOfficeOperationsAdapterEvent {
        fun onOperationAct(operationAct: OperationAct)
        fun onOperationInit(operationAct: OperationAct)
    }

    fun setOfficeOperationsAdapterEvent(officeOperationsAdapterEvent: OnOfficeOperationsAdapterEvent) {
        this.officeOperationsAdapterEvent = officeOperationsAdapterEvent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out OperationAct> {
        val layoutInflater = LayoutInflater.from(context)
        return mapOf(
            HEAD to OperationHeadViewHolder(ItemSegmentBinding.inflate(layoutInflater, parent, false)),
            ACTION to OperationInitViewHolder(ItemOperationInitBinding.inflate(layoutInflater, parent, false)),
            OPERATION to OperationActViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false)))[viewType]
            ?: OperationActViewHolder(ItemOperationActionBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = operations[position]
        when (holder) {
            is OperationHeadViewHolder ->
                holder.bind(item as OperationHead, position)
            is OperationInitViewHolder ->
                holder.bind(item as OperationInit, position)
            is OperationActViewHolder ->
                holder.bind(item as OperationAct, position)
        }
    }

    override fun getItemCount() = operations.size

    fun putItems(items: ArrayList<OperationAct>) {
        this.operations.clear()
        this.operations.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (operations[position]) {
            is OperationHead -> HEAD
            is OperationInit -> ACTION
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

    inner class OperationActViewHolder internal constructor(private val itemBinding: ItemOperationActionBinding) : BaseViewHolder<OperationAct>(itemBinding) {
        override fun bind(item: OperationAct, position: Int) {
//            this.itemBinding.run {
//                this.actionName = item.type
//                this.iconId = R.drawable.ic_add
//            }
        }
    }

}