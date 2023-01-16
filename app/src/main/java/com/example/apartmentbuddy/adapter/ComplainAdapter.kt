package com.example.apartmentbuddy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.R
import com.example.apartmentbuddy.model.ComplainListData

class ComplainAdapter(private val complain: List<ComplainListData>) : RecyclerView.Adapter<ComplainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.complainlistview,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ticketID.text = complain[position].ticketid
        holder.status.text = complain[position].status
        holder.categorys.text = complain[position].category
        holder.subjects.text = complain[position].subject
        holder.descriptions.text = complain[position].description

    }

    override fun getItemCount(): Int {
        return complain.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ticketID : TextView
        var status : TextView
        var categorys : TextView
        var subjects : TextView
        var descriptions: TextView


        init {
            ticketID = itemView.findViewById(R.id.ticketComplains)
            status = itemView.findViewById(R.id.statusComplain)
            categorys = itemView.findViewById(R.id.CategoryComplains)
            subjects = itemView.findViewById(R.id.SubjectComplains)
            descriptions = itemView.findViewById(R.id.DecriptionComplains)
        }
    }
}

