package com.edifice.residentialsecurity.core

sealed class ViewUiState{
    object Success: ViewUiState()
    data class Error(val message: String) : ViewUiState()
    object Loading: ViewUiState()
    object Empty: ViewUiState()
}