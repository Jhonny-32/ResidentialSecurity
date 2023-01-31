package com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.ui.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecurityDataResidentFragment : Fragment() {

    private var _binding: FragmentSecurityDataResidentBinding?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    lateinit var securityViewModel: SecurityViewModel
    private lateinit var adapter: DataResidentAdapter
    var user : User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityDataResidentBinding.inflate(inflater, container, false)
        securityViewModel = ViewModelProvider(this)[SecurityViewModel::class.java]
        initUi()
        return binding.root
    }


    private fun initUi(){
        initObservers()
    }

    private fun initObservers() {
        getUserFromSession()
        binding.recyclerviewDataResident.layoutManager = LinearLayoutManager(requireContext())
        securityViewModel.dataResidential()
        securityViewModel.dataResident.observe(requireActivity(), Observer { data ->
            adapter = DataResidentAdapter(data)
            binding.recyclerviewDataResident.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(binding.circleimageUser)
        }
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}