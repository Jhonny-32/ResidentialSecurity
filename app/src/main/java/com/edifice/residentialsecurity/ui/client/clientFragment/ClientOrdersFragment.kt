package com.edifice.residentialsecurity.ui.client.clientFragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.ui.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.databinding.FragmentClientOrdersBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.ui.adapters.ClientTabsPagerAdapter
import com.edifice.residentialsecurity.ui.adapters.TabsPagerAdapter
import com.edifice.residentialsecurity.util.SharedPref
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClientOrdersFragment : Fragment() {



    private var _binding: FragmentClientOrdersBinding?= null
    private val binding get() = _binding!!

    var user : User? = null
    var sharedPref: SharedPref? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientOrdersBinding.inflate(inflater, container, false)
        binding.tabLayout.setSelectedTabIndicatorColor(Color.BLACK)
        binding.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.tabLayout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)
        binding.tabLayout.maxScrollAmount
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabLayout.isInlineLabel = true

        val numberOfTabs = 2

        val adapter = ClientTabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)
        binding.viewpager.adapter = adapter
        binding.viewpager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewpager){ tab, position ->

            when(position){
                0 -> {
                    tab.text = "ASIGNADO"
                }
                1 -> {
                    tab.text = "ENTREGADO"
                }
            }

        }.attach()

        return binding.root
    }



    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

}