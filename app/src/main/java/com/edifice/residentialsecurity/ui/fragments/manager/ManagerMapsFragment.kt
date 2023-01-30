package com.edifice.residentialsecurity.ui.fragments.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.databinding.FragmentManagerMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManagerMapsFragment : Fragment() {

    private var _binding: FragmentManagerMapsBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentManagerMapsBinding.inflate(inflater, container, false)

        return binding.root
    }


}