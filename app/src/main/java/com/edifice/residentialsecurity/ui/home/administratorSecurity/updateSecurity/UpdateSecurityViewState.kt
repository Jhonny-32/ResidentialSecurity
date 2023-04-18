package com.edifice.residentialsecurity.ui.home.administratorSecurity.updateSecurity

data class UpdateSecurityViewState(
    val isLoading: Boolean = false,
    val isValidName: Boolean = true,
    val isValidLastName: Boolean = true,
    val isValidPhone: Boolean = true,
){
    fun updateValidated() = isValidName && isValidLastName && isValidPhone
}
