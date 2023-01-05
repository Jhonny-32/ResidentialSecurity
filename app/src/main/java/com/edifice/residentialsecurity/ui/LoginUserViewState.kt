package com.edifice.residentialsecurity.ui

data class LoginUserViewState(
    val isLoading: Boolean = false,
    val isValidUser: Boolean = true,
    val isValidPassword: Boolean = true,
)