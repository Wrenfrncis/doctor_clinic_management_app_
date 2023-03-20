package com.example.navcomponent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.navcomponent.databinding.ItmpatBinding

class patadapter(val patients:List<ItemPat>) :
RecyclerView.Adapter<patadapter.ViewHolder>(){
    class ViewHolder(val binding : ItmpatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: ItemPat) {
            binding.pat = item
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItmpatBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    }

    override fun getItemCount(): Int {
    return patients.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data=patients[position]
        holder.bind(data)

    }
}