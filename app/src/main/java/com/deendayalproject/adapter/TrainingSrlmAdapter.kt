package com.deendayalproject.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deendayalproject.R
import com.deendayalproject.model.request.TrainingCenter

class TrainingSrlmAdapter(

    private var centers: List<TrainingCenter>,
    private var onItemClick:(TrainingCenter)-> Unit
) : RecyclerView.Adapter<TrainingSrlmAdapter.CenterViewHolder>() {

    inner class CenterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.trainingCenterName)
        val order: TextView = view.findViewById(R.id.senctionOrder)
        val address: TextView = view.findViewById(R.id.trainingCenterAddress)
        val district: TextView =view.findViewById(R.id.districtName)
        val tcBoyCap: TextView =view.findViewById(R.id.tcBoyCap)
        val tcFemaleCap: TextView =view.findViewById(R.id.tcFemaleCap)
        val tcTotalCap: TextView =view.findViewById(R.id.tcTotalCap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_qteam_layout, parent, false)
        return CenterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CenterViewHolder, position: Int) {
        val center = centers[position]

        holder.name.text = "Training Center Name: ${center.trainingCenterName}"
        holder.address.text = "Training Center Address: ${center.trainingCenterAddress}"
        holder.order.text = "Sanction Order: ${center.senctionOrder}"
        holder.district.text="District Name: ${center.districtName}"
        holder.tcBoyCap.text="Tc Male Capacity: ${center.tcMaleCapacity}"
        holder.tcFemaleCap.text="Tc Female Capacity: ${center.tcFemaleCapacity}"
        holder.tcTotalCap.text="Training Center Total Capacity: ${center.tcCapacity}"
        holder.itemView.setOnClickListener {
            onItemClick(center)
        }
    }


    override fun getItemCount(): Int = centers.size

    fun updateData(newList: List<TrainingCenter>) {
        centers = newList
        notifyDataSetChanged()
    }
}