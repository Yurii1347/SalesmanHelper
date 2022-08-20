package com.vytivskyi.salesmanhelper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vytivskyi.salesmanhelper.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private lateinit var binding: FragmentSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSuccessBinding.inflate(inflater)

        val safeArgs: SuccessFragmentArgs by navArgs()
        val code = safeArgs.barcode

        binding.fragmentSuccessTextViewCode.text = code

        binding.fragmentSuccessButtonBackToScanner.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

}