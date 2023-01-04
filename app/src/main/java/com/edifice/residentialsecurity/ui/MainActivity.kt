package com.edifice.residentialsecurity.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.edifice.residentialsecurity.databinding.ActivityMainBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.home.AdministratorHomeActivity
import com.edifice.residentialsecurity.ui.manager.ManagerHomeActivity
import com.edifice.residentialsecurity.ui.securityGuard.SecurityHomeActivity
import com.edifice.residentialsecurity.ui.securityGuard.SecurityViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
    private lateinit var binding: ActivityMainBinding


    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
        mainViewModel.getUserFromSession()
    }

    private fun initUi(){
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.edittextEmail.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextEmail.onTextChanged { onFieldChanged() }

        binding.edittextPassword.loseFocusAfterAction(EditorInfo.IME_ACTION_DONE)
        binding.edittextPassword.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPassword.onTextChanged { onFieldChanged() }

        binding.txtRegister.setOnClickListener { mainViewModel.onRegisterSelected() }

        binding.btnLogin.setOnClickListener {
            it.dismissKeyboard()
            mainViewModel.onLoginSelected(
                email = binding.edittextEmail.text.toString(),
                password = binding.edittextPassword.text.toString()
            )
        }
    }

    private fun initObservers(){
        mainViewModel.navigateRegister.observe(this){
            it.getContentIfNotHandled()?.let {
                goToRegister()
            }
        }

        mainViewModel.navigateSecurity.observe(this){
            it.getContentIfNotHandled()?.let {
                goToSecurity()
            }
        }

        lifecycleScope.launchWhenStarted {
            mainViewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            mainViewModel.loginUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.login_success),
                            Snackbar.LENGTH_LONG
                        ).show()
                        binding.progressBar.isVisible = false
                        goToSelectRol()
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

    private fun updateUI(viewState: LoginUserViewState) {
        with(binding) {
            edittextEmail.error =
                if (viewState.isValidUser) null else getString(R.string.login_error_email)
            edittextPassword.error =
                if (viewState.isValidPassword) null else getString(R.string.login_error_password)
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            mainViewModel.onFieldsChanged(
                email = binding.edittextEmail.text.toString(),
                password = binding.edittextPassword.text.toString()
            )
        }
    }
    private fun goToRegister() {
        startActivity(RegisterUserActivity.create(this))
    }

    private fun goToSelectRol() {
        startActivity(SelectRolesActivity.create(this))
    }

    private fun goToSecurity() {
       startActivity(SecurityHomeActivity.create(this))
    }

}