package com.edifice.residentialsecurity.ui.home.administratorOrder

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentAdministratorOrdersBinding
import com.edifice.residentialsecurity.ui.adapters.TabsPagerAdapter
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdministratorOrdersFragment : Fragment() {

    private var _binding: FragmentAdministratorOrdersBinding?= null
    private val binding get() = _binding!!

    var myView : View ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministratorOrdersBinding.inflate(inflater, container, false)
        myView = binding.root

        binding.tabLayout.setSelectedTabIndicatorColor(Color.BLACK)
        binding.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.tabLayout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)
        binding.tabLayout.maxScrollAmount
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabLayout.isInlineLabel = true

        val numberOfTabs = 2

        val adapter = TabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)
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

        return myView as LinearLayout
    }

}