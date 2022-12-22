package com.edifice.residentialsecurity.fragments.client

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.databinding.FragmentClientOrdersBinding
import com.edifice.residentialsecurity.models.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientOrdersFragment : Fragment() {

    val TAG ="ClientOrdersFragment"

    private var _binding: FragmentClientOrdersBinding?= null
    private val binding get() = _binding!!

    var userProvider : UserProvider?=null
    var adapter: DataResidentAdapter? = null
    var user : User? = null
    var sharedPref: SharedPref? = null
    var userData = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClientOrdersBinding.inflate(inflater, container, false)
        sharedPref = SharedPref(requireActivity())
        getUserFromSession()
        userProvider = UserProvider(user?.sessionToken!!)
        return binding.root
    }


    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }

}