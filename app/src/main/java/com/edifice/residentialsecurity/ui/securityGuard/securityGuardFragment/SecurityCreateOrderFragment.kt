package com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.network.ResidentialService
import com.edifice.residentialsecurity.databinding.FragmentSecurityCreateOrderBinding
import com.edifice.residentialsecurity.databinding.FragmentSecurityDataResidentBinding
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
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

    lateinit var securityViewModels: SecurityViewModel

    @Inject
    lateinit var residentialService: ResidentialService

    var imageFile1 : File? = null
    var imageFile2 : File? = null
    var imageFile3 : File? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecurityCreateOrderBinding.inflate(inflater, container, false)
        securityViewModels = ViewModelProvider(this)[SecurityViewModel::class.java]

        securityViewModels.getUserFromSession()

        binding.btnCreate.setOnClickListener { createOrder() }
        binding.imageviewImage1.setOnClickListener{selectImage(101)}
        binding.imageviewImage2.setOnClickListener{selectImage(102)}
        binding.imageviewImage3.setOnClickListener{selectImage(103)}

        return binding.root
    }

    private fun createOrder() {
        val idsets = binding.edittextIdSets.text.toString()
        val iduser = binding.edittextIdUser.text.toString()
        val descriptions = binding.edittextDescription.text.toString()
        val status = binding.edittextStatus.text.toString()
        val files = ArrayList<File>()


        val order = Order(
            idsets = idsets,
            iduser = iduser,
            descriptions = descriptions,
            status = status
        )

        files.add(imageFile1!!)
        files.add(imageFile2!!)
        files.add(imageFile3!!)

        securityViewModels.sendOrder(files, order)
        Log.d("PRUEBA", "HAS HECHO CLICK ${order}, ${files[0].toString()}, ${files[1].toString()},${files[2].toString()}")
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


}