package com.edifice.residentialsecurity.ui.home.administratorResident

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentAdministratorDataResidentBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.ui.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.ui.adapters.DataResidentForAdmiAdapter
import com.edifice.residentialsecurity.ui.adapters.DataUserAdapter
import com.edifice.residentialsecurity.ui.home.administratorResident.createResident.CreateResidentFragment
import com.edifice.residentialsecurity.ui.home.administratorSecurity.AdmiSecurityViewModel
import com.edifice.residentialsecurity.ui.home.administratorSecurity.createSecurity.CreateSecurityFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdministratorDataResidentFragment : Fragment() {

    private var _binding: FragmentAdministratorDataResidentBinding?= null
    private val binding get() = _binding!!

    lateinit var admiViewModel: AdmiResidentViewModel
    private lateinit var adapter: DataResidentForAdmiAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministratorDataResidentBinding.inflate(inflater, container, false)
        admiViewModel = ViewModelProvider(this)[AdmiResidentViewModel::class.java]
        initUi()
        return binding.root
    }

    private fun initUi(){
        initObservers()
    }

    private fun initObservers() {
        binding.recyclerDataUser.layoutManager = LinearLayoutManager(requireContext())
        admiViewModel.dataResident.observe(requireActivity()) { data ->
            adapter = DataResidentForAdmiAdapter(requireActivity(), data)
            binding.recyclerDataUser.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        binding.addUser.setOnClickListener {
            changeFragment(CreateResidentFragment())
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}