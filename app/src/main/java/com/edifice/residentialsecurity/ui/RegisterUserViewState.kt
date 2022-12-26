package com.edifice.residentialsecurity.ui

data class RegisterUserViewState(
    val isLoading: Boolean = false,
    val isValidName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidPhone: Boolean = true,
    val isValidEmail: Boolean = true,
    val isValidDni: Boolean = true,
    val isValidPassword: Boolean = true

){
    fun registerValidated() =  isValidName && isValidLastName && isValidLastName && isValidPhone && isValidEmail && isValidDni && isValidPassword
}