package com.example.smartenroll1

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.smartenroll1.mainScreens.Models.AccountDetailViewModel
import com.example.smartenroll1.databinding.FragmentAccountInfoDetailBinding
import io.data2viz.charts.chart.Chart
import io.data2viz.charts.chart.chart
import io.data2viz.charts.chart.discrete
import io.data2viz.charts.chart.mark.area
import io.data2viz.charts.chart.quantitative
import io.data2viz.geom.Size
import io.data2viz.viz.VizContainerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
    private val viewModel: AccountDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAccountInfoDetailBinding.inflate(layoutInflater)
        val backButton = binding.btnBack.setOnClickListener {
            val navController = parentFragmentManager
            navController.popBackStackImmediate()
        }

        val id = arguments?.getString("accountId")?.toInt()
        viewModel.setAccountId(id ?: -1)
//        binding.tvId.text = arguments?.getString("accountId")
//        binding.tvUsername.text = arguments?.getString("accountName")
//        binding.tvEmail.text = arguments?.getString("email")
//        binding.tvArea.text = arguments?.getString("areaId")
//        binding.tvJoinDate.text = arguments?.getString("createdDate")

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.account.collectLatest {
                    binding.tvId.text = it?.accountId.toString()
                    binding.tvAccountName.text = it?.accountName.toString()
                    binding.tvEmail.text = it?.email.toString()
                    binding.tvArea.text = it?.areaName
//                    binding.tvJoinDate.text = it?.createdDate.toString()
                }
            }
        }
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

