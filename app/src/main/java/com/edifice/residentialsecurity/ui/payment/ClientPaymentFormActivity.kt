package com.edifice.residentialsecurity.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.edifice.residentialsecurity.databinding.ActivityClientPaymentFormBinding
import com.fevziomurtekin.payview.Payview
import com.fevziomurtekin.payview.data.PayModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class ClientPaymentFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientPaymentFormBinding

    val TAG = "ActivityClientPayment"
    var cvv = ""
    var cardName = ""
    var cardNumber = ""
    var cardExMonth = ""
    var cardExYear = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientPaymentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.payview.setOnDataChangedListener(object: Payview.OnChangelistener{
            override fun onChangelistener(payModel: PayModel?, isFillAllComponents: Boolean) {
                cardName = payModel?.cardOwnerName.toString()
                cardNumber = payModel?.cardNo.toString()
                cardExMonth = payModel?.cardMonth.toString()
                cardExYear = payModel?.cardYear.toString()
                cvv = payModel?.cardCv.toString()

                Log.d(TAG,"CarName: $cardName")
                Log.d(TAG,"cardNumber: $cardNumber")
                Log.d(TAG,"cardExMonth: $cardExMonth")
                Log.d(TAG,"cardExYear: $cardExYear")
                Log.d(TAG,"cvv: $cvv")
            }
        })

        binding.payview.setPayOnclickListener{

        }

    }


}