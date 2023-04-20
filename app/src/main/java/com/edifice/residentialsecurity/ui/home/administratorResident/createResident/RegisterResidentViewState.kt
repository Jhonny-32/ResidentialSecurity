package com.edifice.residentialsecurity.ui.home.administratorResident.createResident

data class RegisterResidentViewState(
    val isLoading: Boolean = false,
    val isValidName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidPhone: Boolean = true,
    val isValidEmail: Boolean = true,
    val isValidDni: Boolean = true,
    val isValidPassword: Boolean = true,
    val isValidTower: Boolean = true,
    val isValidApartament: Boolean = true,
) {


    fun registerValidated() =  isValidName && isValidLastName && isValidLastName && isValidPhone && isValidEmail && isValidDni && isValidPassword && isValidTower && isValidApartament

}