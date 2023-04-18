package com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.databinding.FragmentUpdateSecurityBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateSecurityFragment : Fragment() {

    private var _binding: FragmentUpdateSecurityBinding?= null
    private val binding get() = _binding!!

    var security : User? = null
    val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateSecurityBinding.inflate(inflater, container, false)

        val orderJson = arguments?.getString("securityData")
        security = gson.fromJson(orderJson, User::class.java)
        return binding.root
    }




}