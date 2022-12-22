package com.edifice.residentialsecurity.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.edifice.residentialsecurity.fragments.securityGuard.SecurityOrdersStatusFragment

class TabsPagerAdapter(
    fragmentManager : FragmentManager,
    lifecycle: Lifecycle,
    var numberOfTabs : Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                val bundle = Bundle()
                bundle.putString("status", "ASIGNADA")
                val securityStatusFragment = SecurityOrdersStatusFragment()
                securityStatusFragment.arguments = bundle
                return securityStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("status", "ENTREGADA")
                val securityStatusFragment = SecurityOrdersStatusFragment()
                securityStatusFragment.arguments = bundle
                return securityStatusFragment
            }
            else -> return SecurityOrdersStatusFragment()
        }
    }

}