package com.edifice.residentialsecurity.ui.fragments.securityGuard

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.ui.adapters.TabsPagerAdapter
import com.edifice.residentialsecurity.databinding.FragmentSecurityOrdersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class SecurityOrdersFragment : Fragment() {
    private var _binding : FragmentSecurityOrdersBinding?=null
    private val binding get() = _binding!!

    var myView : View ?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecurityOrdersBinding.inflate(inflater, container, false)
        myView = binding.root

        binding.tabLayout.setSelectedTabIndicatorColor(Color.BLACK)
        binding.tabLayout.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.tabLayout.tabTextColors = ContextCompat.getColorStateList(requireContext(), R.color.black)
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabLayout.isInlineLabel = true

        var numberOfTabs = 4

        val adapter = TabsPagerAdapter(requireActivity().supportFragmentManager, lifecycle, numberOfTabs)
        binding.viewpager.adapter = adapter
        binding.viewpager.isUserInputEnabled = true

        TabLayoutMediator(binding.tabLayout, binding.viewpager){ tab, position ->

            when(position){
                0 -> {
                    tab.text = "ASIGANADO"
                }
                1 -> {
                    tab.text = "ENTREGADO"
                }
            }

        }.attach()

        return myView
    }


}