package com.edifice.residentialsecurity.ui.manager

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.ui.MainActivity

class ManagerHomeActivity : AppCompatActivity() {

    companion object {
        fun create(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_home)
    }
}