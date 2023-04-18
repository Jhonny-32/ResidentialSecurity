package com.edifice.residentialsecurity.ui.home.administratorOrder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.databinding.FragmentAdministratorOrdersBinding
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdministratorOrdersFragment : Fragment() {

    private var _binding: FragmentAdministratorOrdersBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdministratorOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

}