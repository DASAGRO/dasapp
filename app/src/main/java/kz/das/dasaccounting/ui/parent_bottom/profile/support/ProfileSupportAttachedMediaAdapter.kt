package kz.das.dasaccounting.ui.parent_bottom.profile.support

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.das.dasaccounting.databinding.ItemMediaBinding
import kz.das.dasaccounting.ui.parent_bottom.profile.support.data.Media
import kotlin.collections.ArrayList


class ProfileSupportAttachedMediaAdapter(val context: Context, val onSupportEvents: ProfileSupportAttachedMediaAdapterEvents,
                                         val list: ArrayList<Media>): RecyclerView.Adapter<ProfileSupportAttachedMediaAdapter.MediaVH>() {

    private val layoutInflater = LayoutInflater.from(context)

    interface ProfileSupportAttachedMediaAdapterEvents {
        fun onRemoveMedia(media: Media, position: Int)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaVH {
        return MediaVH(ItemMediaBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: MediaVH, position: Int) {
        holder.bind(list[position])
    }

    fun updateItem(media: Media) {
        notifyItemChanged(list.indexOf(media))
    }

    fun removeItem(media: Media) {
        if (!list.isNullOrEmpty()) {
            notifyItemRemoved(list.indexOf(media))
            list.remove(media)
        }
    }

    fun updateList(mediaList: List<Media>) {
        list.clear()
        list.addAll(mediaList)
        notifyDataSetChanged()
    }

    fun addList(mediaList: List<Media>) {
        list.addAll(mediaList.toMutableList())
        notifyDataSetChanged()
    }

    fun updateList(mediaList: ArrayList<Media>) {
        list.clear()
        list.addAll(mediaList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }

    inner class MediaVH(private val binding: ItemMediaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(media: Media) {
            binding.apply {
                this.ivClose.setOnClickListener { onSupportEvents.onRemoveMedia(media, adapterPosition) }
                this.media = media
                executePendingBindings()
            }
        }
    }
}
