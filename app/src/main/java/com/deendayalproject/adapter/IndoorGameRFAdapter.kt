package com.deendayalproject.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.IndoorGame
import com.deendayalproject.model.request.TrainingCenter
import com.deendayalproject.model.response.IndoorRFGameResponse
import com.deendayalproject.model.response.IndoorRFGameResponseDetails
import com.deendayalproject.model.response.LivingAreaInformationRes

class IndoorGameRFAdapter(
    private var games: MutableList<IndoorRFGameResponseDetails>,
    private val onDeleteClick: (IndoorRFGameResponseDetails) -> Unit
) : RecyclerView.Adapter<IndoorGameRFAdapter.IndoorGameViewHolder>() {
    inner class IndoorGameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvGameView: TextView = itemView.findViewById(R.id.tvGameView)
        val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndoorGameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_indoor_rf_game, parent, false)
        return IndoorGameViewHolder(view)
    }

    override fun onBindViewHolder(holder: IndoorGameViewHolder, position: Int) {
        val game = games[position]
//        holder.tvGameName.text = "Game #${game.indoorGameName}"
        holder.tvGameName.text = game.indoorGameName
        holder.tvGameView.text =  game.indoorGamePdf ?: "N/A"

        holder.tvGameView.setOnClickListener {
            onDeleteClick(game)
        }
    }

    override fun getItemCount(): Int = games.size
    fun updateData(newList: List<IndoorRFGameResponseDetails>) {
        games = newList as MutableList<IndoorRFGameResponseDetails>
        notifyDataSetChanged()
    }

}



