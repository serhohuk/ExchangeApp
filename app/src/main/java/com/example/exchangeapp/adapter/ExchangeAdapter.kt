package com.example.exchangeapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangeapp.R
import com.example.exchangeapp.data.Currency
import kotlin.collections.ArrayList

class ExchangeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list : ArrayList<Any> = ArrayList();

    inner class ItemCurrencyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var currencyName : TextView
        var currencyId : TextView
        var currencySymbol : TextView

        init {
            currencyName = itemView.findViewById(R.id.tv_currency_name)
            currencyId = itemView.findViewById(R.id.tv_currency_id)
            currencySymbol = itemView.findViewById(R.id.tv_currency_symbol)
        }

        fun bind(currency: Currency){
            currencyName.text = currency.currencyName
            currencyId.text = currency.id
            currencySymbol.text = currency.currencySymbol ?: "_"

            setOnClickListener {
                onItemClickListener?.let { it(currency) }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==0){
            return ItemCurrencyViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_currency,parent,false)))
        }
        return ItemCurrencyViewHolder((LayoutInflater.from(parent.context).inflate(R.layout.item_currency,parent,false)))
        TODO("new viewHolder")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            0->{
                val itemViewHolder  = holder as ItemCurrencyViewHolder
                itemViewHolder.bind(list[position] as Currency)
            }
            else ->{

            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position] is Currency){
            return 0
        }
        return 1
    }

    private var onItemClickListener : ((Currency)->Unit)? = null

    fun setOnClickListener(listener : (Currency)->Unit){
        onItemClickListener = listener
    }


}