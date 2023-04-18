package com.edifice.residentialsecurity.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.edifice.residentialsecurity.ui.client.clientFragment.ClientOrdersStatusFragment
import com.edifice.residentialsecurity.ui.securityGuard.securityGuardFragment.SecurityOrdersStatusFragment

class ClientTabsPagerAdapter(
    fragmentManager : FragmentManager,
    lifecycle: Lifecycle,
    private var numberOfTabs : Int
): FragmentStateAdapter(fragmentManager, lifecycle)  {

    override fun getItemCount(): Int {
        return numberOfTabs
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                val bundle = Bundle()
                bundle.putString("statuss", "ASIGNADO")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            1 -> {
                val bundle = Bundle()
                bundle.putString("statuss", "ENTREGADO")
                val clientStatusFragment = ClientOrdersStatusFragment()
                clientStatusFragment.arguments = bundle
                return clientStatusFragment
            }
            else -> return SecurityOrdersStatusFragment()
        }
    }
}