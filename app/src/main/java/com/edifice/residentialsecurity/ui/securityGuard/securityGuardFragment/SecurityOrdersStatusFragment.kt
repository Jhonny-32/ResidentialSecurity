package com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityOrdersStatusBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.adapters.OrdersClientAdapter
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SecurityOrdersStatusFragment : Fragment() {

    private var _binding: FragmentSecurityOrdersStatusBinding?= null
    private val binding get() = _binding!!

    lateinit var securityViewModels: SecurityViewModel

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl
    private lateinit var adapter : OrdersClientAdapter

    var user: User? = null
    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityOrdersStatusBinding.inflate(inflater, container, false)
        securityViewModels = ViewModelProvider(this)[SecurityViewModel::class.java]

        status = arguments?.getString("statuss")!!
        getUserFromSession()
        binding.recyclerviewOrders.layoutManager = LinearLayoutManager(requireContext())
        securityViewModels.getOrders(status,user?.conjunto!! ,user?.sessionToken!!)
        securityViewModels.ordersByStatus.observe(requireActivity(), Observer {data ->
            adapter = OrdersClientAdapter(requireActivity(), data)
            binding.recyclerviewOrders.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

}