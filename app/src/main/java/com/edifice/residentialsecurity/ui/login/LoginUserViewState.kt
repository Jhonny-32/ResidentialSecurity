package com.edifice.residentialsecurity.ui.login

data class LoginUserViewState(
    val isLoading: Boolean = false,
    val isValidUser: Boolean = true,
    val isValidPassword: Boolean = true,
)