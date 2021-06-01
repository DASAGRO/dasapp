package kz.das.dasaccounting.core.ui.recyclerview

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(itemView: ViewBinding) : RecyclerView.ViewHolder(itemView.root) {
    abstract fun bind(item: T, position: Int)
}