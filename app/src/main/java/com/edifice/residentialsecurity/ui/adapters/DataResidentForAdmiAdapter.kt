package com.edifice.residentialsecurity.ui.adapters


import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.CardviewDataResidentBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity.UpdateSecurityActivity
import kotlin.collections.ArrayList

class DataResidentForAdmiAdapter(
    val context: Activity,
    private val data: ArrayList<User>
): RecyclerView.Adapter<DataResidentForAdmiAdapter.DataResidentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):DataResidentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DataResidentViewHolder(layoutInflater.inflate(R.layout.cardview_data_resident, parent, false))
    }

    override fun onBindViewHolder(holder: DataResidentViewHolder, position: Int) {
        val dataResident = data[position]

        holder.textViewName.text = dataResident.getFullName()
        holder.textViewPhone.text = dataResident.phone
        holder.textViewTower.text = dataResident.tower
        holder.textViewApartament.text = dataResident.apartament

        holder.itemView.setOnClickListener { goToResidentDetail(dataResident) }
    }

    private fun goToResidentDetail(resident: User) {
        val i = Intent(context, UpdateSecurityActivity::class.java)
        i.putExtra("residentData", resident.toJson())
        context.startActivity(i)
    }


    override fun getItemCount(): Int {return data.size}

    class DataResidentViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = CardviewDataResidentBinding.bind(view)

        val textViewName : TextView = binding.textviewName
        val textViewPhone : TextView = binding.textviewPhone
        val textViewTower : TextView = binding.textviewTower
        val textViewApartament : TextView = binding.textviewApartament
    }
}