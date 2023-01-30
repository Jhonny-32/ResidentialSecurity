package com.edifice.residentialsecurity.ui.register.registerResidential

data class RegisterViewState(
    val isLoading: Boolean = false,
    val isValidNameResidential: Boolean = true,
    val isValidAddress: Boolean = true,
    val isValidNit: Boolean = true,
){
    fun registerValidated() =  isValidNit && isValidAddress && isValidNameResidential
}