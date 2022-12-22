package com.edifice.residentialsecurity.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.CardviewDataResidentBinding
import com.edifice.residentialsecurity.models.Rol
import com.edifice.residentialsecurity.models.Sets
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.util.SharedPref
import kotlin.collections.ArrayList

class DataResidentAdapter(private val context: FragmentActivity, private val data: ArrayList<User>): RecyclerView.Adapter<DataResidentAdapter.DataResidentViewHolder>() {

    private val sharedPref = SharedPref(context)

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
    }

    private fun goToToRol(rol: Rol){
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