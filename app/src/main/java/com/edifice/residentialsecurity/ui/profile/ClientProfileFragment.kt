package com.edifice.residentialsecurity.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.ui.SelectRolesActivity
import com.edifice.residentialsecurity.databinding.FragmentClientProfileBinding
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.ui.login.MainActivity
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.client.update.ClientUpdateActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientProfileFragment : Fragment() {

    private var _binding: FragmentClientProfileBinding ?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    lateinit var profileViewModel: ProfileViewModel

    private var user:User?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientProfileBinding.inflate(inflater, container, false)
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]


        binding.btnSelectRol.setOnClickListener{ goToSelectRol()}
        binding.imageviewLogout.setOnClickListener{ logout() }
        binding.btnUpadateProfile.setOnClickListener{ goToUpdate() }

        getUserFromSession()
        dataUser()

        return binding.root
    }

    private fun initObservers(){
        profileViewModel.navigateLogin.observe(requireActivity()) {
            it.getContentIfNotHandled()?.let {
                goToLogin()
            }
        }
    }

    private fun dataUser(){
        binding.textviewName.text = user?.getFullName()
        binding.textviewEmail.text = user?.email
        binding.textviewPhone.text = user?.phone
        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(binding.circleimageUser)
        }
    }

    private fun goToLogin() {
        startActivity(MainActivity.create(requireContext()))
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }


    private fun goToSelectRol(){
        val i = Intent(requireContext(), SelectRolesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

    private fun goToUpdate(){
        val i = Intent(requireContext(), ClientUpdateActivity::class.java)
        startActivity(i)
    }

    private fun logout(){
        sharedPref.remove("user")
        sharedPref.remove("roles")
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

}