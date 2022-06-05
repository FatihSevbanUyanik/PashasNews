package com.example.pashanews.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pashanews.R
import com.example.pashanews.data.api.model.currency.Currency
import com.example.pashanews.databinding.ListItemCurrencyBinding
import com.example.pashanews.util.ImgUtil

class CurrencyAdapter: RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    private val callback = object: DiffUtil.ItemCallback<Currency>() {
        override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean = oldItem.title == newItem.title
        override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean = oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder =
        CurrencyViewHolder(ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    inner class CurrencyViewHolder(
        private val binding: ListItemCurrencyBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            val context = binding.root.context
            binding.tvCurrencyTitle.text = currency.title
            binding.tvPrice.text = currency.price

            if (currency.direction == "DOWN") {
                Glide
                    .with(binding.imgPriceDirection.context)
                    .load(R.drawable.ic_baseline_arrow_downward_24)
                    .into(binding.imgPriceDirection)

                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.red))
                binding.imgPriceDirection.setColorFilter(ContextCompat.getColor(context, R.color.red))
            } else {
                Glide
                    .with(binding.imgPriceDirection.context)
                    .load(R.drawable.ic_baseline_arrow_upward_24)
                    .into(binding.imgPriceDirection)

                binding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.green))
                binding.imgPriceDirection.setColorFilter(ContextCompat.getColor(context, R.color.green))
            }
        }
    }
}