package com.edifice.residentialsecurity.ui.home.administratorResident.updateResident

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
import com.edifice.residentialsecurity.databinding.ActivityUpdateResidentBinding
import com.edifice.residentialsecurity.databinding.ActivityUpdateSecurityBinding
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.home.administratorResident.createResident.RegisterResidentViewState
import com.edifice.residentialsecurity.ui.login.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class UpdateResidentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateResidentBinding

    var user: User?= null
    private val updateResidentViewModel: UpdateResidentViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPrefsRepositoryImpl

    var residentData : User? = null
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateResidentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI(){
        initListeners()
        initObservers()
    }
    private fun initListeners() {
        getUserFromSession()

        residentData = gson.fromJson(intent.getStringExtra("residentData"), User::class.java)
        Log.d("QA", residentData.toString())
        binding.edittextName.setText(residentData?.name)
        binding.edittextLastname.setText(residentData?.lastname)
        binding.edittextPhone.setText(residentData?.phone)
        binding.edittextEmail.setText(residentData?.email)
        binding.edittextDni.setText(residentData?.dni)
        binding.edittextPassword.setText(residentData?.password)
        binding.edittextTower.setText(residentData?.tower)
        binding.edittextApartament.setText(residentData?.apartament)

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

        binding.edittextTower.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextTower.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextTower.onTextChanged { onFieldChanged() }

        binding.edittextTower.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
        binding.edittextTower.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
        binding.edittextTower.onTextChanged { onFieldChanged() }

        with(binding){
            btnUpdate.setOnClickListener {
                it.dismissKeyboard()
                updateResidentViewModel.onSignInSelected(
                    User(
                        id = residentData?.id,
                        setid = residentData?.idset,
                        name = binding.edittextName.text.toString(),
                        lastname = binding.edittextLastname.text.toString(),
                        phone = binding.edittextPhone.text.toString(),
                        email = binding.edittextEmail.text.toString(),
                        dni = binding.edittextDni.text.toString(),
                        password = binding.edittextPassword.text.toString(),
                        tower = binding.edittextTower.text.toString(),
                        apartament = binding.edittextApartament.text.toString(),

                    ),
                    user?.sessionToken!!
                )
            }
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            updateResidentViewModel.viewStateResident.collect{viewState ->
                updateUIResident(viewState)
            }
        }
        lifecycleScope.launchWhenCreated {
            updateResidentViewModel.registerUser.collect {
                when (it) {
                    is ViewUiState.Success -> {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.success_update_resident),
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
            updateResidentViewModel.onFieldsChangedResident(
                User(
                    name = binding.edittextName.text.toString(),
                    lastname = binding.edittextLastname.text.toString(),
                    phone = binding.edittextPhone.text.toString(),
                    email = binding.edittextEmail.text.toString(),
                    dni = binding.edittextDni.text.toString(),
                    password = binding.edittextPassword.toString(),
                    tower = binding.edittextTower.text.toString(),
                    apartament = binding.edittextApartament.text.toString()
                )
            )
        }
    }

    private fun updateUIResident(viewState: RegisterResidentViewState) {
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
            edittextTower.error =
                if (viewState.isValidTower) null else getString(R.string.valid_tower)
            edittextApartament.error =
                if (viewState.isValidApartament) null else getString(R.string.valid_apartament)
        }
    }

    private fun getUserFromSession(){
        val gson = Gson()
        if(!sharedPref.getData("user").isNullOrBlank()){
            user = gson.fromJson(sharedPref.getData("user"), User::class.java)
        }
    }

}