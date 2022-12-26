package com.edifice.residentialsecurity.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.edifice.residentialsecurity.core.ex.dismissKeyboard
import com.edifice.residentialsecurity.core.ex.loseFocusAfterAction
import com.edifice.residentialsecurity.core.ex.onTextChanged
import com.edifice.residentialsecurity.databinding.ActivityRegisterBinding
import com.edifice.residentialsecurity.data.model.Residential
import com.google.android.material.snackbar.Snackbar

class RegisterResidentialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerResidentialViewModel: RegisterResidentialViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {

        binding.edittextName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextName.onTextChanged { onFieldChanged() }

        binding.edittextAddress.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextAddress.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextAddress.onTextChanged { onFieldChanged() }

        binding.edittextNit.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextNit.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextNit.onTextChanged { onFieldChanged() }

        with(binding){
            btnRegister.setOnClickListener {
                it.dismissKeyboard()
                registerResidentialViewModel.onSignInSelected(
                    Residential(
                        name = binding.edittextName.text.toString(),
                        address = binding.edittextAddress.text.toString(),
                        nit = binding.edittextNit.text.toString()
                    )
                )
            }
        }
    }
    private fun initObservers(){
        lifecycleScope.launchWhenStarted {
            registerResidentialViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            registerResidentialViewModel.loginUiState.collect {
                when (it) {
                    is RegisterResidentialViewModel.LoginUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            "Registrado Correctamente",
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is RegisterResidentialViewModel.LoginUiState.Error -> {
                        Snackbar.make(
                            binding.root,
                            it.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                    }
                    is RegisterResidentialViewModel.LoginUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun updateUI(viewState: RegisterViewState) {
        with(binding){
            edittextAddress.error =
                if (viewState.isValidAddress) null else "La direccion no es valido"
            edittextNit.error =
                if (viewState.isValidNit) null else "El nit no es valido"
            edittextName.error =
                if(viewState.isValidNameResidential) null else "El nombre no es valido"
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            registerResidentialViewModel.onFieldsChanged(
                Residential(
                    name = binding.edittextName.text.toString(),
                    address = binding.edittextAddress.text.toString(),
                    nit = binding.edittextNit.text.toString()
                )
            )
        }
    }

}