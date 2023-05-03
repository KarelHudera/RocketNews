package com.example.rocketnews.launches

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rocketnews.App
import com.example.rocketnews.apiSpaceX.ResponseSpaceXItem
import com.example.rocketnews.databaseSpaceX.SpaceXItem
import com.example.rocketnews.databaseSpaceX.SpaceXItemRepository
import com.example.rocketnews.databinding.ItemLaunchesBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LaunchesAdapter() :
    ListAdapter<ResponseSpaceXItem, LaunchesAdapter.ItemViewHolder>(ItemsDiffCallBack()) {

    private val appDatabase = App.instance.databaseSpaceX


    inner class ItemViewHolder(private val binding: ItemLaunchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @OptIn(DelicateCoroutinesApi::class)
        fun onBind(item: ResponseSpaceXItem) {
            with(binding){
                imageView.load(item.links?.patch?.small)
                name.text = item.name
                timer.text = item.dateUtc
                livestreamItem.apply {
                    text = "Livestream"
                    setOnClickListener {
                        val videoId = item.links?.youtubeId
                        if (videoId != null) {
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
                wiki.apply {
                    text = "Wiki"
                    setOnClickListener {
                        val url = item.links?.wikipedia
                        if (!url.isNullOrEmpty()) {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse(url)
                            context.startActivity(intent)
                        }
                    }
                }
                buttonFavorite.apply {
                    setOnClickListener {
                        val spaceXItem = SpaceXItem(
                            name = item.name.orEmpty(),
                            imageUrl = item.links?.patch?.small.orEmpty(),
                            dateUtc = item.dateUtc.orEmpty(),
                            youtubeId = item.links?.youtubeId.orEmpty(),
                            wikipediaUrl = item.links?.wikipedia.orEmpty()
                        )
                        GlobalScope.launch {
                            SpaceXItemRepository().insertSpaceX(spaceXItem)
                        }
                    }
                }
            }
        }
    }

    private class ItemsDiffCallBack : DiffUtil.ItemCallback<ResponseSpaceXItem>() {
        override fun areItemsTheSame(
            oldSpaceXData: ResponseSpaceXItem,
            newSpaceXData: ResponseSpaceXItem
        ): Boolean =
            oldSpaceXData == newSpaceXData

        override fun areContentsTheSame(
            oldSpaceXData: ResponseSpaceXItem,
            newSpaceXData: ResponseSpaceXItem
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