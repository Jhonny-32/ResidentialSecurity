package com.edifice.residentialsecurity.ui.client.clientFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.FragmentClientOdersStatusBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityCreateOrderBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityOrdersStatusBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.adapters.OrdersClientAdapter
import com.edifice.residentialsecurity.ui.client.ClientViewModel
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClientOrdersStatusFragment : Fragment() {

    private var _binding: FragmentClientOdersStatusBinding?= null
    private val binding get() = _binding!!

    lateinit var clientViewModel: ClientViewModel

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    private lateinit var adapter : OrdersClientAdapter

    var user: User? = null
    var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientOdersStatusBinding.inflate(inflater, container, false)
        clientViewModel = ViewModelProvider(this)[ClientViewModel::class.java]
        status = arguments?.getString("statuss")!!
        getUserFromSession()
        binding.recyclerviewOrdersClient.layoutManager = LinearLayoutManager(requireContext())
        clientViewModel.getOrderClient(status, user?.conjunto!!, user?.id!!, user?.sessionToken!!)
        clientViewModel.orderClient.observe(requireActivity(), Observer {data ->
            adapter = OrdersClientAdapter(requireActivity(), data)
            binding.recyclerviewOrdersClient.adapter = adapter
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