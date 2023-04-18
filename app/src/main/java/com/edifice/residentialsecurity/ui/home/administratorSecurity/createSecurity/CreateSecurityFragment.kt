package com.edifice.residentialsecurity.ui.home.administratorSecurity.createSecurity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.core.ex.loseFocusAfterAction
import com.edifice.residentialsecurity.core.ex.onTextChanged
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.FragmentCreateSecurityBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateSecurityFragment : Fragment() {

    private var _binding: FragmentCreateSecurityBinding?= null
    private val binding get() = _binding!!

    var user: User?= null
    lateinit var createViewModel: CreateViewModel

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSecurityBinding.inflate(inflater, container, false)
        createViewModel = ViewModelProvider(this)[CreateViewModel::class.java]
        initUI()
        return  binding.root
    }

    private fun initUI(){
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        getUserFromSession()
        binding.edittextName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextName.onTextChanged { onFieldChanged() }

        binding.edittextLastname.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextLastname.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextLastname.onTextChanged { onFieldChanged() }

        binding.edittextPhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextPhone.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPhone.onTextChanged { onFieldChanged() }

        binding.edittextEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextEmail.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextEmail.onTextChanged { onFieldChanged() }

        binding.edittextDni.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextDni.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextDni.onTextChanged { onFieldChanged() }

        binding.edittextPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPassword.onTextChanged { onFieldChanged() }


        with(binding){
            btnRegister.setOnClickListener {
                it.dismissKeyboard()
                createViewModel.onSignInSelected(
                    User(
                        name = binding.edittextName.text.toString(),
                        lastname = binding.edittextLastname.text.toString(),
                        phone = binding.edittextPhone.text.toString(),
                        email = binding.edittextEmail.text.toString(),
                        dni = binding.edittextDni.text.toString(),
                        password = binding.edittextPassword.text.toString(),
                        residentialID = "1"
                    ),
                    user?.sessionToken!!
                )
            }
        }

    }

    private fun initObservers(){

        lifecycleScope.launch {
            createViewModel.viewState.collect{viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            createViewModel.registerUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_user_success),
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
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

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            createViewModel.onFieldsChanged(
                User(
                    name = binding.edittextName.text.toString(),
                    lastname = binding.edittextLastname.text.toString(),
                    phone = binding.edittextPhone.text.toString(),
                    email = binding.edittextEmail.text.toString(),
                    dni = binding.edittextDni.text.toString(),
                    password = binding.edittextPassword.toString()
                )
            )
        }
    }

    private fun updateUI(viewState: RegisterUserViewState) {
        with(binding){
            edittextName.error =
                if (viewState.isValidName) null else getString(R.string.register_user_name)
            edittextLastname.error =
                if (viewState.isValidLastName) null else getString(R.string.register_user_lastname)
            edittextPhone.error =
                if(viewState.isValidPhone) null else getString(R.string.register_user_phone)
            edittextEmail.error =
                if(viewState.isValidEmail) null else getString(R.string.register_user_email)
            edittextDni.error =
                if(viewState.isValidDni) null else getString(R.string.register_user_dni)
            edittextPassword.error =
                if(viewState.isValidPassword) null else getString(R.string.register_user_password)
        }
    }


    private fun getUserFromSession(){
        val gson = Gson()
        if(!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

}