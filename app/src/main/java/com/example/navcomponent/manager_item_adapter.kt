package com.example.navcomponent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class manager_item_adapter (val manager:List<item_patmanager>) :
    RecyclerView.Adapter<manager_item_adapter.ViewHolder>(){

    class ViewHolder( view: View) : RecyclerView.ViewHolder(view){
        val name1=view.findViewById(R.id.text1) as TextView
        val birthdate1=view.findViewById(R.id.text2) as TextView
        val diagnoses=view.findViewById(R.id.text3) as TextView
        val prescription=view.findViewById(R.id.text4) as TextView

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): manager_item_adapter.ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.itemmanager, parent, false)
        )
    }

    override fun onBindViewHolder(holder: manager_item_adapter.ViewHolder, position: Int) {
        val data=manager[position]
        holder.name1.text=data.name
        holder.birthdate1.text=data.birthdate
        holder.diagnoses.text=data.diagnoses
        holder.prescription.text=data.prescription
    }

    override fun getItemCount(): Int {
        return manager.size
    }
}