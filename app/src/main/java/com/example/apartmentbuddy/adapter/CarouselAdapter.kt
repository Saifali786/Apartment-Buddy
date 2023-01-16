package com.example.apartmentbuddy.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apartmentbuddy.databinding.CarouselItemBinding
// credits to https://www.youtube.com/watch?v=6CgkDGIzUC0 for showing carousel creation with Glide
class CarouselAdapter(private var photoList: List<Uri>): RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    class CarouselViewHolder(val binding: CarouselItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = CarouselItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val item = photoList[position]
        holder.binding.apply {
            Glide.with(itemImage)
                .load(item)
                .into(itemImage)
        }
    }

    override fun getItemCount(): Int = photoList.size
}