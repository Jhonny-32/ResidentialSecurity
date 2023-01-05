package com.edifice.residentialsecurity.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.core.ViewUiState
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.core.ex.loseFocusAfterAction
import com.edifice.residentialsecurity.core.ex.onTextChanged
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.ActivityRegisterUserBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterUserActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, RegisterUserActivity::class.java)
    }

    private lateinit var binding: ActivityRegisterUserBinding
    private val registerUserViewModel: RegisterUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }
    private fun initUI(){
        initListeners()
        initObservers()
    }
    private fun initListeners(){
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
                registerUserViewModel.onSignInSelected(
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

    }

    private fun initObservers(){
        registerUserViewModel.navigateRegisterResidential.observe(this){
            it.getContentIfNotHandled()?.let {
                goToRegisterResidential()
            }
        }

        lifecycleScope.launch {
            registerUserViewModel.viewState.collect{viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            registerUserViewModel.registerUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.register_user_success),
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                        goToRegisterResidential()
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

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            registerUserViewModel.onFieldsChanged(
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

    private fun goToRegisterResidential(){
        startActivity(RegisterResidentialActivity.create(this))
    }

}