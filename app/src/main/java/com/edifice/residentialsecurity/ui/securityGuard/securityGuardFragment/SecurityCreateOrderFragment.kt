package com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.Sets
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.ResidentialService
import com.edifice.residentialsecurity.databinding.FragmentSecurityCreateOrderBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class SecurityCreateOrderFragment : Fragment() {

    private var _binding: FragmentSecurityCreateOrderBinding?= null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    lateinit var securityViewModels: SecurityViewModel

    @Inject
    lateinit var residentialService: ResidentialService
    var user : User? = null
    var idSets = ""

    var imageFile1 : File? = null
    var imageFile2 : File? = null
    var imageFile3 : File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityCreateOrderBinding.inflate(inflater, container, false)
        securityViewModels = ViewModelProvider(this)[SecurityViewModel::class.java]
        initUi()
        return binding.root
    }

    private fun initUi(){
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            securityViewModels.createOrder.collect{
                when(it){
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.create_success),
                            Snackbar.LENGTH_LONG
                        )
                        .setAnchorView(binding.btnCreate)
                        .show()
                        binding.progressBar.isVisible = false
                        resetForm()
                    }
                    is ViewUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is ViewUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initListeners() {
        getUserFromSession()
        binding.btnCreate.setOnClickListener { createOrder() }
        binding.imageviewImage1.setOnClickListener{selectImage(101)}
        binding.imageviewImage2.setOnClickListener{selectImage(102)}
        binding.imageviewImage3.setOnClickListener{selectImage(103)}

        securityViewModels.getSetsData(user?.conjunto!!, user?.sessionToken!!)
        securityViewModels.setsDataClient.observe(requireActivity(), Observer {data ->
            val spinner = binding.spinnerDataClient
            val arrayAdapter = ArrayAdapter<Sets>(requireContext(), R.layout.dropdown_item, data)
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    idSets = data[p2].id!!
                    binding.edittextNameResident.setText("${data[p2].name} ${data[p2].lastname}")
                    binding.edittextIdUser.setText(data[p2].idClient)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        })
    }

    private fun createOrder() {
        val iduser = binding.edittextIdUser.text.toString()
        val descriptions = binding.edittextDescription.text.toString()
        val status = binding.edittextStatus.text.toString()
        val files = ArrayList<File>()

        if(isValidForm(descriptions)){
            val order = Order(
                idsets = idSets,
                iduser = iduser,
                descriptions = descriptions,
                status = status
            )

            files.add(imageFile1!!)
            files.add(imageFile2!!)
            files.add(imageFile3!!)

            securityViewModels.sendOrder(files, order)
        }
    }
    fun resetForm(){
        binding.edittextDescription.setText("")
        imageFile1 = null
        imageFile2 = null
        imageFile3 = null
        binding.imageviewImage1.setImageResource(R.drawable.ic_person)
        binding.imageviewImage2.setImageResource(R.drawable.ic_person)
        binding.imageviewImage3.setImageResource(R.drawable.ic_person)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {

                val fileUri = data?.data

                when (requestCode) {
                    101 -> {
                        imageFile1 = File(fileUri?.path) // El ARCHIVO QUE VAMOS A GUARDAR EN EL SERVIDOR
                        binding.imageviewImage1?.setImageURI(fileUri)
                    }
                    102 -> {
                        imageFile2 = File(fileUri?.path) // El ARCHIVO QUE VAMOS A GUARDAR EN EL SERVIDOR
                        binding.imageviewImage2?.setImageURI(fileUri)
                    }
                    103 -> {
                        imageFile3 = File(fileUri?.path) // El ARCHIVO QUE VAMOS A GUARDAR EN EL SERVIDOR
                        binding.imageviewImage3?.setImageURI(fileUri)
                    }
                }

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectImage(requestCode: Int) {
        ImagePicker.with(this)
            .crop() //Para que el usuario pueda corta la imagen
            .compress(1024) //Para comprimir la imagen
            .maxResultSize(1080, 1080)//Maximo de tamaño en imagenes
            .start(requestCode)
    }

    private fun isValidForm( description: String):Boolean{
        if(description.isNullOrBlank()){
            Toast.makeText(requireContext(), "Ingrese descripcion del producto", Toast.LENGTH_SHORT).show()
            return false
        }
        if(imageFile1 == null){
            Toast.makeText(requireContext(), "Seleccione la imagen uno", Toast.LENGTH_SHORT).show()
            return false
        }
        if(imageFile2 == null){
            Toast.makeText(requireContext(), "Seleccione la imagen dos", Toast.LENGTH_SHORT).show()
            return false
        }
        if(imageFile3 == null){
            Toast.makeText(requireContext(), "Seleccione la imagen tres", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPref.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }
}