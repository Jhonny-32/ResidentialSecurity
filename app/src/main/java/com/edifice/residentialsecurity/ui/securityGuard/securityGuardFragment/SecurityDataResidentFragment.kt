package com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.ui.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecurityDataResidentFragment : Fragment() {

    private var _binding: FragmentSecurityDataResidentBinding?= null
    private val binding get() = _binding!!


    private val securityViewModel: SecurityViewModel by viewModels()


    var user : User? = null
    var sharedPref: SharedPref? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityDataResidentBinding.inflate(inflater, container, false)


        binding.recyclerviewDataResident.layoutManager = LinearLayoutManager(requireContext())
        sharedPref = SharedPref(requireActivity())


        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(binding.circleimageUser)
        }

        return binding.root
    }



    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}