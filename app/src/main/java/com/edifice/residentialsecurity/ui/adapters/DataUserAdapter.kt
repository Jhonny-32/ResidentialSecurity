package com.edifice.residentialsecurity.ui.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity.UpdateSecurityActivity


class DataUserAdapter(val context: Activity, private val user: ArrayList<User>) :
    RecyclerView.Adapter<DataUserAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_data, parent, false)
        return DataViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = user[position]

        holder.textViewName.text = data.name
        holder.textViewLastName.text = data.lastname
        holder.textViewEmail.text = data.email
        holder.textViewPhone.text = data.phone
        holder.textViewDni.text = data.dni

        holder.itemView.setOnClickListener { goToSecurityDetail(data) }
    }

    private fun goToSecurityDetail(data: User) {
        val i = Intent(context, UpdateSecurityActivity::class.java)
        i.putExtra("securityData", data.toJson())
        context.startActivity(i)
    }

    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView
        val textViewLastName: TextView
        val textViewEmail: TextView
        val textViewPhone: TextView
        val textViewDni: TextView

        init {
            textViewName = view.findViewById(R.id.textview_name)
            textViewLastName = view.findViewById(R.id.textview_lastname)
            textViewEmail = view.findViewById(R.id.textview_email)
            textViewPhone = view.findViewById(R.id.textview_phone)
            textViewDni = view.findViewById(R.id.textview_dni)
        }
    }

}