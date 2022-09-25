package com.edifice.residentialsecurity.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.activities.Administrator.home.AdministratorHomeActivity
import com.edifice.residentialsecurity.activities.client.home.ClientHomeActivity
import com.edifice.residentialsecurity.activities.manager.ManagerHomeActivity
import com.edifice.residentialsecurity.activities.securityGuard.SecurityHomeActivity
import com.edifice.residentialsecurity.databinding.CardviewRolesBinding
import com.edifice.residentialsecurity.models.Rol
import com.edifice.residentialsecurity.util.SharedPref
import java.util.*

class RolesAdapter(private val roles : ArrayList<Rol>, val context: Activity): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {

    private val sharedPref = SharedPref(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RolesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RolesViewHolder(layoutInflater.inflate(R.layout.cardview_roles, parent, false))
    }

    override fun onBindViewHolder(holder: RolesViewHolder, position: Int) {
        val rol = roles[position]

        holder.textViewRol.text = rol.name
        Glide.with(holder.imageViewRol.context).load(rol.image).into(holder.imageViewRol)

        holder.itemView.setOnClickListener{ goToToRol(rol) }
    }

    private fun goToToRol(rol: Rol){
        if(rol.name == "ADMINISTRADOR"){

            sharedPref.save("rol", "ADMINISTRADOR")
            val i = Intent( context , AdministratorHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name =="PROPIETARIO"){

            sharedPref.save("rol", "PROPIETARIO")

            val i = Intent( context , ClientHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "VIGILANTE"){
            sharedPref.save("rol", "VIGILANTE")

            val i = Intent( context , SecurityHomeActivity::class.java)
            context.startActivity(i)
        }
        else if (rol.name == "MANAGER"){
            sharedPref.save("rol", "MANAGER")

            val i = Intent( context , ManagerHomeActivity::class.java)
            context.startActivity(i)
        }

    }

    override fun getItemCount(): Int {return roles.size}

    class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = CardviewRolesBinding.bind(view)

        val textViewRol : TextView = binding.textviewRol
        val imageViewRol : ImageView = binding.imageviewRol

    }
}