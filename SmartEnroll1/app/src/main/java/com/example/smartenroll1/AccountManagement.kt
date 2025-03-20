package com.example.smartenroll1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartenroll1.mainScreens.Models.AccountListViewModel
import com.example.smartenroll1.mainScreens.MyItemRecyclerViewAdapter
import com.example.smartenroll1.databinding.FragmentAccountManagementBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AccountManagement : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentAccountManagementBinding
    private val viewModel: AccountListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        binding = FragmentAccountManagementBinding.inflate(layoutInflater)

        val recycler = binding.rvAccountList
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Search
        binding.etFilterName.setText(viewModel.filterName.value)
        val searchBtn = binding.btnSearch
        searchBtn.setOnClickListener {
            viewModel.onSearch(binding.etFilterName.text.toString())
        }

        val pages = Array(6) { (it + 1).toString() }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, pages)
        val pageSelect = binding.acvPageSelect
        pageSelect.setText("1")
        pageSelect.setAdapter(arrayAdapter)
        pageSelect.setOnItemClickListener { parent, view, position, id ->
            val pageNumber = pageSelect.text.toString().toIntOrNull() ?: 1
            viewModel.onSearch(binding.etFilterName.text.toString(), pageNumber)
        }

        val fragmentContext = this

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.listAccount.collectLatest {
                        recycler.adapter =
                            MyItemRecyclerViewAdapter(it, findNavController())
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_account_management, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountManagement.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountManagement().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}