package com.example.smartenroll1.MainScreens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.smartenroll1.MainScreens.Models.AccountListViewModel
import com.example.smartenroll1.databinding.FragmentInfoBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class AccountListFragment : Fragment() {
    private var columnCount = 1
    private lateinit var binding: FragmentInfoBinding
    private val viewModel: AccountListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentInfoBinding.inflate(layoutInflater)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        val recycler = binding.rvAccountList
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val fragmentContext = this
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.monthCount.collectLatest {
                        binding.monthCount.text = "${it}"
                    }
                }
                launch {
                    viewModel.listAccount.collectLatest {
                        recycler.adapter = MyItemRecyclerViewAdapter(it, findNavController())
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = binding.root
        return view

//        val recycler = binding.rvAccountList
//        recycler.layoutManager = LinearLayoutManager(requireContext())
//        getAllAccounts { accounts ->
//            recycler.adapter = MyItemRecyclerViewAdapter(accounts)
//        }
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            AccountListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}