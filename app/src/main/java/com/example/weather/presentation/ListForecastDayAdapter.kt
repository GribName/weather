package com.example.weather.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weather.databinding.ListItemForecastBinding
import com.example.weather.entity.Forecastday
import kotlin.math.roundToInt

class ListForecastDayAdapter : RecyclerView.Adapter<ListForecastDayViewHolder>() {

    private var data : List<Forecastday> = emptyList()

    fun setData( data : List<Forecastday> ) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ListForecastDayViewHolder {
        return ListForecastDayViewHolder( ListItemForecastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListForecastDayViewHolder, position: Int) {

        val item = data[ position ].day

        with( holder.binding ) {
            this.conditionText.text = item.condition.text.split( " " ).joinToString( "\n" )

            if ( item.avgtemp_c.roundToInt() > 0 ) this.avgTemp.text = "+" + item.avgtemp_c.roundToInt().toString()
            else this.avgTemp.text = item.avgtemp_c.roundToInt().toString()

            this.maxWind.text = item.maxwind_kph.toString()
            this.avgHumidity.text = item.avghumidity.toString()
            item.let {
                Glide.with( conditionIcon.context )
                    .load( "https:" + it.condition.icon )
                    .into( conditionIcon )
            }
        }
    }
}

class ListForecastDayViewHolder( val binding: ListItemForecastBinding ) : RecyclerView.ViewHolder( binding.root )