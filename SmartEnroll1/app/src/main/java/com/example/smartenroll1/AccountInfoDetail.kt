package com.example.smartenroll1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.databinding.FragmentAccountInfoDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountInfoDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountInfoDetail : Fragment() {

    private lateinit var binding: FragmentAccountInfoDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAccountInfoDetailBinding.inflate(layoutInflater)
        val backButton = binding.btnBack.setOnClickListener {
            val navController = parentFragmentManager
            navController.popBackStackImmediate()
        }

//        bundle.putString("accountId", item.accountId.toString())
//        bundle.putString("accountName", item.accountName)
//        bundle.putString("createdDate", item.createdDate)
//        bundle.putString("email", item.email)
//        bundle.putString("areaId", item.areaId.toString())


        binding.tvId.text = arguments?.getString("accountId")
        binding.tvUsername.text = arguments?.getString("accountName")
        binding.tvEmail.text = arguments?.getString("email")
        binding.tvArea.text = arguments?.getString("areaId")
        binding.tvJoinDate.text = arguments?.getString("createdDate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}