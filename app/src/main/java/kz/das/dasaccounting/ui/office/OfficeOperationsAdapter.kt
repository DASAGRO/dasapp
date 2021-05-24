package kz.das.dasaccounting.ui.office

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class OfficeOperationsAdapter(context: Context, operations: ArrayList<Any>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val OPERATION_HEAD = 0
    private val OPERATION_ITEM = 1
    private val OPERATION_ADD = 2

    private var listItems: ArrayList<Any> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    fun updateList(list: ArrayList<Any>) {

    }
}