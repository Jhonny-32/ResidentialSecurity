package com.edifice.residentialsecurity.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edifice.residentialsecurity.core.Event
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPrefsRepositoryImpl: SharedPrefsRepositoryImpl
):ViewModel() {

    private val _navigateLogin = MutableLiveData<Event<Boolean>>()
    val navigateLogin : LiveData<Event<Boolean>>
        get() = _navigateLogin

    fun logout(){
        sharedPrefsRepositoryImpl.remove("user")
        sharedPrefsRepositoryImpl.remove("rol")
        _navigateLogin.value = Event(true)
    }

}