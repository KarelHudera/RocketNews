package com.example.rocketnews.launches

import android.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load

import com.example.rocketnews.databaseSpaceX.SpaceXItem
import com.example.rocketnews.databaseSpaceX.SpaceXItemRepository
import com.example.rocketnews.databinding.ItemLaunchesBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LaunchesFavouriteAdapter :
    ListAdapter<SpaceXItem, LaunchesFavouriteAdapter.ItemViewHolder>(ItemsDiffCallBack()) {

    private val repository = SpaceXItemRepository()

    inner class ItemViewHolder(private val binding: ItemLaunchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @OptIn(DelicateCoroutinesApi::class)
        fun onBind(item: SpaceXItem) {
            with(binding) {
                imageView.load(item.imageUrl)
                name.text = item.name
                timer.text = item.dateUtc
                livestreamItem.apply {
                    text = "Livestream"
                    setOnClickListener {
                        val videoId = item.youtubeId
                        if (videoId != null) {
                            if (videoId.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.setPackage("com.google.android.youtube")
                                try {
                                    context.startActivity(intent)
                                } catch (e: ActivityNotFoundException) {
                                    // YouTube app is not installed, open in browser instead
                                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$videoId"))
                                    context.startActivity(webIntent)
                                }
                            }
                        }
                    }
                }
                wiki.apply {
                    text = "Wiki"
                    setOnClickListener {
                        val url = item.wikipediaUrl
                        if (!url.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            context.startActivity(intent)
                        }
                    }
                }
                buttonFavorite.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val selectedItem = getItem(position)
                        GlobalScope.launch {
                            repository.deleteSpaceX(selectedItem)
                        }
                        notifyItemChanged(position)
                    }
                }
            }
        }
    }

    private class ItemsDiffCallBack : DiffUtil.ItemCallback<SpaceXItem>() {
        override fun areItemsTheSame(
            oldSpaceXData: SpaceXItem,
            newSpaceXData: SpaceXItem
        ): Boolean =
            oldSpaceXData == newSpaceXData

        override fun areContentsTheSame(
            oldSpaceXData: SpaceXItem,
            newSpaceXData: SpaceXItem
        ): Boolean =
            oldSpaceXData.name == newSpaceXData.name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemLaunchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}