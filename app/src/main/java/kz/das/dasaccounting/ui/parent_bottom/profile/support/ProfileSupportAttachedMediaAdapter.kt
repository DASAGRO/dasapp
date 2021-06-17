package kz.das.dasaccounting.ui.parent_bottom.profile.support

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.databinding.ItemMediaBinding
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kotlin.collections.ArrayList


class ProfileSupportAttachedMediaAdapter(val context: Context, val onSupportEvents: ProfileSupportAttachedMediaAdapterEvents): RecyclerView.Adapter<ProfileSupportAttachedMediaAdapter.MediaVH>() {

    private val layoutInflater = LayoutInflater.from(context)

    interface ProfileSupportAttachedMediaAdapterEvents {
        fun onRemoveMedia(media: Media)
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media>() {

            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
                return oldItem == newItem
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaVH {
        return MediaVH(ItemMediaBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MediaVH, position: Int) {
        holder.bind(differ.currentList[position])
    }

    fun updateItem(courierShift: Media) {
        notifyItemChanged(differ.currentList.indexOf(courierShift))
    }

    fun updateList(shifts: List<Media>) {
        differ.submitList(shifts.toMutableList())
    }

    fun updateList(shifts: ArrayList<Media>) {
        differ.submitList(shifts)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MediaVH(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media) {
            binding.apply {
                this.media = media
                this.onSupportMediaEvents = onSupportMediaEvents
                executePendingBindings()
            }
        }
    }
}
