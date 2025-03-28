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

class AccountManagement : Fragment() {

    private lateinit var binding: FragmentAccountManagementBinding
    private val viewModel: AccountListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAccountManagementBinding.inflate(layoutInflater)

        val recycler = binding.rvAccountList
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // Search
        val searchBtn = binding.btnSearch
        searchBtn.setOnClickListener {
            viewModel.onSearch(binding.etFilterName.text.toString())
        }

        val pages = Array(viewModel.pageCount.value) { (it + 1).toString() }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, pages)
        val pageSelect = binding.acvPageSelect
        pageSelect.setText("${viewModel.pageNumber.value}")
        pageSelect.setAdapter(arrayAdapter)
        pageSelect.setOnItemClickListener { parent, view, position, id ->
            val pageNumber = pageSelect.text.toString().toIntOrNull() ?: 1
            viewModel.onSearch(binding.etFilterName.text.toString(), pageNumber)
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.listAccount.collectLatest {
                        recycler.adapter =
                            MyItemRecyclerViewAdapter(it, findNavController())
                    }
                }
                launch {
                    viewModel.pageNumber.collectLatest {
                        pageSelect.setText("${viewModel.pageNumber.value}")
                    }
                }
                launch {
                    viewModel.filterName.collectLatest {
                        binding.etFilterName.setText(viewModel.filterName.value)
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
    }
}