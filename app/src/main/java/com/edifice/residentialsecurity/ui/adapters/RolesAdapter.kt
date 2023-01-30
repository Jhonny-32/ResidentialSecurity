package com.edifice.residentialsecurity.ui.adapters

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
import com.edifice.residentialsecurity.ui.home.AdministratorHomeActivity
//import com.edifice.residentialsecurity.ui.client.home.ClientHomeActivity
import com.edifice.residentialsecurity.ui.manager.ManagerHomeActivity
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import com.edifice.residentialsecurity.databinding.CardviewRolesBinding
import com.edifice.residentialsecurity.data.model.Rol
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.client.home.ClientHomeActivity
import java.util.*
import javax.inject.Inject

class RolesAdapter @Inject constructor(
    private val roles : ArrayList<Rol>,
    private val sharedPref: SharedPrefsRepositoryImpl,
    val context: Activity
    ): RecyclerView.Adapter<RolesAdapter.RolesViewHolder>() {



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
        when (rol.name) {
            "ADMINISTRADOR" -> {
                sharedPref.save("rol", "ADMINISTRADOR")
                val i = Intent( context , AdministratorHomeActivity::class.java)
                context.startActivity(i)
            }
            "PROPIETARIO" -> {
                sharedPref.save("rol", "PROPIETARIO")
                val i = Intent( context , ClientHomeActivity::class.java)
                context.startActivity(i)
            }
            "VIGILANTE" -> {
                sharedPref.save("rol", "VIGILANTE")
                val i = Intent( context , SecurityHomeActivity::class.java)
                context.startActivity(i)
            }
            "MANAGER" -> {
                sharedPref.save("rol", "MANAGER")
                val i = Intent( context , ManagerHomeActivity::class.java)
                context.startActivity(i)
            }
        }
    }

    override fun getItemCount(): Int {return roles.size}

    inner class RolesViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val binding = CardviewRolesBinding.bind(view)

        val textViewRol : TextView = binding.textviewRol
        val imageViewRol : ImageView = binding.imageviewRol

    }
}