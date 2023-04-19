package com.edifice.residentialsecurity.ui.home.administratorResident.createResident

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentAdministratorDataSecurityBinding
import com.edifice.residentialsecurity.databinding.FragmentCreateResidentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateResidentFragment : Fragment() {

    private var _binding: FragmentCreateResidentBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateResidentBinding.inflate(inflater, container, false)

        return binding.root
    }


}