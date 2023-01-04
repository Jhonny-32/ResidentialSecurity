package com.edifice.residentialsecurity.ui.fragments.client

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.ui.SelectRolesActivity
import com.edifice.residentialsecurity.databinding.FragmentClientProfileBinding
import android.content.Intent
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.ui.MainActivity
//import com.edifice.residentialsecurity.ui.client.update.ClientUpdateActivity
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson

class ClientProfileFragment : Fragment() {

    private val TAG ="ClientProfileFragment"
    private var _binding: FragmentClientProfileBinding ?= null
    private val binding get() = _binding!!

    private var sharedPref: SharedPref?= null
    private var user:User?=null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientProfileBinding.inflate(inflater, container, false)

        sharedPref = SharedPref(requireActivity())
        binding.btnSelectRol.setOnClickListener{ goToSelectRol()}
        binding.imageviewLogout.setOnClickListener{ logout() }
        binding.btnUpadateProfile.setOnClickListener{ goToUpdate() }

        getUserFromSession()

        binding.textviewName.text = "${user?.name} ${user?.lastname}"
        binding.textviewEmail.text = user?.email
        binding.textviewPhone.text = user?.phone

        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(binding.circleimageUser)
        }

        return binding.root
    }


    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

    private fun goToUpdate(){
        /*val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)*/
    }

    private fun logout(){
        sharedPref?.remove("user")
        val i = Intent(requireContext(), MainActivity::class.java)
        startActivity(i)
    }

}