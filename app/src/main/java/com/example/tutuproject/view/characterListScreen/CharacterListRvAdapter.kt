package com.example.tutuproject.view.characterListScreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutuproject.R
import com.example.tutuproject.data.models.Character
import com.example.tutuproject.databinding.CharacterRvItemBinding
import com.example.tutuproject.databinding.RvLoaderBinding
import com.example.tutuproject.others.RvItemDesigner
import com.squareup.picasso.Picasso

class CharacterListRvAdapter(
    val viewLifecycleOwner: LifecycleOwner,
    val showLoader: MutableLiveData<Boolean> = MutableLiveData(false),
    //lambda function to get the selected character
    val onItemSelected: (Character) -> Unit
) :
    ListAdapter<RvItemDesigner, RecyclerView.ViewHolder>(CharacterDiffUtil()) {

    //holder for casual character rv item
    inner class RvItemVh(val binding: CharacterRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(character: Character) {
            binding.characterName.text = character.characterName
            binding.origin.text = character.origin
            binding.statusAndSpecies.text = character.status + " " + character.species
            Picasso.get().load(character.image).into(binding.characterImg)
            binding.root.setOnClickListener {
                onItemSelected.invoke(character)
            }
        }
    }

    //holder for loader which will be shown while next page is being loaded from network call
    inner class LoaderVh(val binding: RvLoaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind() {
            showLoader.observe(viewLifecycleOwner, Observer {
                if (it) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            })
        }
    }

    class CharacterDiffUtil : DiffUtil.ItemCallback<RvItemDesigner>() {
        override fun areItemsTheSame(oldItem: RvItemDesigner, newItem: RvItemDesigner): Boolean {
            if (oldItem is RvItemDesigner.CasualItem && newItem is RvItemDesigner.CasualItem) {
                return oldItem.character.characterName == newItem.character.characterName
            } else return oldItem is RvItemDesigner.Loader && newItem is RvItemDesigner.Loader
        }

        override fun areContentsTheSame(oldItem: RvItemDesigner, newItem: RvItemDesigner): Boolean {
            if (oldItem is RvItemDesigner.CasualItem && newItem is RvItemDesigner.CasualItem) {
                return oldItem.equals(newItem)
            } else return oldItem is RvItemDesigner.Loader && newItem is RvItemDesigner.Loader
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return if (viewType == 0) {
            itemView =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.character_rv_item, parent, false)
            RvItemVh(CharacterRvItemBinding.bind(itemView))
        } else {
            itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.rv_loader, parent, false)
            LoaderVh(RvLoaderBinding.bind(itemView))
        }
    }

    //differentiating between Loader and Casual Rv Character Item
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is RvItemDesigner.CasualItem) {
            0
        } else {
            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        if (viewType == 0) {
            holder as RvItemVh
            val item = getItem(position) as RvItemDesigner.CasualItem
            holder.onBind(item.character)
        } else {
            holder as LoaderVh
            holder.onBind()
        }
    }
}