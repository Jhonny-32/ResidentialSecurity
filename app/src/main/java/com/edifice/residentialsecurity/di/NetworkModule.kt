package com.edifice.residentialsecurity.di

import android.content.Context
import android.content.SharedPreferences
import com.edifice.residentialsecurity.data.model.Residential
import com.edifice.residentialsecurity.data.model.Rol
import com.edifice.residentialsecurity.data.model.User
import com.edifice.residentialsecurity.data.network.OrderRoutes
import com.edifice.residentialsecurity.data.network.ResidentialsRoutes
import com.edifice.residentialsecurity.data.network.SetsRoutes
import com.edifice.residentialsecurity.data.network.UserRoutes
import com.edifice.residentialsecurity.di.sharedPreferencesDi.SharedPrefsRepositoryImpl
import com.edifice.residentialsecurity.ui.login.MainActivity
import com.google.gson.Gson
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.14:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideRoles(sharedPref :SharedPrefsRepositoryImpl): ArrayList<Rol> {
        val rolesJson = sharedPref.getData("user")
        val gson = Gson()
        val user = gson.fromJson(rolesJson, User::class.java)
        return user.roles!!
    }


    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit):UserRoutes{
        return retrofit.create(UserRoutes::class.java)
    }

    @Singleton
    @Provides
    fun provideResidentialApi(retrofit: Retrofit):ResidentialsRoutes{
        return retrofit.create(ResidentialsRoutes::class.java)
    }

    @Singleton
    @Provides
    fun provideOrderApi(retrofit: Retrofit):OrderRoutes{
        return retrofit.create(OrderRoutes::class.java)
    }

    @Singleton
    @Provides
    fun provideSetsApi(retrofit: Retrofit):SetsRoutes{
        return retrofit.create(SetsRoutes::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("com.edifice.residentialsecurity", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideUser(): User {
        return User(name = "", lastname = "", phone = "", email = "", dni = "", password = "")
    }

    @Singleton
    @Provides
    fun provideResidential():Residential{
        return Residential(id="", name = "", nit = "", address = "", lat = 0.0)
    }


}

@Component(modules = [NetworkModule::class])
interface SharedPrefsComponent {
    fun inject(activity: MainActivity)
}