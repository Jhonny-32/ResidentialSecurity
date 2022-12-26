package com.edifice.residentialsecurity.ui.fragments.securityGuard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.edifice.residentialsecurity.ui.adapters.DataResidentAdapter
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.providers.UserProvider
import com.edifice.residentialsecurity.util.SharedPref
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecurityDataResidentFragment : Fragment() {

    val TAG ="SecurityDataResidentFragment"

    private var _binding: FragmentSecurityDataResidentBinding?= null
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
        _binding = FragmentSecurityDataResidentBinding.inflate(inflater, container, false)


        binding.recyclerviewDataResident.layoutManager = LinearLayoutManager(requireContext())
        sharedPref = SharedPref(requireActivity())

        getUserFromSession()

        userProvider = UserProvider(user?.sessionToken!!)



        getDataResident()

        if(!user?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(user?.image).into(binding.circleimageUser)
        }

        return binding.root
    }

    private fun getDataResident(){
        userProvider?.getDataResident(user?.conjunto!!)?.enqueue(object: Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.body() != null){
                    userData = response.body()!!
                    adapter = DataResidentAdapter(requireActivity(), userData)
                    binding.recyclerviewDataResident.adapter = adapter
                    Log.d("JHONNY",userData.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d(TAG, "Error ${t.message}")
                Toast.makeText(requireContext(), "Error ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref?.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref?.getData("user"), User::class.java)
        }
    }
}