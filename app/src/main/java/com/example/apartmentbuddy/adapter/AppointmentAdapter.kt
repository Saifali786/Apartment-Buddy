package com.example.apartmentbuddy.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apartmentbuddy.model.AppointmentData
import com.example.apartmentbuddy.databinding.ItemRowBinding

/**  Reference : https://medium.com/@ezatpanah/recyclerview-in-android-with-example-in-depth-guide-94462a6b573b  */
class AppointmentAdapter(val items: MutableList<AppointmentData>) :
    RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {
    private lateinit var binding: ItemRowBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRowBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    inner class ViewHolder(itemView: ItemRowBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bind(item: AppointmentData) {
            binding.apply {
                tvFirstName.text = item.name
                tvDate.text = item.date
                tvTime.text = item.time
            }
        }
    }
}
