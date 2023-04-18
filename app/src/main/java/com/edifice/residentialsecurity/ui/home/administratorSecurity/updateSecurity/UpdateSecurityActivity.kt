package com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity

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
import com.edifice.residentialsecurity.data.model.Order
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.databinding.ActivityClientUpdateBinding
import com.edifice.residentialsecurity.databinding.ActivityUpdateSecurityBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewModel
import com.edifice.residentialsecurity.ui.register.registerUser.RegisterUserViewState
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateSecurityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateSecurityBinding

    var securityData : User? = null
    val gson = Gson()

    var user : User?=null
    @Inject
    lateinit var sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl

    private val updateSecurityViewModel: UpdateSecurityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUi()
    }

    private fun initUi(){
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        securityData = gson.fromJson(intent.getStringExtra("securityData"), User::class.java)
        Log.d("QA", securityData.toString())
        binding.edittextName.setText(securityData?.name)
        binding.edittextLastname.setText(securityData?.lastname)
        binding.edittextPhone.setText(securityData?.phone)

        binding.edittextName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextName.onTextChanged { onFieldChanged() }

        binding.edittextLastname.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextLastname.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextLastname.onTextChanged { onFieldChanged() }

        binding.edittextPhone.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextPhone.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextPhone.onTextChanged { onFieldChanged() }

        with(binding){
            btnUpdate.setOnClickListener {
                it.dismissKeyboard()
                updateSecurityViewModel.onSignInSelected(
                    User(
                        id =securityData?.id,
                        name = binding.edittextName.text.toString(),
                        lastname = binding.edittextLastname.text.toString(),
                        phone = binding.edittextPhone.text.toString(),
                    )
                )
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            updateSecurityViewModel.viewState.collect{viewState ->
                updateUI(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            updateSecurityViewModel.updateUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.update_security),
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

    private fun updateUI(viewState: UpdateSecurityViewState) {
        with(binding){
            edittextName.error =
                if (viewState.isValidName) null else getString(R.string.register_user_name)
            edittextLastname.error =
                if (viewState.isValidLastName) null else getString(R.string.register_user_lastname)
            edittextPhone.error =
                if(viewState.isValidPhone) null else getString(R.string.register_user_phone)
        }
    }

    private fun onFieldChanged(hasFocus: Boolean = false) {
        if (!hasFocus) {
            updateSecurityViewModel.onFieldsChanged(
                User(
                    name = binding.edittextName.text.toString(),
                    lastname = binding.edittextLastname.text.toString(),
                    phone = binding.edittextPhone.text.toString(),
                )
            )
        }
    }

    fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefsRepositoryImpl.getData("user").isNullOrBlank()) {
            user = gson.fromJson(sharedPrefsRepositoryImpl.getData("user"), User::class.java)
        }
    }

}