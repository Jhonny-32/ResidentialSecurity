package com.edifice.residentialsecurity.ui.fragments.administrator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdministratorDataResidentFragment : Fragment() {

    private var _binding: FragmentSecurityDataResidentBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecurityDataResidentBinding.inflate(inflater, container, false)
        return binding.root
    }

}