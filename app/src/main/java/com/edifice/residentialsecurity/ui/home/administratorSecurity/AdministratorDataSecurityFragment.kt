package com.edifice.residentialsecurity.ui.home.administratorSecurity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentAdministratorDataSecurityBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.adapters.DataUserAdapter
import com.edifice.residentialsecurity.ui.home.administratorSecurity.createSecurity.CreateSecurityFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdministratorDataSecurityFragment : Fragment() {

    private var _binding: FragmentAdministratorDataSecurityBinding?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    lateinit var admiViewModel: AdmiSecurityViewModel
    private lateinit var adapter: DataUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministratorDataSecurityBinding.inflate(inflater, container, false)
        admiViewModel = ViewModelProvider(this)[AdmiSecurityViewModel::class.java]
        initUi()
        return binding.root
    }

    private fun initUi() {
        initObservers()
        initListeners()
    }

    private fun initListeners(){
        binding.addUser.setOnClickListener {
            changeFragment(CreateSecurityFragment())
        }
    }

    private fun initObservers() {
        binding.recyclerDataUser.layoutManager = LinearLayoutManager(requireContext())
        admiViewModel.getData.observe(requireActivity()) { data ->
            adapter = DataUserAdapter(requireActivity(), data)
            binding.recyclerDataUser.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


}